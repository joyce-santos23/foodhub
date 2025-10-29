# ================= STAGE 1: Build =================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean install -DskipTests

# ================= STAGE 2: Runtime =================
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho
WORKDIR /app

# Argumento para o nome do JAR
ARG JAR_FILE=target/*.jar

# Copia o JAR gerado no Stage 1
COPY --from=build /app/${JAR_FILE} app.jar

# Expõe a porta que a aplicação Spring Boot usa
EXPOSE 8080

# Ponto de entrada da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]


