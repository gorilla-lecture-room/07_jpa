package com.ohgiraffers.springdatajpa.chap03.repository;

import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/*
 * =====================================================================================
 * 🔎 Chap 03, Section 01: 메서드 이름으로 쿼리 만들기 (Query Methods)
 * =====================================================================================
 */

/*
 * [학습자 타이핑용 ✏️]
 *
 * 1. 쿼리 메서드 (Query Methods)란?
 * - JpaRepository 인터페이스에 '정해진 키워드 규칙'에 맞게 메서드를 선언하는 방식입니다.
 * - 개발자가 JPQL 쿼리문을 직접 작성하지 않아도 됩니다.
 *
 * 2. 동작 원리
 * - Spring Data JPA가 애플리케이션 시작 시점에 메서드 이름을 '파싱(Parsing)'합니다.
 * - 메서드 이름에 포함된 키워드(예: findBy, And, GreaterThan)를 분석하여
 * 'JPQL 쿼리'를 '자동으로 생성'하고 실행합니다.
 *
 * 3. 예시 (규칙)
 * - findByName(String name)
 * -> JPQL: SELECT p FROM Product p WHERE p.name = ?1
 * - findByPriceGreaterThan(int price)
 * -> JPQL: SELECT p FROM Product p WHERE p.price > ?1
 * - findByNameAndStock(String name, int stock)
 * -> JPQL: SELECT p FROM Product p WHERE p.name = ?1 AND p.stock = ?2
 *
 * 4. 장점
 * - 매우 간단한 조회 쿼리를 빠르고 직관적으로 작성할 수 있습니다.
 * - 쿼리문을 문자열로 작성할 때 발생하는 오타(typo) 문제를 방지할 수 있습니다.
 * - 메서드 이름에 오타(예: 엔티티 필드명 오류)가 있으면 앱 시작 시점에 오류를 잡아줍니다.
 *
 * 5. 단점
 * - 조건이 많아지면 메서드 이름이 극도로 길어집니다.
 * - 복잡한 쿼리(예: 조인, 서브쿼리, DTO 프로젝션)에는 한계가 명확합니다.
 */

/*
 * =====================================================================================
 * [교수자 설명용 👨‍🏫]
 * =====================================================================================
 *
 * 1. JpaRepository의 '자동 구현' 원리의 확장
 *
 * - [복습] Chap 02에서 `save()`, `findById()`는 JpaRepository를 상속받는 것만으로
 * '자동 구현'되었습니다. 이것들은 '표준 CRUD'입니다.
 *
 * - [확장] '쿼리 메서드'는 이 자동 구현 원리를 '커스텀 조회' 영역까지 확장시킨 것입니다.
 * Spring Data JPA는 'find...By...', 'count...By...', 'exists...By...' 등의
 * 특정 '접두사(Prefix)'로 시작하는 메서드 이름을 인식합니다.
 *
 * - [동작] Spring Data JPA의 '쿼리 생성기(Query Builder)'가 메서드 이름을
 * 마치 '문법(Grammar)'처럼 파싱합니다.
 *
 * - (1) `findBy` (조회) `Name` (엔티티의 'name' 필드) `And` (논리연산자) `Price` (엔티티의 'price' 필드)
 * - (2) `GreaterThan` (조건 키워드)
 *
 * - 이 파싱 결과를 바탕으로 실행 가능한 JPQL(Java Persistence Query Language)을
 * 동적으로 생성하여 내부의 EntityManager에게 전달합니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 주요 키워드 문법 (JPA Query Method Grammar)
 *
 * - (1) 조건 키워드 (WHERE 절):
 * - `And`, `Or`: `findByNameAndPrice(String name, int price)`
 * - `GreaterThan`, `LessThan`, `Equal`: `findByPriceGreaterThan(int price)`
 * - `Between`: `findByPriceBetween(int min, int max)`
 * - `IsNull`, `IsNotNull`: `findByNameIsNull()`
 * - `True`, `False`: `findByIsActiveTrue()`
 *
 * - (2) 문자열 검색 키워드 (LIKE):
 * - `StartingWith`: `findByNameStartingWith(String prefix)` -> `WHERE name LIKE 'prefix%'`
 * - `EndingWith`: `findByNameEndingWith(String suffix)` -> `WHERE name LIKE '%suffix'`
 * - `Containing`: `findByNameContaining(String infix)` -> `WHERE name LIKE '%infix%'`
 * - `Like`: `findByNameLike(String pattern)` -> `WHERE name LIKE 'pattern'`
 * (Like는 개발자가 직접 '%' 와일드카드를 파라미터에 포함시켜야 합니다.)
 *
 * - (3) 정렬 (ORDER BY):
 * - `OrderBy`...`Asc`, `Desc`:
 * `findByNameOrderByPriceDesc(String name)`
 * -> `WHERE name = ?1 ORDER BY price DESC`
 *
 * - (4) 중첩 속성 (Nested Property) / 암묵적 조인:
 * - 만약 `Product` 엔티티가 `Category` 엔티티를 가지고 있다면 (예: `private Category category;`)
 * `findByCategoryName(String categoryName)`
 * -> `Product`의 `category` 필드의 `name` 필드를 의미합니다.
 * -> JPQL: `SELECT p FROM Product p JOIN p.category c WHERE c.name = ?1`
 * (개발자는 조인(JOIN)을 명시하지 않았지만, JPA가 자동으로 조인 쿼리를 생성합니다.)
 *
 * -------------------------------------------------------------------------------------
 *
 * 3. 쿼리 메서드의 가장 강력한 장점: '컴파일 타임'이 아닌 '부트 타임' 오류 검사
 *
 * - [문제] 만약 개발자가 JPQL을 문자열로 직접 작성(`@Query`)하면, 오타(예: `SELECT p FROM Prduct p...`)가
 * 있어도 '컴파일(Compile)'은 성공합니다. 이 버그는 '런타임(Runtime)'에 해당 쿼리가
 * '실행'될 때가 되어서야 `QuerySyntaxException` 등으로 발견됩니다.
 *
 * - [장점] 쿼리 메서드를 사용하면 다릅니다.
 * 만약 `Product` 엔티티에 'namee'이라는 필드가 없는데,
 * 개발자가 `findByNamee(String name)`라고 오타를 낸다면,
 *
 * '컴파일'은 성공하지만(문법적으론 Java 메서드일 뿐),
 * Spring Data JPA가 Bean을 등록하기 위해 메서드를 파싱하는
 * '애플리케이션 시작(Boot) 시점'에
 * "Property 'namee' not found for type 'Product'!" 라는 오류를 내뱉으며
 * '애플리케이션 실행 자체가 실패'합니다.
 *
 * - [결론] 이는 버그를 런타임이 아닌 개발 초기 단계(부트 시점)에 '미리' 잡을 수 있게 해주는
 * 매우 강력한 'Fail-Fast' 전략입니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 4. 사용 시기 (When to use)
 *
 * - (O) 사용하기 좋을 때:
 * - 엔티티의 특정 필드 1~3개 정도로 간단하게 'AND' 조건 검색을 할 때.
 * - (예: `findUserByEmail(String email)`, `findItemByOwnerIdAndIsSold(Long id, boolean isSold)`)
 *
 * - (X) 사용을 피해야 할 때 (다음 섹션의 `@Query` 사용):
 * - (1) 메서드 이름이 너무 길어질 때 (예: 4개 이상의 조건) -> 가독성 최악
 * - (2) 복잡한 조인(특히 `LEFT JOIN FETCH`)이 필요할 때
 * - (3) DTO 프로젝션(DTO Projection)이 필요할 때 (SELECT 절 커스터마이징)
 * - (4) 서브쿼리, GROUP BY 등이 필요할 때
 */
