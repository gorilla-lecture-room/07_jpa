package com.ohgiraffers.jpql.chap01.section01;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

/*
 * 🏆 1단계: 문제 직면하기 - "객체 세상에 불쑥 찾아온 SQL"
 *
 * 💡 문제 상황:
 * 우리는 분명 객체지향적으로 코드를 짜고 있었는데, 데이터를 조회하려니 갑자기 `SELECT * FROM courses` 라는 SQL이 등장했습니다.
 * 이것은 심각한 '패러다임 불일치'입니다.
 *
 * 💣 Native SQL의 문제점:
 * 1. 깨져버린 추상화: `Course` 객체만 알고 있으면 됐는데, 갑자기 `courses`라는 DB 테이블 이름과 그 안의 컬럼 구조까지 알아야 합니다. 객체지향의 캡슐화가 무너집니다.
 * 2. 특정 DB에 종속: 만약 다른 데이터베이스로 교체했는데, 특정 DB에서만 동작하는 SQL 함수를 사용했다면? 모든 쿼리를 수정해야 하는 재앙이 발생합니다.
 * 3. 타입 안전성 부재: 쿼리 결과는 `Object[]` 배열입니다. `row[0]`이 `course_id`인지, `row[1]`이 `title`인지 컴파일 시점에는 전혀 알 수 없어 런타임 오류에 매우 취약합니다.
 *
 * "객체는 객체답게, 객체의 언어로 대해야 합니다. SQL은 객체의 언어가 아닙니다."
 */
/* SQL에 의존한 방식 */
public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-lecture");
        EntityManager em = emf.createEntityManager();

        // Native SQL 사용
        String sql = "SELECT * FROM courses WHERE price >= 300";
        // 객체지향적이지 않고 테이블명/컬럼명에 직접적인 의존으로 테이블 구조 변경시 코드 수정 발생
        List<Object[]> result = em.createNativeQuery(sql).getResultList();
        System.out.println(result.size());


        // 결과 처리: 타입 안전성 없음, 인덱스로 접근
        for (Object[] row : result) {
            // 인덱스 참조시 정확하다는 보장이 없음.
            System.out.println("Course ID: " + row[0] + ", Title: " + row[1]);
        }

        em.close();
        emf.close();
    }
}
