#!/usr/bin/env bash
# Stops the Day 3 stack by freeing its ports.
for p in 9000 9090 8082 8761; do
  PID=$(lsof -nP -iTCP:$p -sTCP:LISTEN -t 2>/dev/null)
  [ -n "$PID" ] && kill "$PID" 2>/dev/null && echo "stopped port $p (pid $PID)"
done
echo "done."
