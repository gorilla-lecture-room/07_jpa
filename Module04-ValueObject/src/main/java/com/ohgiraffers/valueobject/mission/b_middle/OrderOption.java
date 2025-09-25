package com.ohgiraffers.valueobject.mission.b_middle;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class OrderOption {

    private String optionName;
    private int price;

    protected OrderOption() {}

    public OrderOption(String optionName, int price) {
        this.optionName = optionName;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    // 값 객체의 동등성 비교를 위해 equals와 hashCode 재정의
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderOption that = (OrderOption) o;
        return price == that.price && Objects.equals(optionName, that.optionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optionName, price);
    }
}