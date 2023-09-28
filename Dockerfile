FROM openjdk:17

RUN mkdir /var/rinha
WORKDIR /var/rinha

COPY jar/rinhalang.jar ./
ENTRYPOINT ["java", "-jar", "rinhalang.jar"]