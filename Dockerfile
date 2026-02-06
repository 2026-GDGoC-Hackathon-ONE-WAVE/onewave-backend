# 1. 빌드 스테이지 (최종 이미지 크기를 줄이기 위해)
FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 2. 실행
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar /app.jar"]