@Repository("chap03-productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // --- 기본 CRUD는 JpaRepository가 제공 ---
    // save(), findById(), findAll(), deleteById(), count(), existsById() ...

    /**
     * 📌 상품 이름(productName)으로 상품 목록 조회
     * Spring Data JPA가 생성하는 JPQL: "SELECT p FROM Product p WHERE p.productName = ?1"
     * @param productName 조회할 상품 이름 (메서드 파라미터가 JPQL의 ?1 위치에 바인딩됨)
     * @return 이름이 일치하는 상품 목록
     */
    List<Product> findByProductName(String productName);

    /**
     * 📌 특정 가격(price)보다 비싼 상품 목록 조회
     * JPQL: "SELECT p FROM Product p WHERE p.price > ?1"
     * @param price 비교할 가격
     * @return 해당 가격보다 비싼 상품 목록
     */
    List<Product> findByPriceGreaterThan(Integer price);


    /**
     * 📌 상품 이름(productName)에 특정 키워드가 포함된 상품 목록 조회
     * JPQL: "SELECT p FROM Product p WHERE p.productName LIKE %?1%"
     * @param keyword 포함 여부를 검사할 키워드
     * @return 키워드를 포함하는 상품 목록
     */
    List<Product> findByProductNameContaining(String keyword);
    /**
     * 📌 특정 가격(price)보다 낮은 상품 목록 조회 (정렬 조건 추가)
     * JPQL: "SELECT p FROM Product p WHERE p.price < ?1 ORDER BY p.price ASC" (가격 오름차순)
     * @param price 비교할 가격
     * @param sort  정렬 조건 (동적으로 변경 가능!) - org.springframework.data.domain.Sort 사용
     * @return 해당 가격 미만 상품 목록 (주어진 조건으로 정렬)
     */
    List<Product> findByPriceLessThan(Integer price, Sort sort);



    //--- 예시를 위한 선택 설명
    /**
     * 📌 여러 상품 ID(productId) 목록에 해당하는 상품 목록 조회 (In 조건)
     * JPQL: "SELECT p FROM Product p WHERE p.productId IN (?1)"
     * @param productIds 조회할 상품 ID 목록 (Collection 타입)
     * @return ID 목록에 포함된 상품 목록
     */
    List<Product> findByProductIdIn(List<Integer> productIds);

    /**
     * 📌 특정 가격 범위(price) 내의 상품 목록 조회
     * JPQL: "SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2"
     * @param minPrice 최소 가격 (메서드의 첫 번째 파라미터 -> ?1)
     * @param maxPrice 최대 가격 (메서드의 두 번째 파라미터 -> ?2)
     * @return 가격 범위 내의 상품 목록
     */
    List<Product> findByPriceBetween(Integer minPrice, Integer maxPrice);

    /**
     * 📌 상품 이름(productName)으로 조회 + 가격(price) 내림차순 정렬
     * JPQL: "SELECT p FROM Product p WHERE p.productName = ?1 ORDER BY p.price DESC"
     * @param productName 조회할 상품 이름
     * @return 이름이 일치하는 상품 목록 (가격 내림차순 정렬)
     */
    List<Product> findByProductNameOrderByPriceDesc(String productName);


    // 💡 더 많은 키워드 조합이 가능합니다! (And, Or, IsNull, NotNull 등)
    // 예: 상품 이름에 키워드가 포함되거나 가격이 특정 값 미만인 상품 조회
    // List<Product> findByProductNameContainingOrPriceLessThan(String keyword, Integer price);
}