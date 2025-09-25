package com.ohgiraffers.event.section02;

import com.ohgiraffers.event.entity.Certificate;
import com.ohgiraffers.event.section02.handler.CertificateIssuanceHandler;
import com.ohgiraffers.event.section02.infra.Events;
import com.ohgiraffers.event.section02.service.CourseCompletionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/*
 * [수정]
 * =====================================
 * 🏆 해결: 느슨한 결합(Loose Coupling)을 통한 유연한 시스템
 * =====================================
 *
 * 💡 동작 흐름:
 * 1. `main`에서 `Events`에 `CertificateIssuanceHandler`를 '구독 신청'합니다.
 * 2. `courseCompletionService.completeCourse(1L)`를 호출합니다.
 * 3. `CourseCompletionService`는 수강 상태를 변경한 후, `Events.raise(...)`를 통해 이벤트를 '발행'합니다.
 * 4. `Events`는 자신에게 구독 신청한 모든 핸들러(`CertificateIssuanceHandler`)에게 이벤트를 '전달'합니다.
 * 5. `CertificateIssuanceHandler`는 이벤트를 받고, 자신의 책임인 수료증 발급 로직을 '처리'합니다.
 *
 * ✨ 결과:
 * - `CourseCompletionService`는 더 이상 `Certificate`의 존재를 알지 못합니다.
 * - 나중에 "수료 시 이메일 발송" 기능이 추가되어도, `CourseCompletionService`는 단 한 줄도 수정할 필요 없이 새로운 핸들러만 추가하면 됩니다. 이것이 바로 '느슨한 결합'의 위력입니다.
 */
public class Application {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-lecture");
        EntityManager em = emf.createEntityManager();

        // 의존성 주입: 핸들러가 DB 작업을 할 수 있도록 EntityManager를 전달
        CertificateIssuanceHandler certificateHandler = new CertificateIssuanceHandler(em);
        Events.addHandler(certificateHandler);

        CourseCompletionService courseCompletionService = new CourseCompletionService(em);

        em.getTransaction().begin();
        courseCompletionService.completeCourse(2L);
        em.getTransaction().commit();

        Certificate certificate = em.find(Certificate.class, 2L);
        System.out.println(certificate);

        em.close();
        emf.close();
    }
}