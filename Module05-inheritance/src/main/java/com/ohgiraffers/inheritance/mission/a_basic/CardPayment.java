package com.ohgiraffers.inheritance.mission.a_basic;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CARD")
public class CardPayment extends Payment {
    private String cardNumber;
    protected CardPayment() {}
    public CardPayment(int amount, String cardNumber) {
        super(amount);
        this.cardNumber = cardNumber;
    }
}