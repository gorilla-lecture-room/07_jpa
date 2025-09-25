package com.ohgiraffers.chap01.section03;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;


/*
 * 🏆 값 객체(Value Object): 흩어진 데이터를 '하나의 개념'으로 묶다
 *
 * 💡 문제 상황:
 * `Product` 엔티티에 가격을 저장하기 위해 `@Column private BigDecimal amount;`와
 * `@Column private String currency;` 두 필드를 따로 둘 수도 있다.
 * 하지만 이렇게 하면 '가격'이라는 비즈니스 개념이 코드에 드러나지 않고, 두 필드는 그저 관련 없는 데이터 조각처럼 보인다.
 * 가격 관련 로직(예: 통화 변환, 가격 비교)이 있다면 `Product` 엔티티나 서비스 계층에 흩어져 캡슐화가 깨질 것이다.
 *
 * 🤔 해결 아이디어:
 * "가격(amount)과 통화(currency)는 항상 함께 움직이는 하나의 '개념'이 아닐까?"
 * -> 이 아이디어를 코드로 구현한 것이 바로 `Money` 같은 값 객체이다.
 *
 * 💡 @Embeddable의 본질
 * - `@Embeddable`은 "이 클래스는 독립적인 실체(Entity)가 아니라, 다른 엔티티에 포함되어 특정 개념을 나타내는 값의 묶음이다." 라는 선언이다.
 * - 이를 통해 `Product`는 `Money`라는 '개념'을 소유하게 되어, 객체 모델이 훨씬 풍부하고 명확해진다.
 */
@Embeddable
public class Money {

    @Column(name = "price_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    // BigDecimal 고정 소수점 및 부동 소수점 수치를 정확하게 표현하고 계산하기 위해 사용

    @Column(name = "price_currency", length = 10)
    private String currency; // 통화

    // 📌 JPA 스펙에 따른 protected 기본 생성자 (리플렉션용)
    protected Money() {}

    // 📌 모든 필드를 초기화하는 생성자만으로 설정 (setter 없음)
    public Money(BigDecimal amount, String currency) {
        // .isBlank() : 문자열이 비어 있거나 공백으로만 구성되어 있는지를 확인
        if (amount == null || currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("금액과 통화 정보는 필수입니다.");
        }
        this.amount = amount;
        this.currency = currency;
    }

    // 📌 Getter 제공 (setter는 제공하지 않음)
    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }


    public Money add(Money other) {
        validateSameCurrency(other);
        //  두 금액을 더 한다.
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        //  현재 객체의 금액에서 다른 객체의 금액을 뺀다.
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    // 🎯 통화가 같은지 확인 (비즈니스 도메인 관점 중요)
    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("통화가 다릅니다: " + this.currency + " vs " + other.currency);
        }
    }


    // 📌 VO는 equals/hashCode 재정의가 핵심!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        // compareTo 메서드는 두 값의 크기를 비교하여, 현재 객체가 더 크면 1, 같으면 0, 더 작으면 -1을 반환
        return amount.compareTo(money.amount) == 0 &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

}
