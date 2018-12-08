FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ENV JAVA_OPTS="-DDATASTORE_HOST=http://localhost:8000 -DDATASTORE_EMULATOR_HOST=localhost:8000"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
