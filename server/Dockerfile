FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8883
ARG jksPassword
COPY target/server-1.0-SNAPSHOT.jar server.jar
ENTRYPOINT java -DserverJksPass=$jksPassword -Djava.security.egd=file:/dev/./urandom -jar /app.jar