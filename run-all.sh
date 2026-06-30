#!/usr/bin/env bash
# Starts the full Day 3 microservices stack in dependency order:
#   eureka-server (8761) -> department-service (8082) -> employee-service (9090) -> api-gateway (9000)
# Logs go to /tmp/<service>_run.log. Use ./stop-all.sh (or Ctrl-C handling) to stop.
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT"

wait_for_log () {  # $1 = logfile, $2 = pattern, $3 = label
  echo "  waiting for $3 ..."
  for _ in $(seq 1 120); do
    grep -qE "$2" "$1" 2>/dev/null && { echo "  $3 is up."; return 0; }
    grep -qiE 'APPLICATION FAILED TO START|BUILD FAILURE' "$1" 2>/dev/null && { echo "  $3 FAILED — see $1"; return 1; }
    sleep 1
  done
  echo "  timed out waiting for $3 (see $1)"; return 1
}

start () {  # $1 = folder, $2 = runner ("mvnw" | "mvn"), $3 = log, $4 = startedPattern, $5 = label
  echo "==> starting $5 ($1)"
  : > "$3"
  if [ "$2" = "mvnw" ]; then ( cd "$1" && chmod +x ./mvnw && nohup ./mvnw -q spring-boot:run > "$3" 2>&1 & )
  else ( cd "$1" && nohup mvn -q spring-boot:run > "$3" 2>&1 & ); fi
  wait_for_log "$3" "$4" "$5"
}

start eurekaserver      mvnw /tmp/eureka_run.log 'Started EurekaserverApplication'      'eureka-server :8761'
start departmentservice mvnw /tmp/dept_run.log   'Started DepartmentserviceApplication' 'department-service :8082'
start employeeservice   mvn  /tmp/emp_run.log    'Started EmployeeserviceApplication'   'employee-service :9090'   # broken wrapper -> system mvn
start apigateway        mvnw /tmp/gw_run.log     'Started ApigatewayApplication'        'api-gateway :9000'

echo
echo "All services started. Eureka dashboard: http://localhost:8761"
echo "Give Eureka ~30s to propagate, then run ./verify.sh"
