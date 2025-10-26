package com.ohgiraffers.springdatajpa.chap02.section01.service;

import com.ohgiraffers.springdatajpa.chap02.section01.repository.ProductRepository;
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Spring의 Transactional 사용

import java.util.List;
import java.util.Optional;

/*
 * =====================================================================================
 * ✨ Section 02: Repository를 활용한 Service 리팩토링
 * =====================================================================================
 */

/*
 * [학습자 타이핑용 ✏️]
 *
 * 1. Service Layer 역할 (동일)
 * - 핵심 비즈니스 로직을 처리하고, 트랜잭션을 관리하는 책임은 동일합니다.
 *
 * 2. 리팩토링 핵심: 의존성 변경
 * - [Before] Chap 01: `EntityManager`를 직접 사용한 '구현 클래스'
 * (예: `OldProductRepositoryImpl`)에 의존
 * - [After] Chap 02: Spring Data JPA가 자동 구현해주는 '인터페이스'
 * (예: `ProductRepository`)에 의존
 *
 * 3. 결과: 코드의 간결화
 * - 서비스 로직은 동일하지만, 호출하는 리포지토리의 내부 구현이 사라졌습니다.
 * - `em.persist(product)` 호출 -> `productRepository.save(product)` 호출
 * (서비스 코드 자체는 비슷해 보일 수 있으나, 리포지토리 '구현체' 자체가 사라짐)
 *
 * 4. @Transactional (선언적 트랜잭션)
 * - Chap 01의 리포지토리 구현체에 있던 'try-catch-begin-commit-rollback'
 * '명령형' 트랜잭션 코드가 완전히 제거됩니다.
 * - `@Transactional` 어노테이션 '선언' 하나로 스프링이 트랜잭션을 자동으로 관리합니다.
 */

