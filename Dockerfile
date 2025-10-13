# Usa una imagen ligera de Java 17
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el código fuente al contenedor
COPY . .

# Compila el proyecto con Maven (usa el wrapper si existe)
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Expone el puerto que usará tu app
EXPOSE 8080

# Ejecuta el jar
CMD ["java", "-jar", "target/energia_y_salud_web-0.0.1-SNAPSHOT.jar"]
