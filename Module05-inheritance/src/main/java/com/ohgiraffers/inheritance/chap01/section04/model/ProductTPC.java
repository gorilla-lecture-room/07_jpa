package com.ohgiraffers.inheritance.chap01.section04.model;


import jakarta.persistence.*;
/*
 * 📌 부모 클래스: ProductTPC
 * TPC는 "Table Per Class"
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 💡 전략: 클래스별 테이블
public abstract class ProductTPC {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_generator")
    @SequenceGenerator(name = "product_id_generator", sequenceName = "product_id_seq", allocationSize = 1)
    @Column(name = "id") // 💡 명시적 매핑 (모든 자식 테이블의 'id' 컬럼에 해당)
    private Long id;

    @Column(name = "name") // 💡 명시적 매핑 (모든 자식 테이블의 'name' 컬럼에 해당)
    private String name;

    @Column(name = "price") // 💡 명시적 매핑 (모든 자식 테이블의 'price' 컬럼에 해당)
    private double price;

    @Column(name = "brand") // 💡 명시적 매핑 (모든 자식 테이블의 'brand' 컬럼에 해당)
    private String brand;

    @Column(name = "stock_quantity") // 💡 명시적 매핑 (모든 자식 테이블의 'stock_quantity' 컬럼에 해당)
    private int stockQuantity;

    // (생성자, toString 등은 생략)
    protected ProductTPC() {}
    public ProductTPC(String name, double price, String brand, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.stockQuantity = stockQuantity;
    }
    @Override
    public String toString() {
        return "ProductTPC{id=" + id + ", name='" + name + "', price=" + price + ", brand='" + brand + "', stockQuantity=" + stockQuantity + "}";
    }
}