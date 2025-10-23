package com.ohgiraffers.inheritance.chap01.section03.model;


import jakarta.persistence.*;

/*
 * 📌 부모 클래스: ProductJoined
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)   // 💡 전략: 조인
@DiscriminatorColumn(name = "product_type")     // 💡 DTYPE 컬럼: 'products_joined.product_type'
@Table(name = "products_joined")                // 💡 부모 테이블: 'products_joined'
public class ProductJoined {
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

    @Column(name = "stock_quantity") // 💡 명시적 매핑
    private int stockQuantity;

    // (생성자, toString 등은 생략)
    protected ProductJoined() {}
    public ProductJoined(String name, double price, String brand, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.stockQuantity = stockQuantity;
    }
    @Override
    public String toString() {
        return "ProductJoined{id=" + id + ", name='" + name + "', price=" + price + ", brand='" + brand + "', stockQuantity=" + stockQuantity + "}";
    }
}
