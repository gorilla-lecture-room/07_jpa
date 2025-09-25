package com.ohgiraffers.event.section02.handler;

import com.ohgiraffers.event.entity.Certificate;
import com.ohgiraffers.event.section02.aggregate.CourseCompletedEvent;
import com.ohgiraffers.event.section02.infra.EventHandler;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

/*
 * 📌 CertificateIssuanceHandler: 수료증 발급 책임자
 * - 이 핸들러는 오직 `CourseCompletedEvent`에만 관심을 가집니다.
 * - 이벤트가 발생했다는 소식을 들으면, 자신의 책임인 '수료증 발급' 로직을 묵묵히 수행합니다.
 * - `CourseCompletionService`가 어떻게 동작하는지는 전혀 알 필요가 없습니다.
 */
public class CertificateIssuanceHandler implements EventHandler<CourseCompletedEvent> {
    private final EntityManager em;
    public CertificateIssuanceHandler(EntityManager em) { this.em = em; }

    @Override
    public void handle(CourseCompletedEvent event) {
        System.out.println("이벤트 수신: 수료증 발급을 시작합니다. UserID: " + event.getUserId());
        Certificate certificate = new Certificate(event.getUserId(), event.getCourseId(), LocalDate.now());
        em.persist(certificate);
        System.out.println("수료증 발급 완료. Certificate ID: " + certificate.getCertificateId());
    }
}