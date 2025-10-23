package com.ohgiraffers.inheritance.chap01.section04.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
/*
 * 📌 자식 클래스: ClothingProductTPC
 */
@Entity
@Table(name = "clothing_products_tpc") // 💡 자식 테이블: 'clothing_products_tpc'
public class ClothingProductTPC extends ProductTPC {

    // (부모로부터 상속받은 id, name, price, brand, stockQuantity 필드들이
    //  'clothing_products_tpc' 테이블의 동일한 컬럼들에 매핑됨)

    @Column(name = "size") // 💡 명시적 매핑
    private String size;

    @Column(name = "material") // 💡 명시적 매핑
    private String material;

    @Column(name = "color") // 💡 명시적 매핑
    private String color;

    // (생성자, toString 등은 생략)
    protected ClothingProductTPC() {}
    public ClothingProductTPC(String name, double price, String brand, int stockQuantity, String size, String material, String color) {
        super(name, price, brand, stockQuantity);
        this.size = size;
        this.material = material;
        this.color = color;
    }
    @Override
    public String toString() {
        return "ClothingProductTPC{" + super.toString() + ", size='" + size + "', material='" + material + "', color='" + color + "'}";
    }
}