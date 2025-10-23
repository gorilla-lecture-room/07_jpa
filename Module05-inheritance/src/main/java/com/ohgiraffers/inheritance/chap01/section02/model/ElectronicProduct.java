package com.ohgiraffers.inheritance.chap01.section02.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


/*
 * 📌 자식 클래스: ElectronicProduct
 */
@Entity
@DiscriminatorValue("ELECTRONIC") // 💡 DTYPE 값: 'product_type' 컬럼에 'ELECTRONIC'으로 저장됨
public class ElectronicProduct extends Product {

    @Column(name = "warranty_period") // (원래 명시되어 있었음)
    private int warrantyPeriod;

    @Column(name = "power_consumption") // 💡 명시적 매핑
    private String powerConsumption;

    protected ElectronicProduct() {}

    public ElectronicProduct(String name, double price, String brand, int stockQuantity, int warrantyPeriod, String powerConsumption) {
        super(name, price, brand, stockQuantity);
        this.warrantyPeriod = warrantyPeriod;
        this.powerConsumption = powerConsumption;
    }

    @Override
    public String toString() {
        return "ElectronicProduct{" + super.toString() + ", warrantyPeriod=" + warrantyPeriod + ", powerConsumption='" + powerConsumption + "'}";
    }
}