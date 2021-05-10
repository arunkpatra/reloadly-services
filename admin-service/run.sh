#!/bin/sh

set -e

RELOADLY_OPTS=

if [ -n "${RELOADLY_ENV}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly-env=${RELOADLY_ENV}"
else
  RELOADLY_ENV="dev"
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly-env=${RELOADLY_ENV}"
fi

if [ -n "${RELOADLY_SPRING_PROFILES}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.profiles.active=${RELOADLY_SPRING_PROFILES},${RELOADLY_ENV}"
else
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.profiles.active=${RELOADLY_ENV}"
fi

if [ -n "${DB_USER}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.datasource.hikari.username=${DB_USER}"
fi

if [ -n "${DB_PASSWORD}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.datasource.hikari.password=${DB_PASSWORD}"
fi

if [ -n "${DB_URL}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.datasource.url=${DB_URL}"
fi

echo RELOADLY_OPTS : "$RELOADLY_OPTS"
java $RELOADLY_OPTS -jar /app.jar
