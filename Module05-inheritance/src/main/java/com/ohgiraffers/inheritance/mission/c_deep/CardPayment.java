package com.ohgiraffers.inheritance.mission.c_deep;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CARD")
public class CardPayment extends Payment {
    @Column(nullable = false)
    private String cardCompany;
    @Column(nullable = false)
    private String cardType;
    private String cardNumber;

    protected CardPayment() {}
    public CardPayment(int amount, String cardNumber, String cardCompany, String cardType) {
        super(amount);
        this.cardNumber = cardNumber;
        this.cardCompany = cardCompany;
        this.cardType = cardType;
    }
}