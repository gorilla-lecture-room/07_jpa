package com.ohgiraffers.chap01.section03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;


// 진행 전 setup.sql 파일 실행 후 사용
// value object 내용을 잠시 다루고 깊은 설명과 이해는 Module04에서 진행한다.
public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf =  Persistence.createEntityManagerFactory("jpa-lecture");
        EntityManager em = emf.createEntityManager();

        //  조회 조회
        selectProduct(em);
        // 등록
        insertProduct(em);

        // 수정
        updateProduct(em);
        em.close();
        emf.close();

    }

    public static void selectProduct(EntityManager em){
        Product user = em.getReference(Product.class, 1);
        System.out.println(user);
    }

    public static void insertProduct(EntityManager em){
        em.getTransaction().begin();

        Manufacturer samsung = new Manufacturer("Samsung", "Korea");
        Money price = new Money(new BigDecimal("999.99"), "USD");
        Product product = new Product("Galaxy S25", price, samsung);
        em.persist(product);
        System.out.println(product);
        em.getTransaction().commit();
    }


    /*
     * =================================================================================
     * 📌 JPA 변경 감지(Dirty Checking)와 값 객체(Value Object)의 교체 원리
     * =================================================================================
     *
     * 1. 변경 감지 (Dirty Checking) 란?
     * - `em.find()`로 조회된 엔티티는 영속성 컨텍스트(Persistence Context)에 의해 관리되는 '영속 상태(Managed State)'가 됩니다.
     * - 이 때, JPA는 해당 엔티티의 최초 상태를 복사한 '스냅샷(Snapshot)'을 별도로 저장해 둡니다.
     * - 트랜잭션이 `commit()`되는 시점에, JPA는 관리 중인 엔티티의 현재 상태와 이 스냅샷을 비교합니다.
     * - 만약 두 상태가 다르다면(Dirty), JPA가 자동으로 `UPDATE` SQL을 생성하여 데이터베이스에 변경 사항을 반영합니다.
     * - 이 기능 덕분에 개발자는 `em.update()`와 같은 메서드를 명시적으로 호출할 필요가 없습니다.
     *
     * 2. 값 객체의 교체 (Value Object Replacement)
     * - `Money`, `Manufacturer`와 같은 값 객체는 보통 불변(Immutable)하게 설계하는 것이 모범 사례입니다.
     * - 불변 객체의 값을 '변경'해야 할 때는, 기존 객체의 내부 상태를 바꾸는 것이 아니라
     * 새로운 값을 가진 객체를 `new` 키워드로 생성하여 필드 참조 자체를 완전히 '교체'하는 방식을 사용합니다.
     * - 이 코드에서 `hashCode()`가 변경되는 이유는 바로 이 '교체' 때문입니다. `foundProduct`는
     * 완전히 새로운 `Money`와 `Manufacturer` 객체를 참조하게 되므로 해시코드가 다른 것은 당연하고 올바른 결과입니다.
     *
     * ✨ 결론: 이 코드는 값 객체를 새로운 인스턴스로 교체함으로써 영속 엔티티의 상태를 변경하고,
     * JPA의 변경 감지 기능이 이를 스냅샷과 비교하여 자동으로 감지한 뒤 데이터베이스를 업데이트하는
     * JPA의 핵심 동작 방식을 보여주는 매우 좋은 예제입니다.
     */
    public static void updateProduct(EntityManager em){
        em.getTransaction().begin();
        Product foundProduct = em.find(Product.class, 1);
        System.out.println("✅ 초기 상태: " + foundProduct);
        System.out.println("초기 가격의 hash : " + foundProduct.getPrice().hashCode());
        System.out.println("초기 제조사의 hash : " + foundProduct.getManufacturer().hashCode());


        // 📌 가격 변경 (값 객체 교체 방식)
        Money newPrice = new Money(new BigDecimal("899.99"), "USD");
        foundProduct.changePrice(newPrice);

        // 📌 제조사 변경 (값 객체 교체 방식)
        Manufacturer newManufacturer = new Manufacturer("LG", "Korea");
        foundProduct.changeManufacturer(newManufacturer);

        em.getTransaction().commit();

        // 📌 최종 상태 확인
        System.out.println("✅ 변경 후 상태: " + foundProduct);
        System.out.println("변경 후 가격 hash : " + foundProduct.getPrice().hashCode());
        System.out.println("변경 후 제조사 hash : " + foundProduct.getManufacturer().hashCode());
    }
}
