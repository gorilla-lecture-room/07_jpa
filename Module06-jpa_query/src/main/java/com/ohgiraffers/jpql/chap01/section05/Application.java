package com.ohgiraffers.jpql.chap01.section05;

import com.ohgiraffers.jpql.chap01.section05.dto.CourseDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

/*
 * 🏆 3단계: DTO 프로젝션 - "원하는 정보만 골라서 듣기"
 *
 * 💡 문제 상황:
 * `SELECT c FROM Course c` 처럼 엔티티 전체를 조회하는 것은, 간단한 보고서를 받으려고 책 한 권을 통째로 빌리는 것과 같습니다.
 * 화면에 필요한 것은 '강좌 이름'과 '소속된 강의 수' 뿐인데, `Course` 엔티티의 모든 정보를 메모리에 올리는 것은 낭비입니다.
 *
 * 🤔 해결 아이디어:
 * "처음부터 내가 원하는 데이터만 담을 수 있는 전용 가방(DTO)을 만들어서 달라고 하면 되지 않을까?"
 * -> 이것이 바로 `new` 키워드를 사용한 DTO 프로젝션입니다.
 *
 * 💡 DTO 프로젝션의 본질
 * - `SELECT new com. ... .CourseDTO(...)`: "Course 엔티티의 모든 정보는 필요 없고,
 * `courseId`, `title`, `COUNT(l)` 세 가지 재료만 써서 `CourseDTO`라는 새로운 객체를 만들어서 줘!" 라는 의미입니다.
 * - 이는 성능 최적화(필요한 컬럼만 조회)뿐만 아니라, 조회 계층과 도메인 모델 계층을 명확하게 분리하는 매우 중요한 설계 패턴입니다.
 */
public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-lecture");
        EntityManager em = emf.createEntityManager();


        String jpql = " SELECT new com.ohgiraffers.jpql.chap01.section05.dto.CourseDTO(c.courseId, c.title, COUNT(l))" +
                  " FROM Course c JOIN c.lessons l  GROUP BY c.courseId, c.title" +
                " HAVING COUNT(l) > :cnt";

        TypedQuery<CourseDTO> query = em.createQuery(jpql, CourseDTO.class);
        query.setParameter("cnt", 5);

        List<CourseDTO> values = query.getResultList();
        values.forEach(System.out::println);
    }
}
