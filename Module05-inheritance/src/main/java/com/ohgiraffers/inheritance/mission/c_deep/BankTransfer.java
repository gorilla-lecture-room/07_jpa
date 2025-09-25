package com.ohgiraffers.inheritance.mission.c_deep;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BANK")
public class BankTransfer extends Payment {
    @Column(nullable = false)
    private String accountNumber;
    private String bankName;
    protected BankTransfer() {}
    public BankTransfer(int amount, String bankName, String accountNumber) {
        super(amount);
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }
}