package com.ohgiraffers.jpql.chap01.section02;


import com.ohgiraffers.jpql.chap01.model.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.util.List;


/*
 * 🏆 2단계: 해답으로서의 JPQL - "객체의 언어로 질문하다"
 *
 * 💡 패러다임의 전환:
 * - (Before - SQL): `SELECT * FROM courses c WHERE c.price >= 300`
 * - (After - JPQL): `SELECT c FROM Course c WHERE c.price >= 300` 객체로서 조회 시작
 *
 * 겉보기엔 비슷하지만, 근본적으로 다릅니다.
 *
 * 1. 대상의 차이: JPQL의 `FROM Course c`는 `courses` 테이블이 아닌 `Course` **엔티티 객체**를 의미합니다. JPQL은 DB 테이블의 존재를 모릅니다. 오직 엔티티 객체의 세상만 알고 있습니다.
 * 2. 조건의 차이: `c.price`는 DB 컬럼명이 아니라 `Course` 엔티티의 **필드(속성)** 이름입니다.
 * 3. 결과의 차이: JPQL은 `Object[]`가 아닌, 타입이 명확한 `Course` **객체**를 반환합니다.
 *
 * "JPQL을 사용함으로써, 우리는 드디어 데이터베이스의 그늘에서 벗어나 온전히 객체지향적인 관점에서 데이터를 조회할 수 있게 되었습니다."
 */
public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-lecture");
        EntityManager em = emf.createEntityManager();

        // JPQL을 이용하는 방식
        String jpql = "SELECT c FROM Course c WHERE c.price >= 300";
        List<Course> courses = em.createQuery(jpql, Course.class).getResultList();
        System.out.println("============   단일 테이블 조회 ================");
        courses.forEach(course -> System.out.println(course.getTitle() + " - " + course.getPrice()));


        System.out.println("============   다중 테이블 조회  ================");
        String joinQuery = "SELECT c FROM Course c join c.lessons l WHERE c.price >= 300";

        courses = em.createQuery(joinQuery, Course.class).getResultList();
        for (Course course : courses) {
            System.out.println(course.getTitle() + " : " + course.getCourseId());
            course.getLessons().forEach(System.out::println);
            System.out.println();
            System.out.println();
        }

        /* 주석: 메모리 동작
         * - JPQL은 엔티티 객체를 대상으로 실행, 결과는 힙의 영속성 컨텍스트에 저장.
         * - 설계 이유: 객체 중심으로 데이터를 다루므로 코드 가독성과 유지보수성 향상.
         */
        em.close();
    }
}
