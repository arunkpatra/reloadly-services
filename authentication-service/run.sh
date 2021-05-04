#!/bin/sh

set -e

RELOADLY_OPTS=

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
