### 🥉 기초 미션: `FullName` 값 객체 만들기

**목표:** 가장 기본적인 `@Embeddable` 값 객체를 만들고 엔티티에 `@Embedded`로 포함시켜, 데이터의 응집력을 높이는 방법을 익힙니다.

**요구사항:**
1. `FullName`이라는 이름의 값 객체 클래스를 만드세요. (`@Embeddable` 사용)
2. `FullName`은 다음 두 필드를 가져야 합니다.
    * `firstName`: `String` 타입 (이름)
    * `lastName`: `String` 타입 (성)
3. `User`라는 새로운 엔티티를 만들고, `username` 필드 대신 `FullName` 타입의 `fullName` 필드를 포함시키세요. (`@Embedded` 사용)
4. `Application`에서 `User` 객체를 생성하고 저장한 뒤, `User` 테이블에 `first_name`과 `last_name` 컬럼이 잘 생성되었는지 확인하세요.