/*
 * =====================================================================================
 * [교수자 설명용 👨‍🏫]
 * =====================================================================================
 * 1. 서비스 역할의 불변성 (비즈니스 로직 & 트랜잭션)
 * - 리팩토링을 하더라도 '서비스 레이어'의 근본적인 '책임'은 변하지 않습니다.
 * - (1) 비즈니스 로직: "상품을 등록한다", "상품을 조회한다"와 같은 핵심 업무 규칙을 수행합니다.
 * - (2) 트랜잭션 관리: 이 업무 규칙이 '하나의 작업 단위(트랜잭션)'로
 * 안전하게 실행(All or Nothing)되도록 보장합니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 리팩토링의 핵심: 의존성 역전 (DIP)
 * 이 리팩토링은 객체지향의 핵심 원칙인 '의존성 역전 원칙(DIP)'을 명확하게 보여줍니다.
 * - [Before] Chap 01 (구현체 의존):
 * `ProductService` -> `ProductRepositoryImpl` (구체적인 클래스)
 * 서비스는 '어떻게(How)' 저장하는지(EntityManager 사용 방식)를 아는 구현체에 의존했습니다.
 *
 * - [After] Chap 02 (추상화 의존):
 * `ProductService` -> `ProductRepository` (인터페이스)
 * 서비스는 이제 '무엇을(What)' 하는지(`save`, `findById`)만 정의된 '인터페이스'에 의존합니다.
 *
 * - [장점] 서비스는 더 이상 데이터 접근 기술이 '순수 JPA'인지, 'Spring Data JPA'인지,
 * 심지어 'MyBatis'인지 알 필요가 없어집니다.
 * 인터페이스라는 '계약'에만 의존하므로, 리포지토리의 내부 구현이 변경되어도
 * 서비스 코드는 영향을 받지 않습니다. (느슨한 결합)
 *
 *
 * -------------------------------------------------------------------------------------
 * 3. 데이터 접근 로직이 극도로 간결해진 이유
 * "서비스 코드에서 `repository.save()` 호출은 전과 동일한데요?"
 * - '간결함'은 서비스 클래스가 아닌 '프로젝트 전체 코드' 관점에서 봐야 합니다.
 * - [Before] Chap 01:
 * 1. `ProductRepository` (인터페이스) 정의
 * 2. `ProductRepositoryImpl` (클래스) 구현 (내부에 `EntityManager`와 보일러플레이트 코드 작성)
 * 3. `ProductService` (클래스) 구현 (Impl을 주입받아 호출)
 * (총 3개의 파일/클래스가 필요)
 *
 * - [After] Chap 02:
 * 1. `ProductRepository` (인터페이스, JpaRepository 상속) 정의
 * 2. (구현 클래스 없음 - Spring Data JPA가 자동 생성)
 * 3. `ProductService` (클래스) 구현 (인터페이스를 주입받아 호출)
 * (총 2개의 파일/클래스만 필요)
 *
 * - 즉, `ProductRepositoryImpl` 클래스 파일 자체가 통째로 '삭제'됩니다.
 * 개발자가 작성해야 할 코드 라인 수가 수십 줄 이상 줄어듭니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 4. `@Transactional`을 통한 '선언적(Declarative)' 트랜잭션
 * 이 리팩토링을 통해 '트랜잭션 관리' 코드가 완전히 다른 차원으로 이전됩니다.
 * - [Before] '명령형(Imperative)' 트랜잭션:
 * (주로 RepositoryImpl 내부에) 개발자가 '직접' 트랜잭션 코드를 '명령'했습니다.
 * EntityTransaction tx = em.getTransaction();
 * try {
 * tx.begin();
 * em.persist(product); // <-- 비즈니스 로직과 트랜잭션 코드 혼재
 * tx.commit();
 * } catch (Exception e) {
 * tx.rollback();
 * }
 *
 * - [After] '선언형(Declarative)' 트랜잭션:
 * 서비스 메서드 위에 `@Transactional` 어노테이션 하나만 '선언'합니다.
 * @Transactional
 * public void create(Product p) {
 * productRepository.save(p); // <-- 순수 비즈니스 로직만 남음
 * }
 *
 * - [동작 원리] 스프링 AOP(프록시)가 `@Transactional`을 보고,
 * 위 [Before]의 `try-catch-begin-commit-rollback` 코드를
 * 메서드 실행 '전/후'에 '자동으로' 삽입해 줍니다.
 *
 * - [결과] 서비스 로직에서 트랜잭션 처리라는 '횡단 관심사(Cross-cutting concern)'가
 * 완벽하게 분리되어, 코드가 매우 깔끔해지고 '비즈니스 로직'에만 집중할 수 있게 됩니다.
 */
@Service("chap02-section01-service") // 비즈니스 로직 컴포넌트
public class ProductService {

    private final ProductRepository productRepository; // ✨ Spring Data JPA Repository 주입!

    // 📌 생성자 주입 (@Autowired 생략 가능)
    // Spring 컨테이너가 ProductRepository 인터페이스의 자동 생성된 구현체(Bean)를 찾아 주입합니다.
    @Autowired
    public ProductService(ProductRepository productRepository) {
        System.out.println("ProductService 생성: ProductRepository 주입됨 = " + productRepository.getClass().getName()); // 주입된 객체의 실제 클래스 이름 확인
        this.productRepository = productRepository;
    }

    /*
     * 📌 @Transactional 어노테이션 복습
     * - 메서드 시작 시 트랜잭션 시작, 성공 시 커밋, 예외 발생 시 롤백.
     * - 데이터 변경(C, U, D) 작업 시 필수!
     * - (readOnly=true): 조회 메서드에 적용 시 성능 최적화 (JPA가 변경 감지 등 불필요 작업 생략)
     */

