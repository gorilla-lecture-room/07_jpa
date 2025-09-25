package com.ohgiraffers.valueobject.chap02.section01;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


/*
 * 📌 Application 클래스
 * - Product와 AvailableSize 간의 관계를 @OneToMany로 설정했을 때 발생하는 문제점을 보여준다.
 */
/*
 * 🏆 1단계: 잘못된 선택 - "모든 것을 엔티티로 만들려는 유혹"
 *
 * 💡 문제 상황:
 * `Product`에 속한 사이즈 정보(`AvailableSize`)를 독립적인 생명주기를 가진 `@Entity`로 설계했습니다.
 * 이것은 RDB 관점에서는 정규화된 좋은 설계처럼 보일 수 있지만, 객체지향 관점에서는 재앙의 시작입니다.
 *
 * 💣 발생하는 문제들:
 * 1. 불필요한 복잡성: `AvailableSize`는 `Product` 없이는 존재 의미가 없는데도, 별도의 ID를 가진 독립 개체로 관리됩니다.
 *    이는 '자식'을 '손님'처럼 대하는 것과 같습니다.
 * 2. 값으로 비교 불가: 우리는 'M 사이즈'라는 '값'으로 재고를 찾고 싶지만, 엔티티는 'ID'로 식별됩니다.
 *    `new AvailableSize("M", 0)`는 기존의 'M 사이즈'와 전혀 다른 객체로 취급되어 `contains()`가 `false`를 반환합니다.
 * 3. 생명주기 불일치: `Product`를 저장할 때 `AvailableSize`도 함께 저장(`Cascade`)해야 하고,
 *    양방향 관계를 수동으로 맺어주는 등 번거로운 부가 작업이 너무 많습니다.
 *
 * 🤔 핵심 질문:
 * "과연 `AvailableSize`가 자신만의 고유한 식별자(ID)를 가져야 할 만큼 중요한 '독립적인 존재'일까?"
 * -> 이 질문에 '아니오'라고 답할 수 있다면, `@OneToMany`는 잘못된 선택입니다.
 */
public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-lecture");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        try {
            // 상품 엔티티 생성
            Product product1 = new Product("멋쟁이 티셔츠");

            // AvailableSize 엔티티 생성
            AvailableSize sizeS = new AvailableSize("S", 10);
            AvailableSize sizeM = new AvailableSize("M", 20);
            AvailableSize sizeL = new AvailableSize("L", 15);

            // 연관 관계 설정 및 추가
            product1.addAvailableSize(sizeS);
            product1.addAvailableSize(sizeM);
            product1.addAvailableSize(sizeL);

            // 영속화: Product를 영속화하면 CascadeType.ALL 설정에 따라 AvailableSize도 함께 영속화됨
            em.persist(product1);

            /*
             * ❌ 문제점 1: 불필요한 엔티티 관리로 인한 오버헤드
             * - AvailableSize는 Product에 종속적인 데이터로, 독립적인 생명주기를 가질 필요가 없음.
             * - 하지만 @Entity로 정의되어 JPA가 이를 독립적인 엔티티로 관리.
             * - 단순한 값 변경(재고 감소)에도 JPA의 변경 추적(Dirty Checking)이 발생하여 불필요한 오버헤드 초래.
             */
            product1.decreaseStock("M", 5); // dirty checking
            System.out.println("티셔츠 M 사이즈 재고 감소 후: " + product1.getAvailableSizes());

            /*
             * ❌ 문제점 2: 동등성 비교의 어려움 (참조 기반 비교)
             * - AvailableSize가 엔티티로 관리되므로, JPA는 참조 기반으로 객체를 비교.
             * - 동일한 라벨("M")을 가진 새로운 AvailableSize 객체를 생성하더라도,
             *   컬렉션에서 이를 동일한 객체로 인식하지 않음.
             * - 이는 JPA에서 엔티티를 식별하는 기준은 ID가 되기 때문에 값만으로 비교하지 않음.
             */
            AvailableSize searchM = new AvailableSize("M", 0);
            boolean containsM = product1.getAvailableSizes().contains(searchM); //31번 라인에 추가되어 있음
            System.out.println("티셔츠에 M 사이즈가 포함되어 있는가 (참조 비교)? " + containsM); // false

            /*
             * ❌ 문제점 3: 컬렉션 조작의 번거로움
             * - "M" 사이즈를 제거하려면, 컬렉션 내의 정확한 AvailableSize 인스턴스를 알아야 함.
             * - 값 객체로 설계하면 단순히 라벨("M")을 기준으로 제거 가능.
             * - 또한, 양방향 연관관계를 관리해야 하므로 removeAvailableSize 메서드에서
             *   AvailableSize의 product 필드를 null로 설정하는 추가 작업 필요.
             */
            // test 1 : 아래의 경우 지워지지 않는다.
//            product1.removeAvailableSize(searchM);
            // test 2 : 아래와 같이 정확한 인스턴스를 제공해야 삭제가 된다.
//            product1.removeAvailableSize(sizeM);
            System.out.println("티셔츠에서 M 사이즈 제거 후 사이즈 목록: " + product1.getAvailableSizes());

            /*
             * 새로운 "M" 사이즈 추가
             * - 동일한 라벨("M")을 가진 사이즈를 다시 추가 가능.
             * - 하지만 @Entity로 관리되므로, 동일한 라벨을 가진 데이터가 중복으로 저장됨.
             * - 값 객체로 설계하면 라벨을 기준으로 중복을 방지할 수 있음.
             */
            AvailableSize newSizeM = new AvailableSize("L", 10);
            product1.addAvailableSize(newSizeM);
            System.out.println("티셔츠에 L 사이즈 추가 후 사이즈 목록: " + product1.getAvailableSizes());

            /*
             * ❌ 문제점 4: 양방향 연관관계 관리의 복잡성
             * - @OneToMany와 @ManyToOne으로 양방향 연관관계를 설정해야 하며,
             *   addAvailableSize와 removeAvailableSize 메서드에서 연관관계를 일관되게 유지해야 함.
             * - 이는 코드 복잡성을 증가시키며, 실수로 연관관계가 끊어질 가능성 있음.
             */

            em.getTransaction().commit();

            /*
             * 📌 개선 방안: @ElementCollection으로 변경
             * - AvailableSize를 @Embeddable로 설계하고, @ElementCollection으로 관리하면:
             *   1. 도메인적으로 더 적합: AvailableSize가 Product의 일부로 관리됨.
             *   2. 불필요한 엔티티 관리 제거: JPA의 변경 추적 오버헤드 감소.
             *   3. 값 기반 비교 가능: 라벨을 기준으로 동등성 비교 가능.
             *   4. 컬렉션 조작 간소화: 라벨만으로 추가/제거 가능.
             *   5. 양방향 연관관계 불필요: 단방향 관계로 관리 가능.
             */

        } catch (IllegalArgumentException e) {
            System.err.println("유효하지 않은 값입니다: " + e.getMessage());
            em.getTransaction().rollback();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}