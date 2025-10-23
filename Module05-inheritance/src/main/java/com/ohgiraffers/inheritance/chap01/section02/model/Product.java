package com.ohgiraffers.inheritance.chap01.section02.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 💡 전략: 단일 테이블 (products 테이블 하나만 사용)
@DiscriminatorColumn(name = "product_type")         // 💡 DTYPE 컬럼: 'product_type' 컬럼을 구분자로 사용
@Table(name = "products")                           // 💡 테이블: 'products' 테이블에 매핑
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // 💡 명시적 매핑
    private Long id;

    @Column(name = "name") // 💡 명시적 매핑
    private String name;

    @Column(name = "price") // 💡 명시적 매핑
    private double price;

    @Column(name = "brand") // 💡 명시적 매핑
    private String brand;

    @Column(name = "stock_quantity") // (원래 명시되어 있었음)
    private int stockQuantity;

    protected Product() {}

    public Product(String name, double price, String brand, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", brand='" + brand + "', stockQuantity=" + stockQuantity + "}";
    }
}
