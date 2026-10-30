# https://hub.docker.com/_/eclipse-temurin/
FROM eclipse-temurin:25

VOLUME /tmp

COPY target/app.jar /opt/app/app.jar

# Disabled by default: the playground switches use @ConditionalOnProperty, which AOT evaluates at build time.
# Set SPRING_AOT_ENABLED=true (see scenarios/aot.env) to reproduce AOT related issues.
ENV SPRING_AOT_ENABLED=false

CMD ["bash", "-c", "java $JAVA_OPTS -jar /opt/app/app.jar"]
