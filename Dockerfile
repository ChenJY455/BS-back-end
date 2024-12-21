# 第一阶段：使用 Maven 构建 JAR 包
FROM maven AS builder

# 设置工作目录
WORKDIR /app

# 将 Maven 的配置文件复制到镜像中
COPY pom.xml ./
COPY src ./src

# 执行 Maven 打包命令
RUN mvn clean package

# 第二阶段：运行 Spring Boot 应用
FROM openjdk:17

# 设置工作目录
WORKDIR /app

# 从构建阶段复制生成的 JAR 包到运行环境
COPY --from=builder /app/target/purweb-1.0-SNAPSHOT.jar app.jar
#COPY target/purweb-1.0-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties application.properties

# 暴露端口 15455
EXPOSE 15455

# 运行 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]