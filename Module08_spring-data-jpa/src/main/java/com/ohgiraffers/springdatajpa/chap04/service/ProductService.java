package com.ohgiraffers.springdatajpa.chap04.service;

import com.ohgiraffers.springdatajpa.chap04.model.ProductDTO;
import com.ohgiraffers.springdatajpa.chap04.model.ProductSummary;
import com.ohgiraffers.springdatajpa.chap04.repository.ProductRepository;
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("chap04-productService")
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- 쿼리 메소드 활용 예시 (Section 01) ---
    // ... (findProductsByName 등 생략) ...

    // === @Query 활용 예시 ===

    @Transactional(readOnly = true)
    public List<Product> findProductsBelowPriceSorted(Integer maxPrice) {
        System.out.println("Service(Chap03) - findProductsBelowPriceSorted 호출: maxPrice = " + maxPrice);
        List<Product> products = productRepository.findProductsBelowPriceSorted(maxPrice); // ✅ @Query(JPQL) 호출!
        System.out.println("Service(Chap03) - 조회된 상품 수: " + products.size());
        return products;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findProductDTOsAbovePrice(Integer minPrice) {
        System.out.println("Service(Chap03) - findProductDTOsAbovePrice 호출: minPrice = " + minPrice);
        List<ProductDTO> productDTOs = productRepository.findProductDTOsAbovePrice(minPrice); // ✅ @Query(JPQL+DTO) 호출!
        System.out.println("Service(Chap03) - 조회된 DTO 수: " + productDTOs.size());
        return productDTOs;
    }

    @Transactional(readOnly = true)
    public List<Object[]> findProductNameAndPriceNative(Integer minPrice) {
        System.out.println("Service(Chap03) - findProductNameAndPriceNative 호출: minPrice = " + minPrice);
        List<Object[]> results = productRepository.findProductNameAndPriceNative(minPrice); // ✅ @Query(Native) 호출!
        System.out.println("Service(Chap03) - 조회된 결과 수: " + results.size());
        // 결과 처리 예시 (Object[] -> DTO 변환 등)
        // results.forEach(row -> System.out.println("Name: " + row[0] + ", Price: " + row[1]));
        return results;
    }

    @Transactional(readOnly = true)
    public List<ProductSummary> findProductSummariesByIds(List<Integer> ids) {
        System.out.println("Service(Chap03) - findProductSummariesByIds 호출: ids = " + ids);
        List<ProductSummary> summaries = productRepository.findProductSummariesByIds(ids); // ✅ @Query(Native + Interface Projection) 호출!
        System.out.println("Service(Chap03) - 조회된 Summary 수: " + summaries.size());
        return summaries;
    }

    @Transactional // 데이터 변경 작업!`
    public int updateProductPrice(Integer id, Integer newPrice) {
        System.out.println("Service(Chap03) - updateProductPrice 호출: id=" + id + ", newPrice=" + newPrice);
        // ✨ @Modifying + @Query 호출!
        int updatedRows = productRepository.updateProductPrice(id, newPrice);
        System.out.println("Service(Chap03) - 업데이트된 행 수: " + updatedRows);
        // 💡 주의: @Modifying 쿼리는 영속성 컨텍스트를 거치지 않고 바로 DB를 업데이트할 수 있습니다.
        // 따라서 이 쿼리 실행 후 해당 엔티티를 다시 조회해야 할 경우,
        // 영속성 컨텍스트를 비우거나(@Query(clearAutomatically = true) 옵션 사용)
        // 또는 findById 등으로 다시 로드해야 최신 상태를 반영할 수 있습니다.
        return updatedRows;
    }

    @Transactional(readOnly = true)
    public Product findProductById(Integer productId) {
        // Chap02의 findProductById 구현 재사용
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ID " + productId + " 상품 없음"));
    }
}