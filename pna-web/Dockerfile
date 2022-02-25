FROM java:8
VOLUME /tmp
ADD pna-web-0.0.1.jar pna.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/blog.jar","--spring.profiles.active=prod"]