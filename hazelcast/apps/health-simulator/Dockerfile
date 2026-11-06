# https://hub.docker.com/_/eclipse-temurin/
FROM eclipse-temurin:25

VOLUME /tmp

COPY target/app.jar /opt/app/app.jar

ENV SPRING_AOT_ENABLED=true

CMD ["bash", "-c", "java $JAVA_OPTS -jar /opt/app/app.jar"]
