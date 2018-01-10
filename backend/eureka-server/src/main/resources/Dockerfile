FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD eureka-server-1.0.jar eureka-server-1.0.jar
RUN sh -c 'touch /eureka-server-1.0.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /eureka-server-1.0.jar -Dspring.profiles.active=prod" ]