FROM java:8
VOLUME /tmp
ADD target/pna-web-0.0.1-SNAPSHOT.jar pna.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/pna.jar","--spring.profiles.active=prod"]