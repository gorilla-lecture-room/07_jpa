package com.ohgiraffers.event.section02.aggregate;

// 💡 이벤트 객체는 발생한 과거의 '사실'을 담는 불변 객체(Immutable Object)로 설계하는 것이 좋습니다.
public class CourseCompletedEvent {
    private final Long userId;
    private final Long courseId;

    public CourseCompletedEvent(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Long getUserId() { return userId; }
    public Long getCourseId() { return courseId; }
}