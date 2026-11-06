# https://hub.docker.com/_/eclipse-temurin/
FROM eclipse-temurin:25

VOLUME /tmp

COPY target/app.jar /opt/app/app.jar

# AOT is off by default: the playground switches use @ConditionalOnProperty, which AOT evaluates at build time.
# Start with JAVA_OPTS=-Dspring.aot.enabled=true (see scenarios/aot.env) to reproduce AOT related issues.

CMD ["bash", "-c", "java $JAVA_OPTS -jar /opt/app/app.jar"]