    // ID로 상품 조회
    @Transactional(readOnly = true) // 조회 전용 트랜잭션
    public Product findProductById(Integer productId) {
        System.out.println("Service - findProductById 호출: ID = " + productId);
        // ✨ JpaRepository의 findById() 사용! EntityManager 관련 코드 없음!
        // 반환 타입이 Optional<Product> 이므로, 값이 없을 경우에 대한 처리가 필요합니다.
        Optional<Product> optionalProduct = productRepository.findById(productId);

        // orElseThrow() : 값이 있으면 Product 반환, 없으면 예외 발생 (다른 처리 방식도 가능)
        Product product = optionalProduct.orElseThrow(() -> new IllegalArgumentException("ID " + productId + "에 해당하는 상품이 없습니다."));
        System.out.println("Service - 조회된 Product: " + product);
        return product;
    }

    // 모든 상품 조회
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        System.out.println("Service - findAllProducts 호출");
        // ✨ JpaRepository의 findAll() 사용! EntityManager 관련 코드 없음!
        List<Product> products = productRepository.findAll();
        System.out.println("Service - 조회된 상품 수: " + products.size());
        return products;
    }

    // 특정 가격 이하 상품 조회 (비즈니스 로직 + Repository 호출)
    @Transactional(readOnly = true)
    public List<Product> findProductsCheaperThan(Integer maxPrice) {
        System.out.println("Service - findProductsCheaperThan 호출: maxPrice = " + maxPrice);
        // 1. 모든 상품 조회 (Repository 위임)
        List<Product> allProducts = productRepository.findAll(); // ✨ 간결!

        // 2. 비즈니스 로직: 가격 필터링
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.getPrice() <= maxPrice)
                .toList();
        System.out.println("Service - 필터링 후 상품 수: " + filteredProducts.size());
        return filteredProducts;
    }

    // 상품 등록 (Create)
    @Transactional // 데이터 변경이므로 readOnly=false (기본값)
    public Product createProduct(Product newProduct) {
        System.out.println("Service - createProduct 호출: " + newProduct.getProductName());
        // ✨ JpaRepository의 save() 사용! EntityManager, 트랜잭션 코드 없음!
        // save() 메서드는 전달된 엔티티가 새로운 엔티티(ID가 null)이면 persist()를,
        // 이미 존재하는 엔티티(ID가 있음)이면 merge()를 내부적으로 호출합니다.
        Product savedProduct = productRepository.save(newProduct);
        System.out.println("Service - 저장된 Product: " + savedProduct);
        return savedProduct;
    }

    // 상품 수정 (Update) - 변경 감지 활용
    @Transactional
    public Product updateProduct(Integer productId, String newName, Integer newPrice) {
        System.out.println("Service - updateProduct 호출: ID = " + productId);
        // ✨ 1. 엔티티 조회 (영속 상태로 만듦) - findById 활용
        Product productToUpdate = findProductById(productId); // findById 내부에서 orElseThrow로 예외 처리됨

        // ✨ 2. 엔티티 상태 변경 (Setter 호출)
        System.out.println("Service - 기존 정보: " + productToUpdate);
        productToUpdate.setProductName(newName);
        productToUpdate.setPrice(newPrice);
        System.out.println("Service - 정보 변경됨: " + productToUpdate);

        // ✨ 3. 트랜잭션 종료 시 변경 감지(Dirty Checking)에 의해 자동 UPDATE
        // productRepository.save(productToUpdate); // 명시적으로 save 호출도 가능 (merge 동작)
        return productToUpdate;
    }

    // 상품 삭제 (Delete)
    @Transactional
    public void deleteProduct(Integer productId) {
        System.out.println("Service - deleteProduct 호출: ID = " + productId);
        // ✨ JpaRepository의 deleteById() 사용! EntityManager 관련 코드 없음!
        // (주의!) ID가 존재하지 않으면 예외 발생 가능성이 있으므로,
        // 필요 시 existsById() 등으로 확인 후 삭제하는 것이 더 안전할 수 있습니다.
        // productRepository.existsById(productId)
        productRepository.deleteById(productId);
        System.out.println("Service - 삭제 완료 (커밋 시 DB 반영)");
    }
}