package com.ohgiraffers.springdatajpa.chap01.section02.service;

import com.ohgiraffers.springdatajpa.chap01.section02.repository.ProductRepository;
import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 잊지 말고 Spring의 Transactional 임포트!

import java.util.List;

/*
 * =====================================================================================
 * 💼 Section 02: Service Layer 구현
 * =====================================================================================
 */

/*
 * [학습자 타이핑용 ✏️]
 *
 * 1. Service Layer (비즈니스 계층)
 * - 애플리케이션의 '핵심 비즈니스 로직'을 처리합니다.
 * - '트랜잭션(Transaction)'을 관리하는 단위입니다.
 *
 * 2. 책임과 위임
 * - HTTP 요청/응답 처리는 Controller에게, 데이터 영속성 처리는 Repository에게 '위임'합니다.
 * - 서비스는 이 둘을 '조율(Orchestration)'하는 역할을 합니다.
 *
 * 3. @Service 어노테이션
 * - 이 클래스를 스프링 'Bean'으로 등록합니다. (@Component의 특수화)
 *
 * 4. @Transactional 어노테이션
 * - 메서드가 포함된 '트랜잭션' 경계를 설정합니다.
 * - 메서드 시작 시 트랜잭션을 시작(begin)하고,
 * - 성공적으로 종료되면 커밋(commit), 예외 발생 시 롤백(rollback)을 자동으로 수행합니다.
 *
 * 5. @Transactional(readOnly = true)
 * - '조회 전용' 트랜잭션임을 명시하여 성능을 최적화합니다.
 * - JPA(Hibernate)가 불필요한 스냅샷 생성 및 변경 감지(Dirty Checking)를 생략합니다.
 * - 🚨 주의: 데이터 변경(C/U/D)이 있는 메서드에는 절대 사용하면 안 됩니다.
 */

