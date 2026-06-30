#!/usr/bin/env bash
# End-to-end verification of the Day 3 stack THROUGH the API gateway (:9000).
# Waits for the gateway to discover services, then runs the full contract.
set -uo pipefail
GW="http://localhost:9000"
EUREKA="http://localhost:8761"

echo "==> waiting for gateway to discover employee-service (login stops returning 503)..."
code=000
for _ in $(seq 1 45); do
  code=$(curl -s -o /dev/null -w '%{http_code}' -X POST "$GW/api/auth/login" \
    -H 'Content-Type: application/json' -d '{"username":"admin","password":"admin123"}')
  [ "$code" = "200" ] && break
  sleep 2
done
echo "   login via gateway -> HTTP $code"

echo "==> Eureka registry:"
curl -s -H 'Accept: application/json' "$EUREKA/eureka/apps" \
  | python3 -c 'import json,sys; d=json.load(sys.stdin); print("  ", sorted(a["name"] for a in d["applications"].get("application",[])))' 2>/dev/null \
  || echo "  (could not read registry)"

TOKEN=$(curl -s -X POST "$GW/api/auth/login" -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')

pass=0; fail=0
check () {  # $1 = label, $2 = expected, $3 = actual
  if [ "$2" = "$3" ]; then echo "  PASS  $1 -> $3"; pass=$((pass+1));
  else echo "  FAIL  $1 -> $3 (expected $2)"; fail=$((fail+1)); fi
}

echo "==> contract via gateway (:9000):"
check "POST /api/auth/login"             200 "$(curl -s -o /dev/null -w '%{http_code}' -X POST "$GW/api/auth/login" -H 'Content-Type: application/json' -d '{"username":"admin","password":"admin123"}')"
check "GET  /api/employees  (no token)"  401 "$(curl -s -o /dev/null -w '%{http_code}' "$GW/api/employees")"
check "GET  /api/employees  (+admin)"    200 "$(curl -s -o /dev/null -w '%{http_code}' "$GW/api/employees" -H "Authorization: Bearer $TOKEN")"
check "GET  /api/departments"            200 "$(curl -s -o /dev/null -w '%{http_code}' "$GW/api/departments")"
check "GET  /api/departments/1"          200 "$(curl -s -o /dev/null -w '%{http_code}' "$GW/api/departments/1")"
check "GET  /api/departments/999 (404)"  404 "$(curl -s -o /dev/null -w '%{http_code}' "$GW/api/departments/999")"

echo
echo "Result: $pass passed, $fail failed."
[ "$fail" -eq 0 ]
