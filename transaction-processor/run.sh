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

if [ -n "${RELOADLY_LOG_FILE}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dlogging.file.name=${RELOADLY_LOG_FILE}"
fi

if [ -n "${RELOADLY_LOG_TO_CONTAINER_CONSOLE}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.log.to.container.console=${RELOADLY_LOG_TO_CONTAINER_CONSOLE}"
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

if [ -n "${SBA_SERVER_URL}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.boot.admin.client.url=${SBA_SERVER_URL}"
fi

if [ -n "${SBA_CLIENT_URL}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.boot.admin.client.instance.service-base-url=${SBA_CLIENT_URL}"
fi

if [ -n "${NOTIFICATION_SVC_ENDPOINT}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.notification.reloadly-notification-service-endpoint=${NOTIFICATION_SVC_ENDPOINT}"
fi

if [ -n "${ACCOUNT_SVC_ENDPOINT}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.api.transaction.processor.reloadly-account-service-endpoint=${ACCOUNT_SVC_ENDPOINT}"
fi

if [ -n "${SVC_ACCT_API_KEY}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.api.transaction.processor.svc-account-api-key=${SVC_ACCT_API_KEY}"
fi

if [ -n "${KAFKA_BOOTSTRAP_SERVERS}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dspring.kafka.bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS}"
fi


if [ -n "${RELOADLY_TRACING_ENABLED}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.enabled=${RELOADLY_TRACING_ENABLED}"
fi

if [ -n "${RELOADLY_TRACING_SERVICE_NAME}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.service-name=${RELOADLY_TRACING_SERVICE_NAME}"
fi

if [ -n "${RELOADLY_TRACING_HOST_NAME}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-agent-host=${RELOADLY_TRACING_HOST_NAME}"
fi

if [ -n "${RELOADLY_TRACING_PORT}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-agent-port=${RELOADLY_TRACING_PORT}"
fi

if [ -n "${RELOADLY_TRACING_ENDPOINT}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-endpoint=${RELOADLY_TRACING_ENDPOINT}"
fi

if [ -n "${RELOADLY_TRACING_SAMPLER_TYPE}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-sampler-type=${RELOADLY_TRACING_SAMPLER_TYPE}"
fi

if [ -n "${RELOADLY_TRACING_SAMPLER_PARAM}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-sampler-param=${RELOADLY_TRACING_SAMPLER_PARAM}"
fi

if [ -n "${RELOADLY_TRACING_PROPAGATION}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-propagation-formats=${RELOADLY_TRACING_PROPAGATION}"
fi

if [ -n "${RELOADLY_TRACING_TRANSMIT_SPAN}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.transmit-span=${RELOADLY_TRACING_TRANSMIT_SPAN}"
fi

if [ -n "${RELOADLY_TRACING_REPORTER_LOG_SPANS}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-reporter-log-spans=${RELOADLY_TRACING_REPORTER_LOG_SPANS}"
fi

if [ -n "${RELOADLY_TRACING_REPORTER_MAX_QUEUE_SIZE}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-reporter-max-queue-size=${RELOADLY_TRACING_REPORTER_MAX_QUEUE_SIZE}"
fi

if [ -n "${RELOADLY_TRACING_REPORTER_FLUSH_INTERVAL}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.tracing.jaeger-reporter-flush-interval=${RELOADLY_TRACING_REPORTER_FLUSH_INTERVAL}"
fi

echo RELOADLY_OPTS : "$RELOADLY_OPTS"
java $RELOADLY_OPTS -jar /app.jar
