package com.ohgiraffers.jpql.mission.a_basic;


import com.ohgiraffers.jpql.mission.model.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;


public class BasicSolution {
    public void solve(EntityManager em) {
        System.out.println("\n--- 🥉 기초 미션 결과 ---");
        String jpql = "SELECT c FROM mission_courses c WHERE c.instructorId = :instructorId";
        TypedQuery<Course> query = em.createQuery(jpql, Course.class);
        query.setParameter("instructorId", 2);
        List<Course> courses = query.getResultList();
        courses.forEach(course -> System.out.println("강좌명: " + course.getTitle()));
    }
}
