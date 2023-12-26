FROM openjdk:21

EXPOSE 8080

WORKDIR /usr/src/myapp

# COPY . .

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

ENTRYPOINT [ "java" ]

CMD [ "-jar", "./target/medium-clone.jar" ]