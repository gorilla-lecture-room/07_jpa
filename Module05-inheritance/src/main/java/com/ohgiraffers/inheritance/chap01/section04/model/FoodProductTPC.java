package com.ohgiraffers.inheritance.chap01.section04.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
/*
 * 📌 자식 클래스: FoodProductTPC
 */
@Entity
@Table(name = "food_products_tpc") // 💡 자식 테이블: 'food_products_tpc'
public class FoodProductTPC extends ProductTPC {

    // (부모로부터 상속받은 id, name, price, brand, stockQuantity 필드들이
    //  'food_products_tpc' 테이블의 동일한 컬럼들에 매핑됨)

    @Column(name = "expiration_date") // 💡 명시적 매핑
    private LocalDate expirationDate;

    @Column(name = "is_organic") // 💡 명시적 매핑
    private boolean isOrganic;

    @Column(name = "storage_instruction") // 💡 명시적 매핑
    private String storageInstruction;

    // (생성자, toString 등은 생략)
    protected FoodProductTPC() {}
    public FoodProductTPC(String name, double price, String brand, int stockQuantity, LocalDate expirationDate, boolean isOrganic, String storageInstruction) {
        super(name, price, brand, stockQuantity);
        this.expirationDate = expirationDate;
        this.isOrganic = isOrganic;
        this.storageInstruction = storageInstruction;
    }
    @Override
    public String toString() {
        return "FoodProductTPC{" + super.toString() + ", expirationDate=" + expirationDate + ", isOrganic=" + isOrganic + ", storageInstruction='" + storageInstruction + "'}";
    }
}