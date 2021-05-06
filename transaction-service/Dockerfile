FROM adoptopenjdk:11.0.7_10-jre-hotspot
ENV TZ=Asia/Kolkata
VOLUME /tmp/transaction-service
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app.jar
COPY ./run.sh /
RUN ln -snf /user/share/zoneinfo/$TZ etc/localtime && echo $TZ > /etc/timezone && chmod +x /run.sh
ENTRYPOINT ["/run.sh"]