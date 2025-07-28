# Use JDK 17 or your version
FROM eclipse-temurin:17-jdk

# Set work directory inside the container
WORKDIR /app

# Copy the JAR file (update the filename as needed)
COPY build/libs/e-commerce-app-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]