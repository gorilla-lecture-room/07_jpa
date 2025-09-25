package com.ohgiraffers.jpql.mission.b_middle;

public class CourseInfoDTO {
    private String title;
    private Long lessonCount;
    private double price;

    public CourseInfoDTO(String title, Long lessonCount, double price) {
        this.title = title;
        this.lessonCount = lessonCount;
        this.price = price;
    }

    @Override
    public String toString() {
        return "CourseInfoDTO{" +
                "title='" + title + '\'' +
                ", lessonCount=" + lessonCount +
                ", price=" + price +
                '}';
    }
}
