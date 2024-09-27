# Usa una imagen base de Java
FROM openjdk:11-jre-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR al contenedor
COPY build/libs/*.jar app.jar

# Especifica el comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]