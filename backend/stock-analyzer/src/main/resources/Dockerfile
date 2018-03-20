FROM openjdk:8-jdk-alpine
VOLUME /stock
ADD stock-analyzer-0.0.1-SNAPSHOT.jar stock-analyzer-0.0.1-SNAPSHOT.jar
ADD stock-analyzer-0.0.1-SNAPSHOT.jar.original stock-analyzer-0.0.1-SNAPSHOT.jar.original
RUN sh -c 'touch /stock-analyzer-0.0.1-SNAPSHOT.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar  -Dspring.profiles.active=prod /stock-analyzer-0.0.1-SNAPSHOT.jar" ]