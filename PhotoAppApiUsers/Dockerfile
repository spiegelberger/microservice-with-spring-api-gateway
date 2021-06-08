FROM adoptopenjdk/openjdk11

VOLUME /tmp

COPY target/PhotoAppApiUsers-0.0.1-SNAPSHOT.jar users-ms.jar

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","users-ms.jar"] 
