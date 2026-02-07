# imagem base para build com Maven
FROM maven:3.9.12-eclipse-temurin-21-alpine AS build

# define a pasta de trabalho dentro do container
WORKDIR /app

# copio o pom para o /app uma unica vez
COPY pom.xml .

# copia o código-fonte da aplicação
COPY src ./src

# executar o maven no container
RUN mvn clean package -DskipTests

# inicia a imagem final apenas com Java
FROM eclipse-temurin:21-alpine

# define a pasta de trabalho da imagem final
WORKDIR /app

# copia o JAR gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# comando executado ao iniciar o container
CMD ["java", "-jar", "app.jar"]
