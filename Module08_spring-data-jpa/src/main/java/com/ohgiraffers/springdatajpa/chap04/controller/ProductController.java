package com.ohgiraffers.springdatajpa.chap04.controller;

import com.ohgiraffers.springdatajpa.chap04.model.ProductDTO;
import com.ohgiraffers.springdatajpa.chap04.model.ProductSummary;
import com.ohgiraffers.springdatajpa.chap04.service.ProductService;
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("chap04-productController")
@RequestMapping("/products-query") // 동일한 경로 사용
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- 쿼리 메소드 사용 API (Section 01) ---
    // ... (findProductsByName 등 생략) ...

    // === @Query 사용 API 예시 ===

    /**
     * 📌 특정 가격 미만 + 가격 오름차순 조회 (GET /products-query/price-below-sorted?maxPrice=...)
     */
    @GetMapping("/price-below-sorted")
    public ResponseEntity<List<Product>> findProductsBelowPriceSorted(@RequestParam("maxPrice") Integer maxPrice) {
        System.out.println("\nController(Chap03) - GET /price-below-sorted?maxPrice=" + maxPrice);
        List<Product> products = productService.findProductsBelowPriceSorted(maxPrice); // ✅ @Query(JPQL) 호출!
        System.out.println("Controller(Chap03) - 응답 데이터 수: " + products.size());
        return ResponseEntity.ok(products);
    }

    /**
     * 📌 특정 가격 이상 상품 DTO 조회 (GET /products-query/dto/price-above?minPrice=...)
     */
    @GetMapping("/dto/price-above")
    public ResponseEntity<List<ProductDTO>> findProductDTOsAbovePrice(@RequestParam("minPrice") Integer minPrice) {
        System.out.println("\nController(Chap03) - GET /dto/price-above?minPrice=" + minPrice);
        List<ProductDTO> productDTOs = productService.findProductDTOsAbovePrice(minPrice); // ✅ @Query(JPQL+DTO) 호출!
        System.out.println("Controller(Chap03) - 응답 데이터(DTO) 수: " + productDTOs.size());
        return ResponseEntity.ok(productDTOs); // DTO 목록 반환
    }

    /**
     * 📌 Native Query로 이름, 가격 조회 (GET /products-query/native/name-price?minPrice=...)
     * 🚨 주의: Native Query 결과(Object[])는 JSON으로 자동 변환 시 문제가 생길 수 있으므로,
     * Controller에서 DTO 등으로 가공하여 반환하는 것이 좋습니다. 여기서는 예시로 Object[] 리스트 반환.
     */
    @GetMapping("/native/name-price")
    public ResponseEntity<List<Object[]>> findProductNameAndPriceNative(@RequestParam("minPrice") Integer minPrice) {
        System.out.println("\nController(Chap03) - GET /native/name-price?minPrice=" + minPrice);
        List<Object[]> results = productService.findProductNameAndPriceNative(minPrice); // ✅ @Query(Native) 호출!
        System.out.println("Controller(Chap03) - 응답 데이터(Object[]) 수: " + results.size());
        // results.forEach(row -> System.out.println("  - Name: " + row[0] + ", Price: " + row[1]));
        return ResponseEntity.ok(results);
    }

    /**
     * 📌 Native Query + 인터페이스 프로젝션 조회 (GET /products-query/native/summary?ids=...)
     */
    @GetMapping("/native/summary")
    public ResponseEntity<List<ProductSummary>> findProductSummariesByIds(@RequestParam("ids") List<Integer> ids) {
        System.out.println("\nController(Chap03) - GET /native/summary?ids=" + ids);
        List<ProductSummary> summaries = productService.findProductSummariesByIds(ids); // ✅ @Query(Native + Interface Projection) 호출!
        System.out.println("Controller(Chap03) - 응답 데이터(Summary) 수: " + summaries.size());
        return ResponseEntity.ok(summaries); // 인터페이스 기반 객체 목록 반환
    }


    /**
     * 📌 상품 가격 업데이트 (PATCH /products-query/{id}/price) - 부분 수정을 위해 PUT 대신 PATCH 사용
     * @param id 수정할 상품 ID
     * @param payload 요청 본문에 포함된 새로운 가격 정보 (단순화를 위해 Map 사용)
     * @return 성공 시 HTTP 200 OK, 실패 시 404 Not Found
     */
    @PatchMapping("/{id}/price") // HTTP PATCH 요청 처리
    public ResponseEntity<String> updateProductPrice(@PathVariable("id") Integer id, @RequestBody java.util.Map<String, Integer> payload) {
        // 💡 실제로는 가격만 받는 DTO를 사용하는 것이 더 좋습니다.
        Integer newPrice = payload.get("price");
        System.out.println("\nController(Chap03) - PATCH /{id}/price: id=" + id + ", newPrice=" + newPrice);
        if (newPrice == null) {
            return ResponseEntity.badRequest().body("요청 본문에 'price' 필드가 필요합니다.");
        }
        try {
            int updatedRows = productService.updateProductPrice(id, newPrice);
            if (updatedRows > 0) {
                System.out.println("Controller(Chap03) - 가격 업데이트 성공");
                return ResponseEntity.ok("상품 ID " + id + "의 가격이 " + newPrice + "로 업데이트 되었습니다.");
            } else {
                System.err.println("Controller(Chap03) - 가격 업데이트 실패: ID " + id + " 상품 없음");
                return ResponseEntity.notFound().build(); // ID가 없는 경우 영향받은 행이 0일 수 있음
            }
        } catch (Exception e) {
            System.err.println("Controller(Chap03) - 가격 업데이트 중 오류 발생: " + e.getMessage());
            // 내부 서버 오류 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("가격 업데이트 중 오류 발생");
        }
    }
}