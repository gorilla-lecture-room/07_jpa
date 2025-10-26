package com.ohgiraffers.springdatajpa.chap01.section02.repository;

import com.ohgiraffers.springdatajpa.common.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.List;


/*
 * [학습자 타이핑용 ✏️]
 *
 * 1. Repository Layer (데이터 접근 계층)
 * - 데이터베이스와의 통신을 전담하는 계층입니다.
 * - 데이터의 '영속성(Persistence)'을 담당합니다. (즉, 데이터를 영구 저장하고 조회)
 *
 * 2. EntityManager 사용
 * - JPA의 표준 핵심 객체인 'EntityManager'를 직접 주입받아 사용합니다.
 * - Spring Data JPA의 자동화 기능(예: JpaRepository)을 사용하지 않는 순수 JPA 구현 방식입니다.
 *
 * 3. @Repository 어노테이션
 * - (1) 이 클래스를 스프링 'Bean'으로 등록합니다. (IoC)
 * - (2) '예외 변환(Exception Translation)' 기능을 활성화합니다.
 * (JPA 예외 -> 스프링 DataAccessException)
 *
 * 4. 🚨 문제점: Boilerplate 코드
 * - EntityManager를 직접 사용하면, 트랜잭션 처리(begin, commit, rollback)나
 * 자원 획득/반납(create, close) 같은 반복적인 '보일러플레이트 코드'가 발생합니다.
 * - 이 문제는 다음 챕터에서 'Spring Data JPA'를 통해 해결합니다.
 */

/*
 * =====================================================================================
 * [교수자 설명용 👨‍🏫]
 * =====================================================================================
 *
 * 1. '데이터 영속성(Persistence)'을 담당하는 레이어
 * - [개념] '영속성'이란 데이터가 프로그램 종료 후에도 사라지지 않고 영구 보존되는 특성입니다.
 * 애플리케이션 메모리(RAM)에 존재하는 '객체(Object)'는 휘발성이지만,
 * 데이터베이스(Disk)에 저장된 '데이터(Record)'는 비휘발성(영속성)입니다.
 *
 * - [역할] '리포지토리 레이어'는 이 둘 사이의 변환을 책임집니다.
 * 즉, 자바 객체(Entity)를 DB 레코드로 변환해 저장(INSERT, UPDATE)하고,
 * DB 레코드를 자바 객체로 변환해 조회(SELECT)하는 모든 작업을 전담합니다.
 *
 * - [아키텍처] 이는 '계층형 아키텍처(Layered Architecture)'의 핵심 원칙입니다.
 * - Presentation (Controller): HTTP 요청/응답 처리
 * - Business (Service): 비즈니스 로직 처리
 * - Data Access (Repository): 데이터 영속성 처리
 * 각 계층은 자신의 책임에만 집중하며, '리포지토리'는 데이터 기술에 대한 상세 내용을
 * '서비스' 계층으로부터 숨기는 '추상화' 역할을 합니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 순수 JPA의 'EntityManager'를 사용하여 구현
 *
 * - [비교] 과거 JDBC 시절에는 개발자가 직접 Connection을 열고, SQL을 문자열로 작성하고,
 * ResultSet을 객체로 한 줄 한 줄 매핑했습니다.
 *
 * - [JPA] JPA(Java Persistence API)는 자바의 ORM(객체-관계 매핑) 표준 '명세'입니다.
 * (구현체는 주로 'Hibernate'를 사용합니다.)
 *
 * - [EntityManager] '엔티티 관리자'라는 뜻으로, JPA의 모든 핵심 동작을 수행하는 작업자입니다.
 * 개발자는 EntityManager API를 통해 DB 작업을 명령합니다.
 * - em.persist(entity): 영속성 컨텍스트에 저장 (INSERT SQL 준비)
 * - em.find(Entity.class, id): ID로 조회 (SELECT SQL 실행)
 * - em.remove(entity): 삭제 (DELETE SQL 준비)
 * - em.createQuery("..."): JPQL (객체지향 쿼리) 실행
 *
 * - [의의] Spring Data JPA가 이 'EntityManager'를 내부적으로 어떻게 다루는지 이해하기 위해
 * 이 섹션에서는 의도적으로 EntityManager를 직접 사용합니다.
 *
 * -------------------------------------------------------------------------------------
 *
 * 3. '@Repository' 어노테이션의 두 가지 핵심 기능
 *
 * 이 어노테이션은 단순한 이름표가 아닙니다. @Component의 기능을 포함하며, 추가로 중요한
 * 기능을 수행합니다.
 *
 * - (1) Spring 컴포넌트(Bean) 등록:
 * - 스프링 IoC(제어의 역전) 컨테이너가 이 클래스를 스캔하여 객체('Bean')로 생성하고 관리합니다.
 * - 따라서 다른 컴포넌트(예: Service)에서 @Autowired를 통해 이 리포지토리를
 * '의존성 주입(DI)' 받을 수 있습니다.
 *
 * - (2) 예외 변환 기능 활성화 (Exception Translation):
 * - [문제점] JPA(Hibernate)는 고유의 예외(예: PersistenceException, IllegalStateException)를
 * 발생시킵니다. 만약 서비스 계층이 이 예외를 직접 처리한다면,
 * 서비스 계층은 리포지토리 계층이 'JPA' 기술을 쓴다는 사실에 '종속'됩니다.
 * (기술이 바뀌면 서비스 코드도 바뀌어야 함 -> 강한 결합)
 *
 * - [해결책] @Repository가 붙으면, 스프링은 AOP(관점 지향 프로그래밍) 기반의
 * '예외 변환 프록시(Proxy)'를 이 Bean에 적용합니다.
 * 이 프록시는 JPA 예외가 발생하면 가로채서(intercept), 스프링이 정의한
 * 일관된 예외인 'DataAccessException' (런타임 예외)으로 '변환'하여 다시 던집니다.
 *
 * - [장점] 서비스 계층은 이제 JPA 예외를 몰라도 됩니다. 오직 스프링의 DataAccessException만
 * 알면 됩니다. (느슨한 결합, DIP 준수)
 *
 * -------------------------------------------------------------------------------------
 *
 * 4. 🚨 문제점: Boilerplate 코드 발생
 *
 * - [정의] '보일러플레이트 코드'란, 핵심 로직과는 관계없이 반복적으로 작성해야 하는
 * 상용구(관용구) 코드를 의미합니다.
 *
 * - [사례 1: 트랜잭션 처리]
 * JPA에서 모든 데이터 변경(C, U, D)은 '반드시' 트랜잭션 내에서 수행되어야 합니다.
 * EntityManager를 직접 사용하면, 이 트랜잭션 코드를 개발자가 직접 작성해야 합니다.
 *
 * EntityTransaction tx = em.getTransaction();
 * try {
 * tx.begin(); // (1) 트랜잭션 시작
 * // --- 핵심 로직 (예: em.persist(data)) ---
 * tx.commit(); // (2) 성공 시 커밋
 * } catch (Exception e) {
 * tx.rollback(); // (3) 실패 시 롤백
 * }
 *
 * - [사례 2: 자원 획득 및 반납]
 * (스프링이 아닌 순수 JPA 환경에서는) EntityManager를 직접 생성하고 닫아야 합니다.
 *
 * EntityManager em = emf.createEntityManager(); // (1) 획득
 * try {
 * // ... (위의 트랜잭션 로직) ...
 * } finally {
 * em.close(); // (2) 반드시 반납 (finally)
 * }
 *
 * - [결론] 이 'try-catch-finally'와 'begin-commit-rollback' 코드가 바로 보일러플레이트입니다.
 * Spring Data JPA는 이 모든 것을 자동으로 처리해 줍니다.
 * 이 섹션의 목적은 Spring Data JPA의 편리함을 체감하기 위한 빌드업입니다.
 */
