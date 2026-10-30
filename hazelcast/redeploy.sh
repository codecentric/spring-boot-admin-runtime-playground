#!/bin/sh
# Issue 2795: simulates deploying a new version of health-simulator-1.
# The old container is stopped (graceful shutdown -> auto-deregistration through the load balancer)
# and a new one is started under a new hostname, like a new pod / task with a new IP would be.
# Usage: ./redeploy.sh [--kill]   (--kill = no graceful shutdown, no deregistration)

NETWORK="spring-boot-admin-runtime-playground-hazelcast_default"
OLD=$(docker ps --format '{{.Names}}' | grep '^health-simulator-1' | head -1)
NEW="health-simulator-1-$(date +%s)"

if [ -z "$OLD" ]; then
  echo "No running health-simulator-1 container found"
  exit 1
fi

if [ "$1" = "--kill" ]; then
  echo "Killing $OLD"
  docker kill "$OLD" >/dev/null
else
  echo "Stopping $OLD"
  docker stop "$OLD" >/dev/null
fi
[ "$OLD" != "health-simulator-1" ] && docker rm "$OLD" >/dev/null

echo "Starting $NEW"
docker run -d --name "$NEW" --hostname "$NEW" --network "$NETWORK" \
  -e SBA_URL=http://sba-lb -p 8281:8080 health-simulator-hazelcast >/dev/null

echo "Wait ~30s, then run ./inspect.sh. Old instances that stay registered on some nodes reproduce issue 2795."
