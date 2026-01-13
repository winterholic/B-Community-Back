# BJT Gallery Backend 배포 가이드

## GitHub Secrets 설정

**b-gallery-back** 레포지토리의 Settings > Secrets and variables > Actions에서 다음 Secrets를 추가하세요:

### 데이터베이스 설정
- `DB_URL`: `jdbc:mariadb://192.168.0.3:3306/bjt_gallery?useUnicode=true&characterEncoding=utf8`
- `DB_USERNAME`: 데이터베이스 사용자명 (예: `bjtuser`)
- `DB_PASSWORD`: 데이터베이스 비밀번호

### 서버 접속 정보
- `SERVER_HOST`: 배포 서버 IP 또는 도메인
- `SERVER_USER`: `winterholic`
- `SERVER_SSH_KEY`: SSH private key (전체 내용 복사)
- `SERVER_SSH_PORT`: SSH 포트 번호 (예: `2222`)

### 애플리케이션 설정
- `IP_HASH_SALT`: IP 해시용 솔트 값 (예: `_2026_BJT_QWER`)

## 배포 프로세스

1. `main` 브랜치에 코드를 push하면 자동으로 배포가 시작됩니다.
2. GitHub Actions가 다음 작업을 수행합니다:
   - 코드 체크아웃
   - JDK 17 설정
   - application-prod.yml 생성 (Secrets 값으로)
   - Gradle 빌드
   - Docker 이미지 생성
   - 서버로 이미지 전송 (custom SSH port)
   - 기존 컨테이너 중지 및 삭제
   - 새 컨테이너 실행
   - Docker 캐시 정리

## 서버 사전 준비사항

배포 서버에 다음이 설치되어 있어야 합니다:
- Docker
- SSH 접속 허용 (custom port)

## 배포 확인

배포 완료 후 다음 URL로 접속하여 확인:
- API: `http://{SERVER_HOST}:9003/api/v1/posts`
- Swagger: `http://{SERVER_HOST}:9003/swagger-ui/index.html`
- Health Check: `http://{SERVER_HOST}:9003/api/v1/health`

## 로그 확인

서버에 SSH 접속 후:
```bash
# 컨테이너 로그 확인
docker logs -f bjt-gallery-backend

# 컨테이너 상태 확인
docker ps | grep bjt-gallery-backend
```

## 수동 재시작

```bash
cd /home/winterholic/projects/services/bjt-gallery
docker restart bjt-gallery-backend
```

## 문제 해결

컨테이너가 실행되지 않는 경우:
```bash
# 로그 확인
docker logs bjt-gallery-backend

# 컨테이너 중지 및 삭제
docker stop bjt-gallery-backend
docker rm bjt-gallery-backend

# 이미지 확인
docker images | grep bjt-gallery-backend

# 수동 실행
docker run -d \
  --name bjt-gallery-backend \
  --restart unless-stopped \
  -p 9003:9003 \
  bjt-gallery-backend:latest
```
