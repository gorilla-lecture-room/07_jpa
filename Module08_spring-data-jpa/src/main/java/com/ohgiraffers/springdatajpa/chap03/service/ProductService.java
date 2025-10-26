package com.ohgiraffers.springdatajpa.chap03.service;

import com.ohgiraffers.springdatajpa.chap03.repository.ProductRepository;
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/*
 * =====================================================================================
 * 💼 Section 01: Service에서 쿼리 메소드 사용하기
 * =====================================================================================
 * - Repository에 선언한 쿼리 메소드를 Service 계층에서 호출하여 사용합니다.
 * - 비즈니스 로직과 데이터 조회 로직이 명확히 분리됩니다.
 * - 조회 작업은 `@Transactional(readOnly = true)`를 사용하는 것이 좋습니다.
 * =====================================================================================
 */
@Service("chap03-productService")
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        System.out.println("ProductService(Chap03) 생성: ProductRepository 주입됨");
        this.productRepository = productRepository;
    }

    // --- 쿼리 메소드 호출 예시 ---

    @Transactional(readOnly = true)
    public List<Product> findProductsByName(String productName) {
        System.out.println("Service(Chap03) - findProductsByName 호출: productName = " + productName);
        List<Product> products = productRepository.findByProductName(productName); // ✅ 쿼리 메소드 호출!
        System.out.println("Service(Chap03) - 조회된 상품 수: " + products.size());
        return products;
    }

    @Transactional(readOnly = true)
    public List<Product> findProductsPriceGreaterThan(Integer price) {
        System.out.println("Service(Chap03) - findProductsPriceGreaterThan 호출: price = " + price);
        List<Product> products = productRepository.findByPriceGreaterThan(price); // ✅ 쿼리 메소드 호출!
        System.out.println("Service(Chap03) - 조회된 상품 수: " + products.size());
        return products;
    }

    @Transactional(readOnly = true)
    public List<Product> findProductsByNameContaining(String keyword) {
        System.out.println("Service(Chap03) - findProductsByNameContaining 호출: keyword = " + keyword);
        List<Product> products = productRepository.findByProductNameContaining(keyword); // ✅ 쿼리 메소드 호출!
        System.out.println("Service(Chap03) - 조회된 상품 수: " + products.size());
        return products;
    }

    @Transactional(readOnly = true)
    public List<Product> findProductsCheaperThanAndSort(Integer price) {
        System.out.println("Service(Chap03) - findProductsCheaperThanAndSort 호출: price = " + price);
        // Sort 객체 생성: Sort.by("필드명").descending() / .ascending()
        Sort sort = Sort.by("price").descending(); // 가격 내림차순 정렬
        List<Product> products = productRepository.findByPriceLessThan(price, sort); // ✅ 정렬 파라미터 전달!
        System.out.println("Service(Chap03) - 조회된 상품 수: " + products.size());
        return products;
    }

    @Transactional(readOnly = true)
    public List<Product> findProductsByIds(List<Integer> productIds) {
        System.out.println("Service(Chap03) - findProductsByIds 호출: productIds = " + productIds);
        List<Product> products = productRepository.findByProductIdIn(productIds); // ✅ In 조건 쿼리 메소드 호출!
        System.out.println("Service(Chap03) - 조회된 상품 수: " + products.size());
        return products;
    }

 }