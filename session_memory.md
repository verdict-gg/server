## 2025년 7월 18일 작업 기록

### 1. MySQL 연결 문제 해결
- **초기 문제**: `Communications link failure` 에러 발생.
- **원인 분석**: `application-local.yml` 및 `envs.env` 파일의 `datasource.url` 설정 오류.
- **해결 과정**:
    - `envs.env` 파일 내 `LOCAL_DB_URL`을 `jdbc:mysql://verdict:3306/verdict?serverTimezone=Asia/Seoul`에서 `jdbc:mysql://localhost:3306/verdict?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul`로 변경. (`verdict` 호스트를 `localhost`로 변경하고 SSL 및 공개 키 검색 옵션 추가)
    - `Access denied` 에러 발생. MySQL 서버에 `root` 계정으로 접속하여 `verdict` 유저에게 `sa` 비밀번호로 모든 호스트(`%`)에서 `verdict` 데이터베이스에 대한 모든 권한을 부여함. (`CREATE USER`, `GRANT ALL PRIVILEGES`, `FLUSH PRIVILEGES` 명령어 사용)

### 2. Redis 연결 문제 해결
- **초기 문제**: `Redis health check failed` 에러 발생.
- **원인 분석**: `envs.env` 파일에 `LOCAL_REDIS_PASSWORD=sa`로 비밀번호가 설정되어 있었으나, Redis Docker 컨테이너는 비밀번호 없이 실행 중이었음.
- **해결 과정**:
    - `envs.env` 파일에서 `LOCAL_REDIS_PASSWORD=sa` 라인을 제거하여 스프링 부트 애플리케이션이 비밀번호 없이 Redis에 접속하도록 설정.

### 3. OAuth2 로그인 구현 시작
- **진행 상황**:
    - `SecurityConfig.java`, `AuthController.java`, `build.gradle` 파일 확인 결과, OAuth2 관련 의존성 및 기본 설정은 되어 있음.
    - `CustomOAuth2UserService`, `OAuth2SuccessHandler`, `OAuth2FailureHandler` 파일들이 OAuth2 로그인 흐름의 핵심 역할을 하는 것으로 파악됨.
    - `OAuth2SuccessHandler`에서 JWT 토큰 생성 로직이 누락되어 있음을 확인. `AuthController`의 토큰 생성 로직을 참조하여 `OAuth2SuccessHandler`에 적용할 예정.
    - **`OAuth2SuccessHandler.java` 수정 완료:** `JwtFacade`와 `UserFacade`를 주입하고, `onAuthenticationSuccess` 메서드에서 토큰을 생성하고 응답에 담는 로직을 추가함.

### 4. 프로젝트 시나리오 및 기능 설명

이 프로젝트는 `verdict.gg`라는 도메인 이름과 코드 구조를 봤을 때, 사용자들이 특정 주제나 콘텐츠에 대해 '판결' 또는 '의견'을 공유하고 상호작용하는 플랫폼으로 보인다. 특히 `LOL` (리그 오브 레전드) 관련 OAuth2 제공자 정보가 있는 걸로 봐서, 게임 관련 콘텐츠에 특화된 커뮤니티 플랫폼일 가능성이 높다.

**주요 기능:**

1.  **사용자 관리 및 인증/인가:**
    *   **회원가입/로그인:** 일반적인 회원가입 및 로그인 기능을 제공한다.
    *   **OAuth2 소셜 로그인:** Google, Naver를 통한 소셜 로그인을 지원하며, `LOL` 계정 연동도 계획 중인 것으로 보인다.
    *   **게스트 로그인:** 임시 사용자(게스트)로 로그인하여 일부 기능을 사용할 수 있다.
    *   **JWT 기반 인증:** Access Token과 Refresh Token을 사용하여 사용자 세션을 관리하고 보안을 유지한다. Refresh Token은 Redis에 저장하여 관리한다.
    *   **역할 기반 접근 제어 (RBAC):** `NOT_REGISTERED`, `USER`, `ADMIN` 세 가지 사용자 역할을 통해 기능 접근 권한을 제어한다.
    *   **닉네임 중복 확인:** 회원가입 시 닉네임 중복을 검사한다.

