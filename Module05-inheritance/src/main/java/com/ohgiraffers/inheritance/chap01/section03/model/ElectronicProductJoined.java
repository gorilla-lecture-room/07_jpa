package com.ohgiraffers.inheritance.chap01.section03.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
/*
 * 📌 자식 클래스: ElectronicProductJoined
 */
@Entity
@Table(name = "electronic_products_joined") // 💡 자식 테이블: 'electronic_products_joined'
@DiscriminatorValue("ELECTRONIC")             // 💡 DTYPE 값
public class ElectronicProductJoined extends ProductJoined {

    @Column(name = "warranty_period") // 💡 명시적 매핑
    private int warrantyPeriod;

    @Column(name = "power_consumption") // 💡 명시적 매핑
    private String powerConsumption;

    // (생성자, toString 등은 생략)
    protected ElectronicProductJoined() {}
    public ElectronicProductJoined(String name, double price, String brand, int stockQuantity, int warrantyPeriod, String powerConsumption) {
        super(name, price, brand, stockQuantity);
        this.warrantyPeriod = warrantyPeriod;
        this.powerConsumption = powerConsumption;
    }
    @Override
    public String toString() {
        return "ElectronicProductJoined{" + super.toString() + ", warrantyPeriod=" + warrantyPeriod + ", powerConsumption='" + powerConsumption + "'}";
    }
}