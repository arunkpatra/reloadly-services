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

if [ -n "${TWILIO_DRY_RUN}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.integration.sms.twilio.dry-run=${TWILIO_DRY_RUN}"
fi

if [ -n "${TWILIO_MSG_SVC_ID}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.integration.sms.twilio.messaging-service-id=${TWILIO_MSG_SVC_ID}"
fi

if [ -n "${TWILIO_ACCOUNT_SID}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.integration.sms.twilio.twilio-account-sid=${TWILIO_ACCOUNT_SID}"
fi

if [ -n "${TWILIO_AUTH_TOKEN}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.integration.sms.twilio.twilio-auth-token=${TWILIO_AUTH_TOKEN}"
fi

if [ -n "${EMAIL_DRY_RUN}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.integration.email.ses.dry-run=${EMAIL_DRY_RUN}"
fi

if [ -n "${SENDER_EMAIL_ID}" ]; then
  RELOADLY_OPTS="$RELOADLY_OPTS -Dreloadly.integration.email.ses.from-email-id=${SENDER_EMAIL_ID}"
fi

echo RELOADLY_OPTS : "$RELOADLY_OPTS"
java $RELOADLY_OPTS -jar /app.jar
