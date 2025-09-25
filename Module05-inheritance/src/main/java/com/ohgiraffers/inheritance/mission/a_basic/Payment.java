package com.ohgiraffers.inheritance.mission.a_basic;

import jakarta.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type")
public abstract class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amount;

    protected Payment() {}
    public Payment(int amount) { this.amount = amount; }
}