# Use the official OpenJDK base image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/SocialApp-0.0.1-SNAPSHOT.jar /app/SocialApp-0.0.1-SNAPSHOT.jar
COPY src/main/resources/META_INF/persistence.xml /app/META-INF/persistence.xml

# Command to run the application
CMD ["java", "-jar", "SocialApp-0.0.1-SNAPSHOT.jar"]
