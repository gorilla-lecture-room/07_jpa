package com.ohgiraffers.event.section01.service;

import com.ohgiraffers.event.entity.Certificate;
import com.ohgiraffers.event.entity.Enrollment;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;

/*
 * 📌 CourseCompletionService: 강좌 수료 처리 서비스
 * - 역할: 특정 수강 정보를 '수료' 상태로 변경하고, 관련된 후속 조치를 처리.
 */
public class CourseCompletionService {
    private final EntityManager em;
    public CourseCompletionService(EntityManager em) { this.em = em; }

    /*
     * 📌 잘못된 설계: 수료 처리 후, 수료증을 '직접' 발급하는 메서드
     * - `CourseCompletionService`가 강좌를 수료 처리하는 것은 본인의 책임이 맞습니다.
     * - 하지만, 수료증(`Certificate`)을 발급하는 로직까지 직접 알고 있는 것은 '월권'입니다.
     *
     * 💣 문제점 (강한 결합, Tight Coupling):
     * 1. 책임 불일치: `CourseCompletion` 도메인이 `Certificate` 도메인의 생성 방법을 알아야 합니다. 만약 수료증 발급 정책(예: 발급자 이름 추가)이 변경되면, 엉뚱하게 `CourseCompletionService`를 수정해야 합니다.
     * 2. 확장성 저해: "강좌를 수료하면 이메일도 보내고, 뱃지도 부여해줘" 라는 요구사항이 추가될 때마다 이 메서드는 점점 더 비대해지고 복잡해질 것입니다.
     */
    public void completeCourse(Long enrollmentId) {
        Enrollment enrollment = em.find(Enrollment.class, enrollmentId);
        enrollment.complete();

        System.out.println("수료증 발급 로직 실행...");
        Certificate certificate = new Certificate(enrollment.getUserId(), enrollment.getCourseId(), LocalDate.now());
        em.persist(certificate);
        System.out.println("수료증이 직접 발급되었습니다. Certificate ID: " + certificate.getCertificateId());
    }
}