#!/bin/sh
# Usage: ./run.sh [scenario]   (scenario = file name in ./scenarios without .env)
# Recreates all containers with the given scenario.

SCENARIO="${1:-default}"
ENV_FILE="scenarios/$SCENARIO.env"

if [ ! -f "$ENV_FILE" ]; then
  echo "Unknown scenario '$SCENARIO'. Available:"
  ls scenarios | sed 's/\.env$//;s/^/  /'
  exit 1
fi

echo "Starting scenario '$SCENARIO'"
docker compose --env-file "$ENV_FILE" up -d --force-recreate
