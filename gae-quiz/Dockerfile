FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/gae-quiz-1.0.0.jar app.jar
ENV JAVA_OPTS="-DDATASTORE_HOST=http://localhost:8000 -DDATASTORE_EMULATOR_HOST=localhost:8000"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
