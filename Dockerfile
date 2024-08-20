FROM amazoncorretto:21-alpine

WORKDIR /app

COPY rest/target/rest-0.0.1-SNAPSHOT.jar /app/hotel.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/hotel.jar"]