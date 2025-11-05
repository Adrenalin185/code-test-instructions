FROM maven:3.9.9-amazoncorretto-21 As builder
WORKDIR /app

COPY /url-service/pom.xml .

RUN mvn dependency:go-offline -B

COPY /url-service/src ./src
RUN mvn clean package

FROM amazoncorretto:21-alpine
WORKDIR /url-service

COPY --from=builder /app/target/*.jar url-service.jar

EXPOSE 8080

CMD ["java", "-jar", "url-service.jar"]
