# Use Java 11
FROM eclipse-temurin:11-jdk-alpine AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Make mvnw executable
RUN chmod +x ./mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:11-jre-alpine

WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 10000

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
