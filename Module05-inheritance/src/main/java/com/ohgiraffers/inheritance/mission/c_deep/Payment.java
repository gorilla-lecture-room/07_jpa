package com.ohgiraffers.inheritance.mission.c_deep;

import jakarta.persistence.*;

/*
 * [결론] 왜 JOINED 전략이 더 나은 선택인가?
 * 1. 데이터 무결성: 각 자식 테이블(CardPayment, BankTransfer)의 고유 속성에 NOT NULL 제약조건을 걸 수 있어 데이터의 정합성을 보장합니다.
 * 2. 확장성: 앞으로 새로운 결제수단이 추가되더라도 기존 PAYMENT 테이블의 구조를 변경할 필요 없이 새로운 자식 테이블만 추가하면 되므로 확장성이 뛰어납니다.
 * 3. 정규화: 데이터 중복이 없고 각 테이블의 책임이 명확하여 더 나은 데이터베이스 설계입니다.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED) // SINGLE_TABLE에서 JOINED로 리팩토링
@DiscriminatorColumn(name = "payment_type")
public abstract class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amount;

    // 왜 SINGLE_TABLE이 새로운 요구사항에 부적합한가?
    // 1. NULL 값 문제: SINGLE_TABLE에서는 CardPayment에만 필요한 cardCompany, cardType 컬럼이
    //    BankTransfer 로우에서는 항상 NULL이 됩니다. 자식 타입이 늘어날수록 테이블에 NULL 값이 급증하게 됩니다.
    // 2. NOT NULL 제약조건 불가: cardCompany는 카드 결제 시 필수값이지만, SINGLE_TABLE에서는 이 컬럼에
    //    NOT NULL 제약조건을 걸 수 없습니다. BankTransfer 같은 다른 결제수단은 이 값을 가질 수 없기 때문입니다.

    protected Payment() {}
    public Payment(int amount) { this.amount = amount; }
}