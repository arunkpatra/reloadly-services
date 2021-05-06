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

if [ -n "${SBA_SERVER_URL}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.boot.admin.client.url=${SBA_SERVER_URL}"
fi

if [ -n "${SBA_CLIENT_URL}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.boot.admin.client.instance.service-base-url=${SBA_CLIENT_URL}"
fi

if [ -n "${SWAGGER_HOST}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.integration.swagger.host=${SWAGGER_HOST}"
fi

if [ -n "${AUTH_SVC_ENDPOINT}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.auth.reloadly-auth-service-endpoint=${AUTH_SVC_ENDPOINT}"
fi

echo RELOADLY_OPTS : "$RELOADLY_OPTS"
java $RELOADLY_OPTS -jar /app.jar
