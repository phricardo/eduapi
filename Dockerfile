FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests && cp target/*.jar app.jar

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=build /app/app.jar /app/app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
