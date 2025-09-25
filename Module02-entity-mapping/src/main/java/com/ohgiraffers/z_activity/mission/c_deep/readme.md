### 🥇 고급 미션: 배송지와 청구지 분리하기

**목표:** 동일한 값 객체를 하나의 엔티티에서 여러 번 사용할 때 발생하는 컬럼명 충돌 문제를 `@AttributeOverrides`를 통해 해결하는 방법을 익힙니다. 이는 다음 모듈에서 배울 연관관계 매핑의 복잡성을 미리 맛보는 좋은 경험이 될 것입니다.

**요구사항:**
1.  `Order`(주문)라는 새로운 엔티티를 생성하세요.
2.  `Order` 엔티티 안에 중급 미션에서 만든 `Address` 값 객체를 **두 번** 포함시키세요.
    * 하나는 `shippingAddress` (배송지 주소)
    * 다른 하나는 `billingAddress` (청구지 주소)
3.  그냥 실행하면 컬럼명이 중복되어 오류가 발생할 것입니다. `@AttributeOverrides`와 `@AttributeOverride`를 사용하여 컬럼명이 충돌하지 않도록 매핑을 재정의하세요.
    * `shippingAddress`의 필드들은 `shipping_zipcode`, `shipping_addr1`, `shipping_addr2`와 같이 매핑되도록 하세요.
    * `billingAddress`의 필드들은 `billing_zipcode`, `billing_addr1`, `billing_addr2`와 같이 매핑되도록 하세요.
4.  `Application` 클래스에서 `Order` 객체를 생성하고 저장한 뒤, DB 테이블에 의도한 대로 컬럼들이 생성되었는지 확인하세요.
