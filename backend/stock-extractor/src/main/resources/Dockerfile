FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD stock-extractor-0.0.1-SNAPSHOT.jar stock-extractor-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /stock-extractor-0.0.1-SNAPSHOT.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /stock-extractor-0.0.1-SNAPSHOT.jar" ]