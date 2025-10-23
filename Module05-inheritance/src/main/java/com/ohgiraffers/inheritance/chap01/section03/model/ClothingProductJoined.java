package com.ohgiraffers.inheritance.chap01.section03.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
/*
 * 📌 자식 클래스: ClothingProductJoined
 */
@Entity
@Table(name = "clothing_products_joined") // 💡 자식 테이블: 'clothing_products_joined'
@DiscriminatorValue("CLOTHING")             // 💡 DTYPE 값
public class ClothingProductJoined extends ProductJoined {

    @Column(name = "size") // 💡 명시적 매핑
    private String size;

    @Column(name = "material") // 💡 명시적 매핑
    private String material;

    @Column(name = "color") // 💡 명시적 매핑
    private String color;

    // (생성자, toString 등은 생략)
    protected ClothingProductJoined() {}
    public ClothingProductJoined(String name, double price, String brand, int stockQuantity, String size, String material, String color) {
        super(name, price, brand, stockQuantity);
        this.size = size;
        this.material = material;
        this.color = color;
    }
    @Override
    public String toString() {
        return "ClothingProductJoined{" + super.toString() + ", size='" + size + "', material='" + material + "', color='" + color + "'}";
    }
}