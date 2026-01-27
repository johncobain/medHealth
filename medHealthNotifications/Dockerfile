FROM maven:3.9-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

EXPOSE 8082

ENTRYPOINT ["mvn", "spring-boot:run", "-Dspring-boot.run.fork=false"]