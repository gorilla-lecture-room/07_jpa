package com.ohgiraffers.event.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollmentId;

    @Column(name = "user_id") // 변경점: 컬럼명 매핑
    private Long userId;

    @Column(name = "course_id") // 변경점: 컬럼명 매핑
    private Long courseId;

    @Column(name = "status")
    private String status;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    protected Enrollment() {}


    public void complete() {
        this.status = "COMPLETED";
        this.completedAt = LocalDateTime.now();
    }


    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", userId=" + userId +
                ", courseId=" + courseId +
                ", status='" + status + '\'' +
                ", completedAt=" + completedAt +
                '}';
    }
}