@Repository // Spring Bean 등록 + JPA 예외를 Spring 예외로 변환
public class ProductRepository {

    /*
     * 💡 @PersistenceUnit vs @Autowired EntityManagerFactory
     * - @PersistenceUnit: JPA 표준 어노테이션. EntityManagerFactory를 직접 주입받습니다.
     * - @Autowired: Spring의 어노테이션. Spring 컨테이너가 관리하는 EntityManagerFactory Bean을 주입받습니다.
     * Spring 환경에서는 둘 다 사용 가능하며, 보통 @Autowired를 더 많이 사용합니다.
     */
    private final EntityManagerFactory emf;

    // 생성자 주입 (@Autowired 생략 가능)
    public ProductRepository(EntityManagerFactory emf) {
        System.out.println("ProductRepositoryEm 생성: EntityManagerFactory 주입됨 = " + emf.hashCode());
        this.emf = emf;
    }

    // 모든 상품 조회 메서드
    public List<Product> findAllProducts() {
        EntityManager em = emf.createEntityManager(); // EntityManager 획득

        try {
            System.out.println("Repository - findAllProducts 호출 (EntityManager)");
            // JPQL 실행
            List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
            System.out.println("Repository - 조회된 상품 수: " + products.size());
            return products;
        } catch (PersistenceException e){ // <- JPA의 고유한 에러 이것을 service로 전달하면 기술의 종속되는 문제가 발생함.
            // @Repository 어노테이션은 이러한 에러를 스프링이 정의한 에러로 변환해서 처리
            e.printStackTrace();
        } finally{
            em.close(); // EntityManager 반납 (🚨 중요!)
            System.out.println("Repository - findAllProducts 종료: EntityManager closed");
        }

        return null;
    }

    // ID로 상품 조회 메서드
    public Product findProductById(Integer productId) {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("Repository - findProductById 호출 (EntityManager): ID = " + productId);
            Product product = em.find(Product.class, productId);
            System.out.println("Repository - 조회된 Product: " + (product != null ? product.getProductName() : "없음"));
            return product;
        } finally {
            em.close();
            System.out.println("Repository - findProductById 종료: EntityManager closed");
        }
    }

    // 💡 참고: 상품 저장(save), 수정(update), 삭제(delete) 메서드는
    // 트랜잭션 처리가 필요하며, `OldStyleCourseDao`와 유사한 방식으로 구현할 수 있습니다.
    // 여기서는 조회 기능만 구현하여 레이어 분리에 집중합니다.
}