FROM openjdk:23-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE ${PORT:-8080}
ENTRYPOINT ["java", "-jar", "/app.jar"]