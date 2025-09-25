package com.ohgiraffers.event.section01;

import com.ohgiraffers.event.entity.Certificate;
import com.ohgiraffers.event.section01.service.CourseCompletionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/*
 * 📌 도메인 간 결합이 높으면 생기는 문제
 * - 강한 결합: 강좌 수료 후 수료증을 직접 발급하면 `CourseCompletion`과 `Certificate` 도메인 간 의존성 증가.
 * - 유지보수 어려움: 상태 변경에 따른 후속 처리 로직이 서비스에 흩어져 수정 시 연쇄 영향 발생.
 * - 실생활 비유: 학생이 졸업했다고 교무처 직원이 직접 상장을 인쇄하고 총장 직인을 찍는 것과 같다. 교무처는 '졸업 처리'만 하고, '상장 발급'은 상장 담당 부서가 알아서 해야 한다.
 */
public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-lecture");
        EntityManager em = emf.createEntityManager();
        CourseCompletionService courseCompletionService = new CourseCompletionService(em);

        em.getTransaction().begin();
        courseCompletionService.completeCourse(1L);
        em.getTransaction().commit();

        System.out.println("=== 상태 확인 === ");
        Certificate certificate = em.find(Certificate.class, 1L);
        System.out.println(certificate);
        em.close();
        emf.close();
    }
}