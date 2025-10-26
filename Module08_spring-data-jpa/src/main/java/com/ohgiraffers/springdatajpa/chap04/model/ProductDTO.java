package com.ohgiraffers.springdatajpa.chap04.model;

// 💡 @Query에서 'new' 키워드로 생성할 DTO
public class ProductDTO {
    private Integer productId;
    private String productName;
    private Integer price; // 가격 필드 추가

    // 📌 @Query의 SELECT new 구문과 파라미터 순서/타입이 일치하는 생성자 필수!
    public ProductDTO(Integer productId, String productName, Integer price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price; // 가격 초기화
    }

    // Getter
    public Integer getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getPrice() { return price; } // 가격 Getter

    @Override
    public String toString() {
        return "ProductDTO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price + // 출력에 가격 포함
                '}';
    }
}