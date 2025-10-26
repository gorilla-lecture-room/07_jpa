package com.ohgiraffers.springdatajpa.chap02.section01.repository;

import com.ohgiraffers.springdatajpa.common.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/*
 * =====================================================================================
 * 🚀 Section 01: JpaRepository 인터페이스와 자동 구현
 * =====================================================================================
 */

/*
 * [학습자 타이핑용 ✏️]
 * 1. Repository 인터페이스: 데이터 접근의 '추상화'
 * - DB 작업의 상세 구현(SQL, JDBC)을 숨기고, '무엇을 할지(What)'만 정의한 명세입니다.
 * - 이 인터페이스는 '클래스'가 아닌 '인터페이스'로 정의합니다.
 *
 * 2. JpaRepository<T, ID> 상속
 * - Spring Data JPA가 제공하는 인터페이스를 상속받습니다.
 * - <T>: 관리할 '엔티티(Entity)' 클래스 (예: Product)
 * - <ID>: 해당 엔티티의 '@Id' 필드 '타입' (예: Integer, Long)
 *
 * 3. 💡 핵심: 자동 구현 (Auto-Implementation)
 * - 개발자는 이 인터페이스의 '구현 클래스(Impl)'를 만들지 않습니다.
 * - Spring Data JPA가 애플리케이션 시작 시점에 '자동으로' 구현 객체를 생성하여
 * 스프링 Bean으로 등록해 줍니다.
 *
 * 4. 기본 기능 즉시 사용
 * - JpaRepository를 상속받는 것만으로, 다음과 같은 핵심 메서드를 '무료로' 얻게 됩니다.
 * - save(T): C(저장), U(수정)
 * - findById(ID): R(단건 조회)
 * - findAll(): R(전체 조회)
 * - delete(T): D(삭제)
 * - (추가) 페이징(Pageable) 및 정렬(Sort) 기능
 *
 * 5. @Repository 어노테이션
 * - (1) 이 인터페이스가 데이터 접근 '컴포넌트(Bean)'임을 명시합니다.
 * - (2) Spring Data JPA의 스캔 대상이 되어 '자동 구현'을 활성화합니다.
 * - (3) JPA 예외를 스프링의 'DataAccessException'으로 '자동 변환'해줍니다.
 */

