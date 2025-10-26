package com.ohgiraffers.springdatajpa.chap02.section01.controller;


import com.ohgiraffers.springdatajpa.chap02.section01.service.ProductService; // ✅ Chap02의 Service 임포트
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*; // GetMapping, PathVariable 등 임포트

import java.util.List;
import java.util.stream.Collectors;

/*
 * =====================================================================================
 * 🎮 Section 02: Controller Layer 구현 (Spring Data JPA Service 사용)
 * =====================================================================================
 *
 * - 사용자의 HTTP 요청을 받아 적절한 Service 메서드를 호출하고, 그 결과를 응답으로 반환하는 레이어입니다.
 * - Chap 01의 Controller와 코드가 거의 동일합니다! Service 구현이 변경되었지만 Controller는 영향을 받지 않았습니다.
 * 이것이 바로 레이어 분리의 힘입니다 💪
 * - `@RestController`를 사용하여 모든 핸들러 메서드의 반환 값이 HTTP 응답 본문(@ResponseBody)이 되도록 합니다.
 * (JSON/XML 등으로 자동 변환)
 * =====================================================================================
 */
@RestController("chap02-section01-controller") // @Controller + @ResponseBody
@RequestMapping("/chap02/section01/products") // 이 컨트롤러의 모든 핸들러 메서드는 '/products' 경로 하위에 매핑됨
public class ProductController {

    private final ProductService productService; // ✅ Service 계층 의존

    @Autowired
    public ProductController(ProductService productService) {
        System.out.println("ProductController(Chap02) 생성: ProductService(Chap02) 주입됨");
        this.productService = productService;
    }

    // === 조회 (Read) ===

    /**
     * 📌 ID로 특정 상품 조회 (GET /products/{productId})
     * @param productId URL 경로에서 추출할 상품 ID
     * @return 조회된 상품 정보 (JSON) 또는 404 Not Found
     */
    @GetMapping("/{productId}") // 예: /products/1
    public ResponseEntity<Product> findProductById(@PathVariable("productId") Integer productId) {
        System.out.println("\nController(Chap02) - GET /products/" + productId);
        try {
            Product product = productService.findProductById(productId);
            System.out.println("Controller(Chap02) - 응답 데이터: " + product);
            // ResponseEntity.ok(): HTTP 200 OK 상태와 함께 응답 본문에 product 객체를 담아 반환
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            // Service에서 상품을 찾지 못해 예외 발생 시
            System.err.println("Controller(Chap02) - 상품 조회 실패: " + e.getMessage());
            // ResponseEntity.notFound().build(): HTTP 404 Not Found 상태 반환
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 📌 모든 상품 조회 (GET /products)
     * @return 모든 상품 목록 (JSON 배열)
     */
    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        System.out.println("\nController(Chap02) - GET /products");
        List<Product> products = productService.findAllProducts();
        System.out.println("Controller(Chap02) - 응답 데이터 (상품 수): " + products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * 📌 특정 가격 이하 상품명 조회 (GET /products/cheap?maxPrice=...)
     * @param maxPrice 요청 파라미터로 전달된 최대 가격
     * @return 조건에 맞는 상품명 목록 (JSON 배열)
     */
    @GetMapping("/cheap") // 예: /products/cheap?maxPrice=10000
    public ResponseEntity<List<String>> findCheapProductNames(@RequestParam("maxPrice") Integer maxPrice) {
        System.out.println("\nController(Chap02) - GET /products/cheap?maxPrice=" + maxPrice);
        List<Product> cheapProducts = productService.findProductsCheaperThan(maxPrice);
        List<String> productNames = cheapProducts.stream()
                .map(Product::getProductName)
                .collect(Collectors.toList());
        System.out.println("Controller(Chap02) - 응답 데이터 (상품명 목록): " + productNames);
        return ResponseEntity.ok(productNames);
    }

    // === 생성 (Create) ===

    /**
     * 📌 새로운 상품 등록 (POST /products)
     * @param newProduct 요청 본문(JSON)에서 변환된 Product 객체
     * @return 생성된 상품 정보 (JSON)와 HTTP 201 Created 상태
     */
    @PostMapping // HTTP POST 요청 처리
    public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
        // 💡 @RequestBody: 요청 본문의 JSON 데이터를 Product 객체로 변환해줍니다.
        System.out.println("\nController(Chap02) - POST /products, 요청 본문: " + newProduct);
        Product createdProduct = productService.createProduct(newProduct);
        System.out.println("Controller(Chap02) - 응답 데이터 (생성된 상품): " + createdProduct);
        // ResponseEntity.status(HttpStatus.CREATED): HTTP 201 Created 상태 설정
        // .body(createdProduct): 응답 본문에 생성된 상품 정보 포함
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // === 수정 (Update) ===

    /**
     * 📌 특정 상품 정보 수정 (PUT /products/{productId})
     * @param productId 수정할 상품의 ID (URL 경로 변수)
     * @param updatedProductInfo 요청 본문(JSON)에서 변환된 수정할 정보가 담긴 Product 객체
     * @return 수정된 상품 정보 (JSON) 또는 404 Not Found
     */
    @PutMapping("/{productId}") // HTTP PUT 요청 처리, 예: /products/1
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Integer productId,
                                                 @RequestBody Product updatedProductInfo) {
        System.out.println("\nController(Chap02) - PUT /products/" + productId + ", 요청 본문: " + updatedProductInfo);
        try {
            // Service 메서드는 수정된 엔티티를 반환하도록 구현됨 (변경 감지 활용)
            Product updatedProduct = productService.updateProduct(
                    productId,
                    updatedProductInfo.getProductName(), // 요청 본문에서 새 이름 추출
                    updatedProductInfo.getPrice()        // 요청 본문에서 새 가격 추출
            );
            System.out.println("Controller(Chap02) - 응답 데이터 (수정된 상품): " + updatedProduct);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            // Service에서 수정할 상품을 찾지 못한 경우
            System.err.println("Controller(Chap02) - 상품 수정 실패: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // === 삭제 (Delete) ===

    /**
     * 📌 특정 상품 삭제 (DELETE /products/{productId})
     * @param productId 삭제할 상품의 ID (URL 경로 변수)
     * @return 성공 시 HTTP 204 No Content, 실패 시 404 Not Found
     */
    @DeleteMapping("/{productId}") // HTTP DELETE 요청 처리, 예: /products/1
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Integer productId) {
        System.out.println("\nController(Chap02) - DELETE /products/" + productId);
        try {
            productService.deleteProduct(productId);
            System.out.println("Controller(Chap02) - 상품 삭제 성공");
            // ResponseEntity.noContent().build(): HTTP 204 No Content 상태 (성공적으로 처리했지만 응답 본문 없음)
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Service에서 삭제할 상품을 찾지 못한 경우
            System.err.println("Controller(Chap02) - 상품 삭제 실패: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}