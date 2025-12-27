FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Instala Maven manualmente
RUN apt-get update && apt-get install -y maven

# Copia todo el código al contenedor
COPY . .

# Compila el proyecto
RUN mvn clean package -DskipTests

# Expone el puerto
EXPOSE 8080

# Ejecuta el jar generado
CMD ["java", "-jar", "target/gimnasio-web-0.0.1-SNAPSHOT.jar"]
