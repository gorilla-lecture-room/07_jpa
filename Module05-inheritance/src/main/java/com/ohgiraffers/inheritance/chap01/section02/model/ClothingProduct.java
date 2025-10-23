package com.ohgiraffers.inheritance.chap01.section02.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/*
 * 📌 자식 클래스: ClothingProduct
 */
@Entity
@DiscriminatorValue("CLOTHING")
public class ClothingProduct extends Product {
    @Column(name = "size") // 💡 명시적 매핑
    private String size;

    @Column(name = "material") // 💡 명시적 매핑
    private String material;

    @Column(name = "color") // 💡 명시적 매핑
    private String color;

    protected ClothingProduct() {}

    public ClothingProduct(String name, double price, String brand, int stockQuantity, String size, String material, String color) {
        super(name, price, brand, stockQuantity);
        this.size = size;
        this.material = material;
        this.color = color;
    }

    @Override
    public String toString() {
        return "ClothingProduct{" + super.toString() + ", size='" + size + "', material='" + material + "', color='" + color + "'}";
    }
}
