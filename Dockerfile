FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 운영 환경 프로파일로 실행
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
