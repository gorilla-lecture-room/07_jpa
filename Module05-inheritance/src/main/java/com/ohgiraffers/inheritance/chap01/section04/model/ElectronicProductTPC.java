package com.ohgiraffers.inheritance.chap01.section04.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
/*
 * 📌 자식 클래스: ElectronicProductTPC
 */
@Entity
@Table(name = "electronic_products_tpc") // 💡 자식 테이블: 'electronic_products_tpc'
public class ElectronicProductTPC extends ProductTPC {

    // (부모로부터 상속받은 id, name, price, brand, stockQuantity 필드들이
    //  'electronic_products_tpc' 테이블의 동일한 컬럼들에 매핑됨)

    @Column(name = "warranty_period") // 💡 명시적 매핑
    private int warrantyPeriod;

    @Column(name = "power_consumption") // 💡 명시적 매핑
    private String powerConsumption;

    // (생성자, toString 등은 생략)
    protected ElectronicProductTPC() {}
    public ElectronicProductTPC(String name, double price, String brand, int stockQuantity, int warrantyPeriod, String powerConsumption) {
        super(name, price, brand, stockQuantity);
        this.warrantyPeriod = warrantyPeriod;
        this.powerConsumption = powerConsumption;
    }
    @Override
    public String toString() {
        return "ElectronicProductTPC{" + super.toString() + ", warrantyPeriod=" + warrantyPeriod + ", powerConsumption='" + powerConsumption + "'}";
    }
}
