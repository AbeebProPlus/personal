FROM maven:3.8.7 as build
COPY . .
RUN mvn -B clean package -DskipTests

FROM openjdk:17
COPY --from=build web/target/*.jar learnspaceplatform.jar
ENTRYPOINT ["java", "-jar", "-Dserver.port=${PORT}", "-Dspring.profiles.active=${PROFILE}","learnspaceplatform.jar"]