2.  **콘텐츠 (게시글/댓글) 관리:**
    *   **게시글 (Post):** 사용자가 게시글을 작성하고 조회할 수 있다. (현재는 예시 엔드포인트만 확인됨)
    *   **댓글 (Comment)::** 게시글에 댓글을 작성하고 조회할 수 있다. (현재는 엔티티와 레포지토리만 확인됨)

3.  **첨부파일 (이미지/영상) 관리:**
    *   **S3 연동:** AWS S3를 이용하여 이미지 및 영상 파일을 저장하고 관리한다.
    *   **이미지 업로드:** 단일 이미지 파일을 S3에 업로드한다.
    *   **영상 멀티파트 업로드:** 대용량 영상 파일의 효율적인 업로드를 위해 멀티파트 업로드 기능을 제공한다. (초기화, 파트 업로드, 완료, 중단 기능 포함)
    *   **파일 삭제:** S3에 저장된 이미지 및 영상 파일을 삭제한다.

4.  **투표 시스템 (Vote):**
    *   사용자들이 게시글에 대한 투표를 진행하고, 투표 옵션을 관리할 수 있다. (현재는 엔티티와 레포지토리만 확인됨)

5.  **시스템 유틸리티 및 예외 처리:**
    *   **헬스 체크:** 애플리케이션의 상태를 확인할 수 있는 헬스 체크 엔드포인트를 제공한다.
    *   **전역 예외 처리:** `GlobalExceptionHandler`를 통해 다양한 HTTP 상태 코드(400, 401, 403, 404, 409, 500)에 대한 일관된 예외 응답을 제공한다.
    *   **환경 변수 관리:** `dotenv` 라이브러리를 사용하여 `.env` 파일에서 환경 변수를 로드한다.

### 시스템 아키텍처 (추정)

*   **백엔드 프레임워크:** Spring Boot (Java 17)
*   **데이터베이스:** MySQL (JPA를 통한 ORM)
*   **캐시/토큰 저장소:** Redis
*   **클라우드 스토리지:** AWS S3 (이미지 및 영상 파일)
*   **빌드 도구:** Gradle
*   **API 문서화:** Swagger (OpenAPI 3.0)
*   **컨테이너화:** Docker (개발 및 배포 환경 구성을 위한 `Dockerfile` 및 `docker-compose.yml` 존재)
*   **보안:** Spring Security, JWT, OAuth2

### 5. 네이버 OAuth2 로그인 문제 및 해결

**1. 문제 발생:**

네이버 OAuth2 로그인 시 `OAuth2SuccessHandler`에서 `java.lang.NullPointerException: Cannot invoke "Object.toString()" because the return value of "java.util.Map.get(Object)" is null` 에러가 발생했다.

**2. 문제 원인:**

*   **네이버 OAuth2 응답 구조:** 네이버 OAuth2는 사용자 정보(이메일 등)를 `response`라는 중첩된 JSON 객체 안에 담아서 반환한다.
*   **`OAuth2SuccessHandler`의 `email` 접근 방식:** 기존 코드에서는 `oAuth2User.getAttributes().get("email").toString()`과 같이 `attributes` 맵에서 직접 "email" 키를 찾아 접근했다. 하지만 네이버의 경우 "email"이 `attributes`의 최상위 레벨에 존재하지 않고 `response` 객체 안에 있었기 때문에 `null`이 반환되어 `NullPointerException`이 발생했다.
*   **`CustomOAuth2UserService`의 `User` 객체 생성:** `CustomOAuth2UserService`에서 새로운 `User` 객체를 생성할 때 `email` 필드를 `null`로 저장하고 있었다.

**3. 해결 방법:**

*   **`OAuth2UserInfo` 인터페이스 확장:**
    *   `global.oAuth.info.OAuth2UserInfo.java` 인터페이스에 `public abstract String getEmail();` 추상 메서드를 추가했다. 이는 각 OAuth2 제공자로부터 이메일 정보를 일관된 방식으로 가져오기 위함이다.
