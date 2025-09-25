### 🥉 기초 미션: `SINGLE_TABLE` 전략으로 결제 수단 구현하기

**목표:** `SINGLE_TABLE` 전략의 장단점을 이해하고, 간단한 상속 구조에 적용할 수 있다.

**상황:** 온라인 쇼핑몰의 결제(`Payment`) 시스템을 설계합니다. 결제 수단에는 '카드 결제(`CardPayment`)'와 '계좌 이체(`BankTransfer`)'가 있습니다. 둘 다 '금액(`amount`)'이라는 공통 속성을 가집니다.

**요구사항:**
1. `Payment` 추상 클래스를 만들고 `id`, `amount` 필드를 정의하세요.
2. `CardPayment`는 `cardNumber`를, `BankTransfer`는 `bankName`을 고유 속성으로 가집니다.
3. 모든 결제 정보를 하나의 `PAYMENTS` 테이블에 저장하도록 `SINGLE_TABLE` 전략을 사용하세요.
4. `Application`에서 카드 결제와 계좌 이체를 각각 생성하여 저장하고, DB 테이블에 `DTYPE` 컬럼과 함께 데이터가 어떻게 저장되는지 확인하세요.
