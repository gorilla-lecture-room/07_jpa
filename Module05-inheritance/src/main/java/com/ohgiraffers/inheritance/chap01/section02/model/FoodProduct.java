package com.ohgiraffers.inheritance.chap01.section02.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

/*
 * 📌 자식 클래스: FoodProduct
 */
@Entity
@DiscriminatorValue("FOOD")
public class FoodProduct extends Product {
    @Column(name = "expiration_date") // 💡 명시적 매핑
    private LocalDate expirationDate;

    @Column(name = "is_organic") // 💡 명시적 매핑
    private boolean isOrganic;

    @Column(name = "storage_instruction") // 💡 명시적 매핑
    private String storageInstruction;
    protected FoodProduct() {}

    public FoodProduct(String name, double price, String brand, int stockQuantity, LocalDate expirationDate, boolean isOrganic, String storageInstruction) {
        super(name, price, brand, stockQuantity);
        this.expirationDate = expirationDate;
        this.isOrganic = isOrganic;
        this.storageInstruction = storageInstruction;
    }

    @Override
    public String toString() {
        return "FoodProduct{" + super.toString() + ", expirationDate=" + expirationDate + ", isOrganic=" + isOrganic + ", storageInstruction='" + storageInstruction + "'}";
    }
}
