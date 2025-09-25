### 🥈 중급 미션 : 주문 옵션(`OrderOption`) 값 컬렉션 만들기

**목표:** 단순 타입이 아닌, 여러 속성을 가진 값 객체(`@Embeddable`)의 컬렉션을 `@ElementCollection`으로 매핑하는 실전적인 능력을 기릅니다. 이를 통해 엔티티의 비즈니스 표현력을 풍부하게 만드는 방법을 익힙니다.

**상황:** 쇼핑몰에서 주문(`Order`) 시, "선물 포장", "빠른 배송"과 같은 추가 '옵션'을 선택할 수 있습니다. 각 옵션은 이름과 추가 비용을 가집니다.

**요구사항:**
1.  `OrderOption` 이라는 이름의 값 객체 클래스를 만드세요. (`@Embeddable` 사용)
    * `optionName`: `String` 타입 (옵션 이름, 예: "선물 포장")
    * `price`: `int` 타입 (추가 비용)
    * **핵심:** `OrderOption`은 불변 객체로 설계해야 합니다. (setter를 만들지 마세요)
2.  `Order` 엔티티를 생성하세요. (`id`, `orderName` 필드 포함)
3.  `Order` 엔티티 안에 `List<OrderOption> options` 필드를 추가하고, `@ElementCollection`을 사용하여 매핑하세요.
    * 생성될 테이블 이름은 `order_options`로 지정합니다.
4.  `Order` 엔티티에 `addOption(OrderOption option)`과 `getTotalOptionPrice()`라는 **'행위(Behavior)'**를 추가하세요.
    * `addOption`: 주문에 옵션을 추가하는 책임.
    * `getTotalOptionPrice`: 모든 옵션의 가격을 합산하여 반환하는 책임. (주문 총액 계산 로직의 일부)
5.  `Application`에서 `Order`를 생성하고, 여러 `OrderOption`을 추가한 뒤, `getTotalOptionPrice()` 메서드를 호출하여 총 옵션 가격이 정확한지 확인 후 영속화하세요. DB 테이블에 데이터가 올바르게 저장되었는지도 확인합니다.
