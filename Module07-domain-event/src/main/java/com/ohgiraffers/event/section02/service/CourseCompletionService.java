package com.ohgiraffers.event.section02.service;

import com.ohgiraffers.event.entity.Enrollment;
import com.ohgiraffers.event.section02.aggregate.CourseCompletedEvent;
import com.ohgiraffers.event.section02.infra.Events;
import jakarta.persistence.EntityManager;

public class CourseCompletionService {
    private final EntityManager em;
    public CourseCompletionService(EntityManager em) { this.em = em; }

    /*
     * 📌 개선된 설계:
     * - 이제 `CourseCompletionService`는 자신의 핵심 책임인 '수료 처리'에만 집중합니다.
     * - 수료 처리가 끝난 후에는 `Certificate` 도메인의 내부 사정을 전혀 알 필요 없이,
     * "CourseCompletedEvent"라는 '사건'이 발생했음을 세상에 알리기만 합니다. (이벤트 발행)
     * - 이로써 `CourseCompletion` 도메인과 `Certificate` 도메인 간의 강한 결합이 완전히 끊어졌습니다.
     */
    public void completeCourse(Long enrollmentId) {
        Enrollment enrollment = em.find(Enrollment.class, enrollmentId);
        System.out.println(enrollment);
        enrollment.complete();

        // "수강생(ID:userId)이 강좌(ID:courseId)를 수료했습니다!" 라는 사실을 방송합니다.
        Events.raise(new CourseCompletedEvent(enrollment.getUserId(), enrollment.getCourseId()));
    }
}