/*
 * =====================================================================================
 * [교수자 설명용 👨‍🏫]
 * =====================================================================================
 *
 * 1. 핵심 비즈니스 로직 처리
 * - [개념] '비즈니스 로직'이란, 이 애플리케이션이 해결하고자 하는 '고유한 업무 규칙'을 의미합니다.
 * (예: "회원가입 시, 1000 포인트를 지급한다.", "주문 시, 재고가 있는지 확인한다.")
 *
 * - [위치] 이 로직은 Controller(표현 계층)나 Repository(데이터 계층)가 아닌,
 * Service(비즈니스 계층)에 위치해야 합니다. (관심사의 분리, Separation of Concerns)
 *
 * - [조율(Orchestration)]
 * 서비스는 종종 여러 리포지토리를 조율하여 하나의 비즈니스 로직을 완성합니다.
 * (예: '주문하기' 서비스)
 * 1. UserRepository에서 회원 정보 조회 (조회)
 * 2. ProductRepository에서 상품 재고 확인 (조회)
 * 3. ProductRepository에 상품 재고 감소 (변경)
 * 4. OrderRepository에 주문 정보 저장 (변경)
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 트랜잭션(Transaction) 관리
 * - [개념] 트랜잭션이란 '모두 성공하거나 모두 실패해야 하는' 하나의 논리적인 작업 단위입니다.
 * (예: 은행 이체 = A 계좌 출금 + B 계좌 입금)
 *
 * - [ACID] 트랜잭션은 4가지 특성(ACID)을 보장해야 합니다.
 * - Atomicity (원자성): All or Nothing. (위 1~4번 작업이 모두 성공하거나 모두 실패)
 * - Consistency (일관성): 트랜잭션 후에도 DB 상태가 일관되어야 함.
 * - Isolation (고립성): 여러 트랜잭션이 동시에 실행될 때 서로 영향을 주지 않아야 함.
 * - Durability (지속성): 성공한 트랜잭션은 영구적으로 저장됨.
 *
 * - [AOP 기반 동작] @Transactional은 스프링의 AOP(관점 지향 프로그래밍)를 통해 '프록시' 방식으로 동작합니다.
 * 스프링은 이 어노테이션이 붙은 메서드를 감싸는 가짜(프록시) 객체를 만듭니다.
 *
 * - (메서드 호출 전): 프록시가 트랜잭션을 시작 (em.getTransaction().begin())
 * - (메서드 실행): ... (개발자가 작성한 비즈니스 로직 실행) ...
 * - (성공적 종료): 프록시가 트랜잭션을 커밋 (tx.commit())
 * - (예외 발생): 프록시가 트랜잭션을 롤백 (tx.rollback())
 *
 * - [장점] 개발자는 더 이상 Repository에서처럼 'try-catch'나 'begin/commit/rollback' 같은
 * 보일러플레이트 코드를 작성할 필요가 없습니다. 오직 '비즈니스 로직'에만 집중할 수 있습니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 3. @Transactional(readOnly = true) 성능 최적화
 * 이것은 JPA(Hibernate)의 동작 원리와 깊은 관련이 있으며, '조회' 메서드에 필수적인 옵션입니다.
 *
 * - [Default (readOnly=false)의 동작]
 * 1. (조회) JPA가 DB에서 엔티티를 조회하면, '1차 캐시(영속성 컨텍스트)'에 저장합니다.
 * 2. (스냅샷 생성) 동시에, 해당 엔티티의 '최초 상태 스냅샷'을 별도로 복사해 둡니다.
 * 3. (트랜잭션 커밋 시) '변경 감지(Dirty Checking)'를 수행합니다.
 * 1차 캐시의 현재 엔티티와 스냅샷을 비교합니다.
 * 4. (변경 시) 만약 다르다면(예: `entity.setName(...)` 호출), JPA가 '자동으로' UPDATE SQL을
 * 생성하여 DB에 전송합니다.
 *
 * - [readOnly = true 설정 시]
 * 개발자가 스프링(JPA)에게 "이 트랜잭션은 데이터를 절대 변경하지 않을 것"이라고 알려줍니다.
 * 따라서 JPA는 다음과 같은 비효율적인 작업을 '모두 생략'합니다.
 *
 * 1. (스냅샷 생성 X): 애초에 스냅샷을 만들지 않습니다. (메모리 절약)
 * 2. (변경 감지 X): 커밋 시점에 Dirty Checking을 수행하지 않습니다. (CPU 연산 절약)
 *
 * - [결론] 단순히 데이터를 '조회'만 하는 `findById()`, `findAll()` 같은 메서드에는
 * `readOnly = true`를 붙이는 것이 성능 최적화의 기본입니다.
 *
 * - [🚨 주의] 만약 `readOnly = true`가 붙은 메서드에서 엔티티의 상태를 변경하는 코드가
 * (예: `entity.setName(...)`) 있어도, JPA는 변경 감지를 하지 않기 때문에
 * 'UPDATE 쿼리가 실행되지 않고 묵살'됩니다. (혹은 예외 발생)
 * 따라서 C/U/D 작업에는 절대 이 옵션을 사용하면 안 됩니다.
 */
@Service // 비즈니스 로직 담당 컴포넌트
public class ProductService {

    private final ProductRepository productRepository; // Repository 계층 의존

    // 생성자 주입 (@Autowired 생략 가능)
    @Autowired
    public ProductService(ProductRepository productRepository) {
        System.out.println("ProductService 생성: ProductRepositoryEm 주입됨");
        this.productRepository = productRepository;
    }

    // 특정 가격 이하 상품 조회 비즈니스 로직
    @Transactional(readOnly = true) // 조회만 하므로 readOnly=true 설정
    public List<Product> findProductsCheaperThan(Integer maxPrice) {
        System.out.println("Service - findProductsCheaperThan 호출: maxPrice = " + maxPrice);
        // 1. 데이터 조회는 Repository에게 위임
        List<Product> allProducts = productRepository.findAllProducts();

        // 2. 핵심 비즈니스 로직: 가격 필터링
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.getPrice() <= maxPrice)
                .toList(); // Java 16+
        System.out.println("Service - 필터링 후 상품 수: " + filteredProducts.size());

        return filteredProducts; // 필터링된 엔티티 목록 반환
    }

    // ID로 상품 조회 (단순히 Repository 호출 위임)
    @Transactional(readOnly = true)
    public Product findProductById(Integer productId) {
        System.out.println("Service - findProductById 호출: ID = " + productId);
        return productRepository.findProductById(productId);
    }

    // 💡 참고: 상품 등록, 수정, 삭제 등의 비즈니스 로직도 여기에 구현합니다.
    // 예: 상품 등록 시 유효성 검증, 중복 체크 등
    // @Transactional // 데이터 변경이 있으므로 readOnly=false (기본값)
    // public Product createProduct(Product newProduct) { ... productRepository.save(newProduct); ... }
}