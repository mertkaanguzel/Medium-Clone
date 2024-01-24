FROM openjdk:21-jdk-slim as build-env

WORKDIR /usr/src/myapp

COPY pom.xml mvnw ./
RUN chmod +x ./mvnw
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src

RUN apt-get update && apt-get -y install openssl && \
mkdir ./src/main/resources/certs && cd ./src/main/resources/certs && \
openssl genrsa -out keypair.pem 2048 && \
openssl rsa -in keypair.pem -pubout -out public.pem && \
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt \
-in keypair.pem -out private.pem && rm keypair.pem

RUN ./mvnw package

FROM eclipse-temurin:21.0.1_12-jre

WORKDIR /

COPY --from=build-env /usr/src/myapp/target/medium-clone.jar .

EXPOSE 8080

ENTRYPOINT [ "java" ]

CMD [ "-jar", "./medium-clone.jar" ]
