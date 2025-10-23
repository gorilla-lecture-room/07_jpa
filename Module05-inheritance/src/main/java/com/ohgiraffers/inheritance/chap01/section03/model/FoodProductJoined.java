package com.ohgiraffers.inheritance.chap01.section03.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

/*
 * 📌 자식 클래스: FoodProductJoined
 */
@Entity
@Table(name = "food_products_joined") // 💡 자식 테이블: 'food_products_joined'
@DiscriminatorValue("FOOD")             // 💡 DTYPE 값
public class FoodProductJoined extends ProductJoined {

    @Column(name = "expiration_date") // 💡 명시적 매핑
    private LocalDate expirationDate;

    @Column(name = "is_organic") // 💡 명시적 매핑
    private boolean isOrganic;

    @Column(name = "storage_instruction") // 💡 명시적 매핑
    private String storageInstruction;

    // (생성자, toString 등은 생략)
    protected FoodProductJoined() {}
    public FoodProductJoined(String name, double price, String brand, int stockQuantity, LocalDate expirationDate, boolean isOrganic, String storageInstruction) {
        super(name, price, brand, stockQuantity);
        this.expirationDate = expirationDate;
        this.isOrganic = isOrganic;
        this.storageInstruction = storageInstruction;
    }
    @Override
    public String toString() {
        return "FoodProductJoined{" + super.toString() + ", expirationDate=" + expirationDate + ", isOrganic=" + isOrganic + ", storageInstruction='" + storageInstruction + "'}";
    }
}
