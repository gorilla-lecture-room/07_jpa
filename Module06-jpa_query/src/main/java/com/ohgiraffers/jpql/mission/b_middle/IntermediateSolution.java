package com.ohgiraffers.jpql.mission.b_middle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class IntermediateSolution {
    public void solve(EntityManager em) {
        System.out.println("\n--- 🥈 중급 미션 결과 ---");
        // mission_courses 엔티티에 lessons 연관관계가 없으므로 JOIN을 위해 추가해야 합니다.
        // 여기서는 JPQL 문법 자체에 집중하기 위해 외부 조인을 사용합니다.
        String jpql = "SELECT new com.ohgiraffers.jpql.mission.b_middle.CourseInfoDTO(c.title, COUNT(l), c.price) " +
                "FROM mission_courses c LEFT JOIN mission_lessons l ON c = l.course " +
                "GROUP BY c.courseId, c.title, c.price";
        TypedQuery<CourseInfoDTO> query = em.createQuery(jpql, CourseInfoDTO.class);
        List<CourseInfoDTO> result = query.getResultList();
        result.forEach(System.out::println);
    }
}
