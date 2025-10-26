package com.ohgiraffers.springdatajpa.chap03.controller;


import com.ohgiraffers.springdatajpa.chap03.service.ProductService;
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * =====================================================================================
 * 🎮 Section 01: Controller에서 쿼리 메소드 활용하기
 * =====================================================================================
 * - Service 계층에 추가된 쿼리 메소드 호출 로직을 Controller에서 사용하여
 * 다양한 조건의 조회 API 엔드포인트를 제공합니다.
 * =====================================================================================
 */
@RestController("chap03-productController") // @Controller + @ResponseBody, API 컨트롤러에 주로 사용
@RequestMapping("/products-query") // 이전 Controller와 경로 충돌 방지를 위해 '/products-query' 사용
public class ProductController {

    private final ProductService productService; // ✅ Chap03의 Service 주입

    @Autowired
    public ProductController(ProductService productService) {
        System.out.println("ProductController(Chap03) 생성: ProductService(Chap03) 주입됨");
        this.productService = productService;
    }

    // --- 쿼리 메소드 사용 API 예시 ---

    /**
     * 📌 상품 이름으로 조회 (GET /products-query/name?productName=...)
     */
    @GetMapping("/name")
    public ResponseEntity<List<Product>> findProductsByName(@RequestParam("productName") String productName) {
        System.out.println("\nController(Chap03) - GET /name?productName=" + productName);
        List<Product> products = productService.findProductsByName(productName);
        System.out.println("Controller(Chap03) - 응답 데이터 수: " + products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * 📌 특정 가격 초과 상품 조회 (GET /products-query/price-greater?price=...)
     */
    @GetMapping("/price-greater")
    public ResponseEntity<List<Product>> findProductsPriceGreaterThan(@RequestParam("price") Integer price) {
        System.out.println("\nController(Chap03) - GET /price-greater?price=" + price);
        List<Product> products = productService.findProductsPriceGreaterThan(price);
        System.out.println("Controller(Chap03) - 응답 데이터 수: " + products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * 📌 상품 이름 포함 조회 (GET /products-query/name-containing?keyword=...)
     */
    @GetMapping("/name-containing")
    public ResponseEntity<List<Product>> findProductsByNameContaining(@RequestParam("keyword") String keyword) {
        System.out.println("\nController(Chap03) - GET /name-containing?keyword=" + keyword);
        List<Product> products = productService.findProductsByNameContaining(keyword);
        System.out.println("Controller(Chap03) - 응답 데이터 수: " + products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * 📌 특정 가격 미만 + 정렬 조회 (GET /products-query/price-less-sorted?price=...)
     */
    @GetMapping("/price-less-sorted")
    public ResponseEntity<List<Product>> findProductsCheaperThanAndSort(@RequestParam("price") Integer price) {
        System.out.println("\nController(Chap03) - GET /price-less-sorted?price=" + price);
        // Service에서 가격 내림차순으로 정렬하도록 구현했음
        List<Product> products = productService.findProductsCheaperThanAndSort(price);
        System.out.println("Controller(Chap03) - 응답 데이터 수: " + products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * 📌 여러 ID로 조회 (GET /products-query/by-ids?ids=1,3,5)
     * @param ids 쉼표(,)로 구분된 ID 목록 문자열
     */
    @GetMapping("/by-ids")
    public ResponseEntity<List<Product>> findProductsByIds(@RequestParam("ids") List<Integer> ids) {
        // Spring MVC는 요청 파라미터 '?ids=1,3,5' 를 List<Integer>로 자동 변환해줍니다!
        System.out.println("\nController(Chap03) - GET /by-ids?ids=" + ids);
        List<Product> products = productService.findProductsByIds(ids);
        System.out.println("Controller(Chap03) - 응답 데이터 수: " + products.size());
        return ResponseEntity.ok(products);
    }

    // 💡 참고: 기본 CRUD API (findById, findAll, create, update, delete)는
    // Chap02의 Controller와 동일하게 여기에 구현할 수 있습니다. (경로만 '/products-query'로 조정)
}