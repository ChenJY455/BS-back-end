# 第一阶段：使用 Maven 构建 JAR 包
FROM maven AS builder
WORKDIR /build

COPY pom.xml ./
COPY src ./src

RUN mvn clean package

FROM openjdk:17
WORKDIR /app

COPY --from=builder /build/target/purweb-1.0-SNAPSHOT.jar app.jar

EXPOSE 15455
ENTRYPOINT ["java", "-jar", "/app/app.jar"]