package com.ohgiraffers.springdatajpa.chap04.model;

/*
 * =====================================================================================
 * 📌 5번 예제: 인터페이스 기반 프로젝션(Interface-Based Projection) 정의
 * =====================================================================================
 */

/*
 * [학습자 타이핑용 ✏️]
 *
 * 1. 프로젝션(Projection)이란?
 * - 엔티티(Entity)의 '전체' 필드가 아닌, '일부 특정 필드'만 선택하여
 * 조회(SELECT)하는 기술입니다. (예: 20개 필드 중 2개만 조회)
 *
 * 2. 인터페이스 기반 프로젝션 (Interface-Based Projection)
 * - 조회할 필드들의 'getter' 메서드 시그니처만 포함하는 '인터페이스'를 정의하는 방식입니다.
 *
 * 3. 작성 규칙
 * - (1) 인터페이스로 선언합니다. (예: `interface ProductSummary`)
 * - (2) 조회하려는 엔티티의 '필드명'과 '정확히 일치'하는 'getter' 메서드를 정의합니다.
 * - (예: 엔티티의 `productId` 필드 -> `getProductId()`)
 * - (예: 엔티티의 `productName` 필드 -> `getProductName()`)
 *
 * 4. 사용법
 * - 이 인터페이스(`ProductSummary`)를 JpaRepository 쿼리 메소드의 '반환 타입'으로
 * 사용합니다.
 * - (예: `List<ProductSummary> findByName(String name);`)
 *
 * 5. 장점
 * - 'SELECT *'가 아닌 'SELECT id, name'처럼 최적화된 쿼리가 실행됩니다.
 * - 전체 엔티티가 아닌 가벼운 'DTO(Data Transfer Object)'를 반환받아
 * 네트워크, 메모리, CPU 성능을 향상시킵니다.
 */

/*
 * =====================================================================================
 * [교수자 설명용 👨‍🏫]
 * =====================================================================================
 *
 * 1. ❓ 왜 프로젝션이 필요한가? (Over-fetching 문제)
 *
 * - [문제] 만약 `Product` 엔티티에 20개의 필드(id, name, price, stock, description,
 * createdAt, modifiedAt, etc...)가 있다고 가정해 봅시다.
 *
 * - `List<Product> findAll();`
 *
 * - 위 메서드는 '상품 목록'을 표시하기 위해 호출되지만, 목록에는 단지 '이름'과 '가격'만 필요할 수 있습니다.
 * - 하지만 JPA는 `SELECT * ...` (정확히는 `SELECT p FROM Product p...`)를 실행하여 20개 필드를 '전부' DB에서 조회하고, '전부' 영속성 컨텍스트에 로드합니다.
 *
 * - [낭비] 이것을 '과다 조회(Over-fetching)'라고 부릅니다.
 * - (1) DB-애플리케이션 간 '네트워크 대역폭' 낭비
 * - (2) 애플리케이션 '메모리(Heap)' 낭비
 * - (3) JPA가 불필요한 필드까지 매핑하고 스냅샷을 만드는 'CPU' 낭비
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 💡 프로젝션: 필요한 것만 골라 담기
 *
 * - [해결] 프로젝션은 DB의 `SELECT` 절을 직접 제어하는 기술입니다.
 * `SELECT *` 대신 `SELECT product_id, product_name FROM product`를 실행하게 합니다.
 *
 * - '인터페이스 기반 프로젝션'은 Spring Data JPA가 제공하는 가장 세련된 방식입니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 3. ⚙️ 동작 원리 (최적화된 쿼리 + 프록시)
 *
 * - (1) Repository 선언:
 * `List<ProductSummary> findByPriceGreaterThan(int price);`
 *
 * - (2) 쿼리 자동 생성 (핵심):
 * Spring Data JPA는 반환 타입이 '엔티티(`Product`)'가 아닌 '인터페이스(`ProductSummary`)'
 * 인 것을 인지합니다.
 *
 * 그리고 '인터페이스의 getter 메서드명'(`getProductId`, `getProductName`)을 분석하여,
 * `Product` 엔티티의 `productId`, `productName` 필드만 선택하는
 * '최적화된 JPQL'을 '자동으로' 생성합니다.
 *
 * -> JPQL: `SELECT p.productId, p.productName FROM Product p WHERE p.price > ?1`
 *
 * - (3) 프록시 객체 반환 (매우 중요):
 * Spring Data JPA는 조회 결과(예: `[1, "Laptop"]`, `[2, "Mouse"]`)를
 * 'Product' 엔티티 객체로 변환하지 *않습니다*. (영속성 컨텍스트에도 넣지 않음)
 *
 * 대신, 이 조회 결과를 '즉시' `ProductSummary` 인터페이스를 구현하는
 * '가짜 프록시(Proxy) 객체'에 담아서 반환합니다.
 *
 * - (4) 사용:
 * List<ProductSummary> summaries = ...;
 * String name = summaries.get(0).getProductName(); // "Laptop" (프록시가 값 반환)
 *
 * -------------------------------------------------------------------------------------
 *
 * 4. ⚠️ 규칙: getter 메서드명은 엔티티 필드명과 '정확히' 일치해야 한다
 *
 * - `getProductId()` -> `Product` 엔티티의 `private Integer productId;` 필드
 * - `getProductName()` -> `Product` 엔티티의 `private String productName;` 필드
 *
 * - 만약 오타(예: `getProductNamee()`)를 내면,
 * Spring Data JPA가 부트 시점에 "Property `productNamee` not found for type `Product`"
 * 라는 오류를 내며 실행을 '실패'시킵니다. (Fail-Fast)
 *
 * - [참고] 클래스 기반 프로젝션 (Class-Based Projection)
 * - 인터페이스 대신, `ProductSummaryDto`라는 '클래스'를 만들어서 사용할 수도 있습니다.
 * 이 경우, 클래스는 필드, 생성자, getter를 가져야 합니다.
 * - 인터페이스 방식은 '읽기 전용' 데이터를 가져올 때 더 간단하고 가볍습니다.
 */
public interface ProductSummary {
    Integer getProductId();
    String getProductName();
}