/*
 * =====================================================================================
 * [교수자 설명용 👨‍🏫]
 * =====================================================================================
 *
 * 1. 🏆 이것이 왜 혁명인가? (Boilerplate 코드의 완전한 제거)
 *
 * - [과거] Section 02에서 본 것처럼, 순수 JPA(EntityManager)를 사용하면
 * 개발자가 `ProductRepositoryImpl` 같은 '구현 클래스'를 직접 만들어야 했습니다.
 * - [과거의 문제] 그 구현 클래스 안에는 `em.persist()`, `em.find()`, `em.createQuery()` 등
 * EntityManager를 사용한 반복적이고 지루한 '보일러플레이트 코드'가 가득했습니다.
 *
 * - [현재 (Spring Data JPA)]
 * 이제 개발자는 '인터페이스'만 정의하면 끝입니다.
 * `save`, `findById` 같은 공통 CRUD 메서드 구현은 100% Spring Data JPA에 '위임'합니다.
 *
 * - [비유]
 * - 과거(EntityManager): 식당에서 '직접' 주방에 들어가('em' 획득),
 * 재료를 손질하고('em.persist'), 요리하고(트랜잭션 처리), 설거지('em.close')까지 했습니다.
 * - 현재(JpaRepository): 식당의 '키오스크(인터페이스)'에서 '주문(메서드 호출)'만 합니다.
 * 주방(Spring Data JPA)에서 요리(구현)가 자동으로 완료되어 나옵니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 🤔 어떻게 가능한가? (자동 구현의 내부 원리)
 *
 * 이 '자동 구현'은 Spring의 '프록시(Proxy)' 기술을 기반으로 동작합니다.
 *
 * 1. (시작) 애플리케이션이 시작되면, Spring Data JPA가 `@Repository`가 붙고
 * `JpaRepository`를 상속한 '인터페이스'들을 스캔합니다.
 *
 * 2. (프록시 생성) 해당 인터페이스(예: `ProductRepository`)를 기반으로,
 * Spring은 '가짜 구현 객체(Dynamic Proxy)'를 '메모리상'에 동적으로 생성합니다.
 * (실제 .java 파일이 생성되는 것이 아닙니다.)
 *
 * 3. (Bean 등록) 이 '프록시 객체'를 실제 구현체처럼 스프링 컨테이너에 'Bean'으로 등록합니다.
 *
 * 4. (실행) 서비스 계층에서 `productRepository.save(product)`를 호출하면,
 * 실제로는 이 '프록시 객체'의 `save` 메서드가 호출됩니다.
 *
 * 5. (위임) 프록시 객체는 내부적으로 'AOP(관점 지향 프로그래밍)'를 통해,
 * 호출된 메서드명(`save`)을 분석하여, 자신이 숨기고 있던 'EntityManager'를 꺼내
 * `em.persist(product)`라는 실제 JPA 로직을 '대신' 실행해 줍니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 3. `JpaRepository<Product, Integer>` (제네릭의 중요성)
 *
 * - `<T, ID>` 이 제네릭 타입은 Spring Data JPA의 프록시에게 매우 중요한 '정보'를 제공합니다.
 * - `<Product>`: "이 리포지토리는 'Product' 엔티티를 관리합니다.
 * `findAll()`을 호출하면 `SELECT p FROM Product p`를 실행해야 합니다."
 * - `<Integer>`: "Product 엔티티의 `@Id` 타입은 'Integer'입니다.
 * `findById(10)`이 호출되면, `em.find(Product.class, 10)`을 실행해야 합니다."
 *
 * - 만약 제네릭을 잘못 지정하면(예: `<Product, String>`), 프록시는 엉뚱한 타입으로
 * ID를 조회하려다 런타임에 심각한 오류를 발생시킵니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 4. 📌 @Repository 어노테이션의 심층적 역할
 *
 * "JpaRepository를 상속하면 어차피 스캔되는데, @Repository는 왜 붙이나요?"
 *
 * - (1) 명시적 컴포넌트 선언 (관례):
 * `@Service`, `@Controller`와 마찬가지로, 이 인터페이스가 '데이터 접근 계층'의
 * 컴포넌트(Bean)임을 명확히 드러내는 '관례적(Idiomatic)'인 표현입니다.
 * 가독성과 명시성을 높여줍니다. (기술적으로는 `@EnableJpaRepositories` 설정이
 * 스캔을 수행하므로 생략 가능할 때도 있지만, 붙이는 것을 권장합니다.)
 *
 * - (2) 예외 변환 (Exception Translation) ⭐️
 * 이 기능은 'Section 02(EntityManager)'에서 설명한 것과 동일하게,
 * Spring Data JPA가 생성한 '프록시 구현체'에도 적용됩니다.
 *
 * - [동작] `save()` 호출 시 DB에서 유니크 제약조건 위반(Unique Constraint Violation)이
 * 발생했다고 가정해 봅시다.
 * 1. Hibernate(JPA)가 `PersistenceException` (또는 하위 예외)를 던집니다.
 * 2. @Repository의 '예외 변환 AOP'가 이 예외를 가로챕니다.
 * 3. 이 예외를 분석하여, 스프링의 일관된 예외인 `DataIntegrityViolationException`
 * (DataAccessException의 하위 예외)으로 '변환'하여 다시 던집니다.
 *
 * - [장점] 서비스 계층(`@Service`)은 JPA나 Hibernate의 특정 예외를 알 필요가 없어집니다.
 * 오직 스프링의 `DataAccessException`만 처리하면 되므로,
 * 데이터 기술(JPA, JDBC, MyBatis 등)로부터 '완전히 분리(decoupled)'됩니다.
 */
@Repository("chap01-section01-repository")
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // ✨ 비어있어도 괜찮습니다! ✨
    // 기본적인 CRUD 메서드는 이미 JpaRepository 인터페이스에 모두 정의되어 있습니다.
    // - save(S entity): 엔티티 저장 (Insert or Update)
    // - findById(ID id): ID로 엔티티 조회 (Optional<T> 반환)
    // - findAll(): 모든 엔티티 조회 (List<T> 반환)
    // - deleteById(ID id): ID로 엔티티 삭제
    // - count(): 전체 엔티티 개수 조회
    // - existsById(ID id): 해당 ID의 엔티티 존재 여부 확인
    // ... 등등 (더 많은 메서드는 JpaRepository 문서를 참고하세요!)

    // 💡 만약 Product 엔티티에만 특화된 조회 메서드가 필요하다면?
    // -> Chap 03에서 배울 '쿼리 메소드' 또는 '@Query'를 사용하여 여기에 직접 선언할 수 있습니다.
    // 예: List<Product> findByProductNameContaining(String namePart);
}
