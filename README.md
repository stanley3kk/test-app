# Getting Started

## Docker Compose 실행 방법 (How to Run with Docker Compose)

이 애플리케이션은 Docker Compose를 사용하여 쉽게 실행할 수 있습니다. 다음 단계를 따라 실행하세요:

1. 프로젝트 루트 디렉토리에서 다음 명령어를 실행합니다:
   ```bash
   docker-compose up -d
   ```

2. 애플리케이션이 시작되면 다음 URL에서 접근할 수 있습니다:
   - 애플리케이션: http://localhost:8080
   - MySQL 데이터베이스: localhost:3306
   - MongoDB: localhost:27017

3. 애플리케이션을 중지하려면 다음 명령어를 실행합니다:
   ```bash
   docker-compose down
   ```

4. 데이터베이스 볼륨을 포함하여 모든 리소스를 제거하려면 다음 명령어를 실행합니다:
   ```bash
   docker-compose down -v
   ```

## 코드 명명 규칙 (Code Naming Conventions)

이 프로젝트는 Controller, Service, Repository의 파일명과 함수명에 대한 명확한 규칙을 따릅니다. 이 규칙은 개발 및 확장이 용이하고, 장애 발생 시 직관적으로 어느 부분에 문제가 있는지 파악할 수 있도록 설계되었습니다.

### Controller 명명 규칙

#### 파일명 규칙
- 파일명은 `[도메인/기능명]Controller.kt` 형식을 따릅니다.
  - 예: `PersonController.kt`, `AuthenticationController.kt`
- 특정 도메인에 속하지 않는 일반적인 기능의 경우 기능을 명확히 표현합니다.
  - 예: `HealthCheckController.kt`, `StatusController.kt`

#### 함수명 규칙
- REST API 동작을 명확히 표현하는 동사-명사 형태를 사용합니다.
  - 생성: `create[도메인]` (예: `createPerson`)
  - 조회(전체): `getAll[도메인(복수형)]` (예: `getAllPersons`)
  - 조회(단일): `get[도메인]By[식별자]` (예: `getPersonById`)
  - 수정: `update[도메인]` (예: `updatePerson`)
  - 삭제: `delete[도메인]` (예: `deletePerson`)
- 특수 기능의 경우 목적을 명확히 표현하는 이름을 사용합니다.
  - 예: `activateAccount`, `resetPassword`

### Service 명명 규칙

#### 파일명 규칙
- 파일명은 `[도메인/기능명]Service.kt` 형식을 따릅니다.
  - 예: `PersonService.kt`, `AuthenticationService.kt`
- 여러 도메인에 걸친 비즈니스 로직의 경우 주요 기능을 표현합니다.
  - 예: `NotificationService.kt`, `AnalyticsService.kt`

#### 함수명 규칙
- 비즈니스 로직을 명확히 표현하는 동사-명사 형태를 사용합니다.
  - 예: `processPurchase`, `validateUserCredentials`
- Controller와 직접 매핑되는 함수는 Controller 함수명과 일관성을 유지합니다.
  - 예: Controller의 `createPerson`에 대응하는 `createPerson`
- 내부 로직 함수는 기능을 구체적으로 설명합니다.
  - 예: `calculateDiscount`, `generateUniqueToken`

### Repository 명명 규칙

#### 파일명 규칙
- 파일명은 `[엔티티명]Repository.kt` 형식을 따릅니다.
  - 예: `PersonRepository.kt`, `ProductRepository.kt`

#### 함수명 규칙
- Spring Data JPA 쿼리 메소드 명명 규칙을 따릅니다.
  - 조회: `findBy[속성명]` (예: `findByName`)
  - 존재 확인: `existsBy[속성명]` (예: `existsByEmail`)
  - 개수 조회: `countBy[속성명]` (예: `countByStatus`)
- 복합 조건의 경우 조건을 명확히 표현합니다.
  - 예: `findByNameAndAgeGreaterThan`
- 커스텀 쿼리의 경우 목적을 명확히 표현합니다.
  - 예: `findActiveUsersInLastMonth`

## 장애 대응을 위한 명명 규칙 활용

위 명명 규칙을 따르면 다음과 같은 장점이 있습니다:

1. **문제 영역 빠른 식별**: 로그에서 오류가 발생한 클래스나 함수명을 보고 즉시 어떤 도메인과 기능에서 문제가 발생했는지 파악할 수 있습니다.
2. **책임 경계 명확화**: 각 계층(Controller, Service, Repository)의 역할과 책임이 명확히 구분되어, 문제 발생 시 해당 계층에서 집중적으로 조사할 수 있습니다.
3. **코드 추적 용이성**: 일관된 명명 규칙으로 인해 관련 코드를 쉽게 찾고 추적할 수 있습니다.

예를 들어, `PersonController.updatePerson` 메소드에서 오류가 발생했다면:
1. Person 도메인의 업데이트 기능에 문제가 있음을 즉시 알 수 있습니다.
2. Controller 계층의 문제인지, 호출하는 Service나 Repository 계층의 문제인지 파악할 수 있습니다.
3. 관련된 `PersonService.updatePerson`이나 `PersonRepository` 메소드를 쉽게 찾아 조사할 수 있습니다.
