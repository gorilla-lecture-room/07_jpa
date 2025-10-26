package com.ohgiraffers.springdatajpa.chap01.section01;

import com.ohgiraffers.springdatajpa.common.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/*
 * =====================================================================================
 * 🍝 Section 01: 문제 직면 - 뒤섞인 책임과 코드의 스파게티
 * =====================================================================================
 *
 * 1. 왜 필요할까? (필요성)
 * 웹 애플리케이션을 처음 만들 때, 우리는 종종 모든 로직을 한 곳에 모아두는 실수를 합니다.
 * 예를 들어, 사용자의 HTTP 요청을 받는 부분(Controller 역할)에서 데이터베이스 조회 로직(Repository 역할)과
 * 핵심 비즈니스 로직(Service 역할)까지 전부 처리하는 것이죠.
 *
 * 이렇게 하면 처음에는 간단해 보일 수 있지만, 애플리케이션 규모가 커지면 금방 '스파게티 코드'가 되어버립니다.
 * 스파게티 면처럼 코드가 서로 얽히고설켜서 어디서부터 손대야 할지, 어떤 코드가 어떤 역할을 하는지 파악하기 매우 어려워집니다.
 *
 * 🤔 모든 것을 한 곳에서 처리하는 것이 정말 최선일까요? 코드의 역할 분담은 왜 중요할까요?
 *
 * -------------------------------------------------------------------------------------
 *
 * 2. 문제 체감하기: 역할 분담 없는 코드의 혼란
 * 간단한 '상품 목록 조회 및 필터링' 기능을 모든 로직이 뒤섞인 방식으로 구현하면 어떤 모습일지 살펴봅시다.
 * 아래 코드를 직접 작성하고 실행해보면서 문제점을 느껴보세요.
 *
 * 💣 스파게티 코드의 문제점
 * 1. 책임의 혼재: 웹 요청 처리(@GetMapping), 비즈니스 로직(가격 필터링), 데이터 접근(EntityManager 사용)이 한 클래스에 뒤섞여 있습니다.
 * 2. 테스트 어려움: 이 Controller 클래스만으로는 가격 필터링 로직을 테스트하기 어렵습니다. DB 연결과 웹 요청 처리가 얽혀있기 때문입니다.
 * 3. 낮은 재사용성: 가격 필터링 로직이 다른 Controller나 Service에서 필요하다면? 코드를 복붙해야 할까요?
 * 4. 유지보수 어려움: 상품 조회 방식을 바꾸거나(SQL/JPQL 변경), 필터링 기준을 바꾸거나(비즈니스 로직 변경), 응답 형식을 바꾸려면(JSON -> XML?) 이 클래스 하나만 계속 건드려야 합니다.
 * 5. 낮은 응집도, 높은 결합도: 관련 없는 책임들이 한 클래스에 모여 코드가 복잡하고, `EntityManagerFactory`와 직접 결합되어 유연성이 떨어집니다.
 * =====================================================================================
 */
@Controller // 🚨 주의: 실제로는 이렇게 작성하면 안 됩니다! 역할 분리 예시를 위한 안티 패턴입니다.
public class SpaghettiProductController {

    private final EntityManagerFactory emf; // 데이터 접근을 위해 EntityManagerFactory 직접 주입 (🚨 안티 패턴)

    // Spring 컨테이너가 EntityManagerFactory Bean을 자동으로 주입
    @Autowired
    public SpaghettiProductController(EntityManagerFactory emf) {
        System.out.println("SpaghettiProductController 생성: EntityManagerFactory 주입됨");
        this.emf = emf;
    }

    /*
    * 실행 후 생각해보기:
    * 만약 필터링 조건을 가격 외에 다른 것(e.g., 상품명 키워드)으로 추가하려면 이 코드를 어떻게 수정해야 할까요?
    * 데이터베이스 대신 다른 저장소(e.g., 외부 API)에서 상품 정보를 가져오려면 코드를 얼마나 바꿔야 할까요?
    * findCheapProductNames 메서드의 가격 필터링 로직만 따로 테스트할 수 있을까요?
    * 이런 질문들에 답하기 어렵다면, 스파게티 코드의 문제점을 체감한 것입니다!
    * */

    // (가정) GET /spaghetti/products/cheap?maxPrice=10000 요청 처리
    @GetMapping("/spaghetti/products/cheap") // 웹 요청 매핑
    @ResponseBody // 결과를 HTTP 응답 본문에 직접 작성 (JSON 변환)
    public List<String> findCheapProductNames(@RequestParam("maxPrice") Integer maxPrice) { // 요청 파라미터 받기
        System.out.println("\nSpaghetti - findCheapProductNames 호출: maxPrice = " + maxPrice);



        // --- 🏛️ 데이터 접근 로직 (원래 Repository 역할) ---
        EntityManager em = emf.createEntityManager(); // EntityManager 획득 (매번 생성)
        List<Product> allProducts;
        try {
            System.out.println("Spaghetti - DB에서 모든 상품 조회 시도...");
            // JPQL 실행
            allProducts = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
            System.out.println("Spaghetti - DB 조회 완료, 총 상품 수: " + allProducts.size());
        } finally {
            em.close(); // EntityManager 반납 (반드시!)
            System.out.println("Spaghetti - EntityManager closed.");
        }


        // --- 💼 비즈니스 로직 (원래 Service 역할) ---
        System.out.println("Spaghetti - 가격 필터링 로직 수행...");
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.getPrice() <= maxPrice) // 가격 비교
                .toList();
        System.out.println("Spaghetti - 필터링 후 상품 수: " + filteredProducts.size());




        // --- 🎮 결과 가공 및 응답 (원래 Controller 역할) ---
        System.out.println("Spaghetti - 결과 가공 (상품명 추출)...");
        List<String> productNames = filteredProducts.stream()
                .map(Product::getProductName) // 이름만 추출
                .collect(Collectors.toList());

        System.out.println("Spaghetti - 최종 응답 데이터: " + productNames);



        return productNames; // 상품명 리스트 반환 (JSON 등으로 변환되어 응답)
    }

    // 💡 참고: 실제 Spring Boot 애플리케이션에서는 애플리케이션 종료 시
    // EntityManagerFactory가 자동으로 관리/종료됩니다.
}