package com.ohgiraffers.event.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="certificates")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long certificateId;

    @Column(name = "user_id") // 변경점: 컬럼명 매핑
    private Long userId;

    @Column(name = "course_id") // 변경점: 컬럼명 매핑
    private Long courseId;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    protected Certificate() {}

    public Certificate(Long userId, Long courseId, LocalDate issueDate) {
        this.userId = userId; this.courseId = courseId; this.issueDate = issueDate;
    }

    public Long getCertificateId() { return certificateId; }

    @Override
    public String toString() {
        return "Certificate{" +
                "certificateId=" + certificateId +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", issueDate=" + issueDate +
                '}';
    }
}