### 🥈 중급 미션: 주소(Address) 값 객체 만들기

**목표:** `@Embeddable`과 `@Embedded`를 사용하여 흩어진 주소 정보를 하나의 '개념'으로 묶고, 엔티티에 재사용 가능한 값으로 포함시키는 방법을 익힙니다.

**요구사항:**
1.  `Address`라는 이름의 값 객체 클래스를 만드세요. (`@Embeddable` 사용)
2.  `Address`는 다음 필드를 가집니다.
    * `zipcode`: `String` 타입, 우편번호.
    * `address1`: `String` 타입, 기본 주소.
    * `address2`: `String` 타입, 상세 주소.
3.  기초 미션에서 만든 `Post` 엔티티를 수정하거나, `section02.User` 같은 새로운 엔티티를 만들어 `Address`를 필드로 포함시키세요. (`@Embedded` 사용)
4.  `Application` 클래스에서 `Address`를 포함한 엔티티를 생성하고 저장하여, 주소 관련 컬럼들이 해당 엔티티의 테이블에 잘 생성되는지 확인하세요.
