package com.ohgiraffers.inheritance.mission.a_basic;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BANK")
public class BankTransfer extends Payment {
    private String bankName;
    protected BankTransfer() {}
    public BankTransfer(int amount, String bankName) {
        super(amount);
        this.bankName = bankName;
    }
}