FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD stock-analyzer-0.0.1-SNAPSHOT.jar stock-analyzer-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /stock-analyzer-0.0.1-SNAPSHOT.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /stock-analyzer-0.0.1-SNAPSHOT.jar]