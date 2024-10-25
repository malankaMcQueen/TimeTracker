# Стадия сборки
FROM maven:3-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Копируем файл pom.xml и загружаем зависимости
COPY pom.xml .
RUN mvn clean verify --fail-never -DskipTests

# Копируем исходный код и создаем сборку
COPY src ./src
RUN mvn package -DskipTests

# Стадия запуска
FROM eclipse-temurin:17-alpine

WORKDIR /app

# Копируем собранный JAR-файл
COPY --from=build /app/target/*.jar application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application.jar"]