*   **`OAuth2UserInfo` 구현체 수정:**
    *   `global.oAuth.info.impl.GoogleOAuth2UserInfo.java`: `getEmail()` 메서드를 추가하여 `attributes.get("email")`을 반환하도록 수정했다.
    *   `global.oAuth.info.impl.NaverOAuth2UserInfo.java`: `getEmail()` 메서드를 추가하여 `attributes.get("email")`을 반환하도록 수정했다. (네이버의 경우 `extractResponse` 메서드에서 이미 `response` 객체 내부의 `email`을 `attributes` 맵의 최상위로 가져오도록 처리되어 있었음)
*   **`CustomOAuth2UserService` 수정:**
    *   `loadUser` 메서드 내에서 `OAuth2UserInfo` 객체로부터 `email`을 가져오도록 `String email = oAuth2UserInfo.getEmail();` 코드를 추가했다.
    *   `getUser` 메서드의 파라미터에 `String email`을 추가하고, 새로운 `User` 객체를 빌드할 때 `email(email)`을 사용하여 이메일 정보를 DB에 저장하도록 수정했다.
*   **`OAuth2SuccessHandler` 수정:**
    *   `onAuthenticationSuccess` 메서드에서 `oAuth2User.getAttributes().get("email").toString()` 부분을 제거하고, `User` 객체에서 직접 `email`을 가져오는 로직을 사용하도록 수정했다. (이메일 정보는 이미 `CustomOAuth2UserService`에서 `User` 객체에 저장했으므로, `User` 객체에서 가져오는 것이 더 안전하고 일관적이다.)

이러한 변경을 통해 `OAuth2SuccessHandler`가 `email` 정보를 안전하게 가져올 수 있게 되었고, `User` 객체에도 이메일 정보가 올바르게 저장되도록 했다.

### 6. 구글 OAuth2 로그인 문제 및 해결 시도

**1. 문제 발생:**

구글 OAuth2 로그인 시 `IllegalArgumentException: User not found with identifier: <Google_ID> and provider: GOOGLE` 에러가 발생했다.

**2. 문제 원인 (추정):**

*   `CustomOAuth2UserService`에서 사용자를 저장하는 방식과 `OAuth2SuccessHandler`에서 조회하는 방식 간의 불일치.
*   `CustomOAuth2UserService`에서 `UserPrincipal`을 반환하지만, 스프링 시큐리티 내부에서 `DefaultOidcUser`나 `DefaultOAuth2User`로 래핑되면서 `UserPrincipal`에 담긴 `User` 객체를 직접 가져오기 어려운 상황일 수 있음.

**3. 해결 시도:**

*   **`OAuth2SuccessHandler` 수정:** `authentication.getPrincipal()`의 타입을 확인하고, `UserPrincipal`이면 직접 `User` 객체를 사용하고, `OAuth2User`라면 `identifier`와 `providerInfo`를 추출해서 DB에서 `User` 객체를 조회하도록 로직을 변경했다.
*   **`CustomOAuth2UserService` 로그 추가:** `userRepository.save(unregisteredUser)` 호출 직후에 저장된 `User` 객체의 `identifier`, `email`, `providerInfo`, `userRole`을 로그로 출력하도록 수정하여 데이터 불일치 여부를 확인하려 했다.
*   **`OAuth2SuccessHandler` 로그 추가:** `userRepository.findByOAuthInfo`를 호출하기 직전에 `identifier`와 `providerInfo`를 로그로 출력하도록 수정하여 조회 시 사용되는 값을 확인하려 했다.

**4. 현재 상황:**

*   로그를 통해 `CustomOAuth2UserService`에서 저장되는 `identifier`와 `providerInfo`가 `OAuth2SuccessHandler`에서 조회하는 값과 일치하는지 확인이 필요하다.
*   애플리케이션 실행 시 `Port 8080 was already in use.` 에러가 계속 발생하여 애플리케이션이 정상적으로 시작되지 못하고 있다. 이로 인해 구글 로그인 테스트를 진행할 수 없는 상황이다.
