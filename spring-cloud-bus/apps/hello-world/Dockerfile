# https://hub.docker.com/_/eclipse-temurin/
FROM eclipse-temurin:17

VOLUME /tmp

COPY target/app.jar /opt/app/app.jar

CMD ["bash", "-c", "java $JAVA_OPTS -jar /opt/app/app.jar"]
