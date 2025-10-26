package com.ohgiraffers.springdatajpa.chap04.repository;

import com.ohgiraffers.springdatajpa.chap04.model.ProductDTO;
import com.ohgiraffers.springdatajpa.chap04.model.ProductSummary;
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * =====================================================================================
 * ✍️ Chap 03, Section 02: @Query - 내가 직접 쿼리 작성하기
 * =====================================================================================
 *
 * - 쿼리 메소드로 표현하기 어렵거나 불가능한 복잡한 쿼리, 또는 성능 최적화가 필요한 경우 사용합니다.
 * - JPQL(기본) 또는 Native SQL(nativeQuery=true)을 직접 작성할 수 있습니다.
 * - 파라미터 바인딩: ':이름' (Named Parameters) + @Param("이름"), '?숫자' (Positional Parameters)
 * - DTO 프로젝션: 'SELECT new 패키지.DTO명(...)' 구문 사용 가능
 * - 수정/삭제 쿼리: '@Modifying' 어노테이션 필수
 * =====================================================================================
 *
 *
 * 인터페이스 방식: 코드가 간결하다. 조회한 데이터를 **단순히 읽기 전용(Read-Only)**으로 화면에 전달할 때 가장 좋다.
 *  동작 방식 : 인터페이스 getter 이름 확인 후 프로젝션 쿼리 생성 -> DB 결과 조회 -> ProductSummary(인터페이스 생성) -> 값 주입.
 * DTO 방식: 코드는 조금 더 길지만 **'실제 객체'**가 생성된다.
 * 따라서 조회한 데이터에 추가적인 가공 로직(예: getVatIncludedPrice())을 포함시키거나, 계층 간에 명확한 데이터 구조를 전달하고 싶을 때 더 유연하고 강력하다.
 */
@Repository("chap04-productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // --- 쿼리 메소드 영역 (Section 01에서 작성) ---
    // ... (findByProductName, findByPriceGreaterThan 등) ...

    /*
     * ==========================================================
     * 🚀 @Query 사용 영역
     * ==========================================================
     */

    /**
     * 📌 1. JPQL 직접 작성 + 이름 기반 파라미터 바인딩
     * 특정 가격보다 낮은 상품 목록을 가격 오름차순으로 조회
     * JPQL: SELECT p FROM Product p WHERE p.price < :maxPrice ORDER BY p.price ASC
     *
     * @param maxPrice 최대 가격 (메서드 파라미터)
     * @return 조건에 맞는 상품 목록
     */
    @Query("SELECT p FROM Product p WHERE p.price < :maxPrice ORDER BY p.price ASC")
    List<Product> findProductsBelowPriceSorted(@Param("maxPrice") Integer maxPrice);
    // 💡 @Param("maxPrice"): 메서드 파라미터 maxPrice를 JPQL의 ':maxPrice' 위치에 바인딩합니다.

    /**
     * 📌 2. JPQL + 위치 기반 파라미터 바인딩 (권장하지 않음)
     * 상품 이름에 특정 키워드가 포함된 상품 목록 조회 (위치 기반)
     * JPQL: SELECT p FROM Product p WHERE p.productName LIKE %?1%
     *
     * @param keyword 포함 여부 검사 키워드
     * @return 키워드를 포함하는 상품 목록
     */
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %?1%") // ?1: 첫 번째 파라미터
    List<Product> searchProductsByNameKeyword(String keyword);
    // 🤔 위치 기반(?1, ?2)은 파라미터 순서 변경 시 오류 발생 가능성이 있어 이름 기반(:name)을 권장합니다.

    /**
     * 📌 3. JPQL + DTO 프로젝션 (new 키워드)
     * 특정 가격 이상인 상품들의 ID와 이름만 조회하여 ProductDTO 객체로 반환
     * JPQL: SELECT new com.ohgiraffers.springdatajpa.chap03.dto.ProductDTO(p.productId, p.productName)
     * FROM Product p WHERE p.price >= :minPrice
     *
     * @param minPrice 최소 가격
     * @return ProductDTO 목록
     */
    @Query("SELECT new com.ohgiraffers.springdatajpa.chap04.model.ProductDTO(p.productId, p.productName, p.price) " +
            "FROM Product p WHERE p.price >= :minPrice")
    List<ProductDTO> findProductDTOsAbovePrice(@Param("minPrice") Integer minPrice);
    // 💡 new 키워드 뒤에는 DTO 클래스의 '패키지 경로를 포함한 전체 이름'을 적고,
    //    생성자에 전달할 엔티티 필드를 순서대로 나열합니다. DTO 클래스에 해당 생성자가 있어야 합니다!

    /**
     * 📌 4. Native SQL 사용 (nativeQuery = true)
     * 상품 테이블(tbl_product)에서 상품명과 가격만 조회 (DB 테이블/컬럼 이름 직접 사용)
     * SQL: SELECT product_name, price FROM tbl_product WHERE price > ?1
     *
     * @param minPrice 최소 가격
     * @return Object 배열 목록 (각 배열은 [상품명, 가격])
     */
    @Query(value = "SELECT product_name, price FROM tbl_product WHERE price > ?1", nativeQuery = true)
    List<Object[]> findProductNameAndPriceNative(Integer minPrice);
    // 🤔 Native SQL은 DB에 종속적이 되므로 꼭 필요할 때만 사용하고, JPQL을 우선적으로 고려하세요.
    // 결과 타입이 정해져 있지 않으면 List<Object[]>로 반환됩니다.

    /**
     * 📌 5. Native SQL + 인터페이스 기반 프로젝션 (결과 매핑)
     * 상품 ID와 이름을 포함하는 ProductSummary 인터페이스로 결과 반환
     * SQL: SELECT product_id as productId, product_name as productName FROM tbl_product WHERE product_id IN (:ids)
     * (주의: Native Query에서 인터페이스 프로젝션 사용 시 alias(as)가 인터페이스의 getter 이름과 일치해야 함)
     */
    // ProductSummary 인터페이스 정의 필요 (아래에서 생성)
    @Query(value = "SELECT product_id as productId, product_name as productName " +
            "FROM tbl_product WHERE product_id IN (:ids)", nativeQuery = true)
    List<ProductSummary> findProductSummariesByIds(@Param("ids") List<Integer> ids);


    /**
     * 📌 6. 데이터 수정 쿼리 (@Modifying)
     * 특정 ID 상품의 가격을 업데이트하는 JPQL
     * JPQL: UPDATE Product p SET p.price = :newPrice WHERE p.productId = :id
     *
     * @param id        수정할 상품 ID
     * @param newPrice  새로운 가격
     * @return 영향을 받은 행(row)의 수 (int)
     */
    @Modifying // ✨ INSERT, UPDATE, DELETE 쿼리 실행 시 필수!
    @Query("UPDATE Product p SET p.price = :newPrice WHERE p.productId = :id")
    int updateProductPrice(@Param("id") Integer id, @Param("newPrice") Integer newPrice);
    // 💡 @Modifying 어노테이션은 해당 쿼리가 데이터 변경 작업임을 알리고,
    //    영속성 컨텍스트와의 동기화 등 추가 작업을 처리하도록 합니다.
    //    반환 타입은 보통 int(영향받은 행 수) 또는 void 입니다.
}

