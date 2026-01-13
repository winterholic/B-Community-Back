# BJT Gallery Backend

익명 커뮤니티 플랫폼 - Spring Boot 백엔드

## 기술 스택

- Java 17
- Spring Boot 3.3.4
- Spring Data JPA + MyBatis
- MariaDB
- Spring Security
- Swagger (SpringDoc OpenAPI)
- Docker

## 시작하기

### 로컬 개발 환경

1. `application.yml` 파일 생성
```bash
cd src/main/resources
cp application.yml.template application.yml
# application.yml 파일을 실제 환경에 맞게 수정
```

2. 애플리케이션 실행
```bash
./gradlew bootRun
# Windows: gradlew.bat bootRun
```

3. API 문서 확인
- Swagger UI: http://localhost:9003/swagger-ui/index.html
- API Docs: http://localhost:9003/v3/api-docs

## API 엔드포인트

### 게시글 관련
- `POST /api/v1/posts` - 게시글 작성
- `GET /api/v1/posts?sortType=latest&page=0&size=15` - 게시글 목록
- `GET /api/v1/posts/search?keyword={keyword}` - 게시글 검색
- `GET /api/v1/posts/{postId}` - 게시글 상세
- `PATCH /api/v1/posts/{postId}/view` - 조회수 증가
- `PATCH /api/v1/posts/{postId}/upvote` - 추천
- `PATCH /api/v1/posts/{postId}/downvote` - 비추천
- `PATCH /api/v1/posts/{postId}/report` - 신고

### 댓글 관련
- `GET /api/v1/posts/{postId}/comments` - 댓글 목록 (트리 구조 + 인기 Top 3)
- `POST /api/v1/comments` - 댓글 작성
- `POST /api/v1/comments/reply` - 답글 작성
- `PATCH /api/v1/comments/{commentId}/upvote` - 추천
- `PATCH /api/v1/comments/{commentId}/downvote` - 비추천
- `PATCH /api/v1/comments/{commentId}/report` - 신고

## 배포

자세한 배포 가이드는 [DEPLOYMENT.md](DEPLOYMENT.md)를 참고하세요.

`main` 브랜치에 push하면 GitHub Actions를 통해 자동으로 배포됩니다.

### GitHub Secrets 설정 필요
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `SERVER_HOST`
- `SERVER_USER`
- `SERVER_SSH_KEY`
- `SERVER_SSH_PORT`
- `IP_HASH_SALT`

## 주요 기능

- 익명 게시글 작성 (IP 해시 기반)
- 무한 댓글 답글
- 추천/비추천 기능
- 신고 기능 (10회 이상시 자동 삭제)
- MyBatis를 통한 N+1 문제 방지
- Swagger API 문서화

## 배포 정보

- 포트: 9003
- 컨테이너명: bjt-gallery-backend
- 배포 경로: /home/winterholic/projects/services/bjt-gallery
