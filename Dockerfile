# Start with a base image containing Java runtime (Eclipse Temurin is a good choice for Java 17)
FROM eclipse-temurin:17-jre

# Add Maintainer Info
LABEL maintainer="bashirabdi1994@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/employee_service-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
COPY ${JAR_FILE} employee_service.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/employee_service.jar"]
