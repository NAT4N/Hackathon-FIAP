# ---------- Build stage ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Cache de dependências: copia wrapper + pom primeiro
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Compila e empacota (pula testes: exigem Docker/Testcontainers)
COPY src/ src/
COPY lombok.config ./
RUN ./mvnw -B clean package -Dmaven.test.skip=true

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Usuário não-root
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
