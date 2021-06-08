FROM adoptopenjdk/openjdk11

VOLUME /tmp

COPY UnlimitedJCEPolicyJDK8/* /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/ 

COPY target/PhotoAppApiConfigServer-0.0.1-SNAPSHOT.jar ConfigServer.jar

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","ConfigServer.jar"] 

