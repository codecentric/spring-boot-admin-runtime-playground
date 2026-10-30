#!/bin/sh
# Prints the wiring of every SBA node, the registered instances per node and who polls the health simulators.

for node in 1 2 3; do
  echo "=== sba-$node"
  curl -s "http://localhost:819$node/actuator/playground" || echo "not reachable"
  echo
  echo "instances: $(curl -s -H "Accept: application/json" "http://localhost:809$node/instances" | grep -o '"id":"[0-9a-f]\{12\}"' | tr '\n' ' ')"
done

for sim in 1 2; do
  echo "=== health-simulator-$sim pollers"
  curl -s "http://localhost:828$sim/pollers" || echo "not reachable"
  echo
done

echo "=== mails in mailpit: $(curl -s http://localhost:8026/api/v1/messages | grep -o '"total":[0-9]*')"
