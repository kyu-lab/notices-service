# 알림 서비스
알림 서비스를 담당합니다.  
webflux + SSE로 사용자에게 알림을 전송하고 mongodb에 알림을 저장합니다.  
팔로우한 대상이 글 작성시 카프카를 통해 알림을 전송받습니다.

## 실행 환경
- openJdk 17
- mongodb
- webFlux
- kafka

# 설정 파일
## application-prod
운영환경에서 사용되는 설정 파일

## application-dev
로컬환경에서 사용되는 설정 파일로 커밋 금지
