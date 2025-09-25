
/*
 * =================================================================================
 * 🏆 도메인 이벤트 실습을 위한 LXP(Learning Experience Platform) 데이터베이스 초기화 스크립트
 * =================================================================================
 */

SET FOREIGN_KEY_CHECKS = 0;

-- 기존 테이블이 있다면 안전하게 삭제
DROP TABLE IF EXISTS certificates;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. Users: 사용자(학생) 테이블
CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '사용자 정보';

-- 2. Courses: 강좌 테이블
CREATE TABLE courses (
                         course_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255) NOT NULL
) COMMENT '강좌 정보';

-- 3. Enrollments: 수강 정보 테이블 (수강 진행 상태 포함)
CREATE TABLE enrollments (
                             enrollment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             course_id BIGINT NOT NULL,
                             status VARCHAR(50) DEFAULT 'IN_PROGRESS', -- 'IN_PROGRESS', 'COMPLETED'
                             completed_at DATETIME NULL,
                             UNIQUE KEY (user_id, course_id),
                             FOREIGN KEY (user_id) REFERENCES users(user_id),
                             FOREIGN KEY (course_id) REFERENCES courses(course_id)
) COMMENT '수강 정보';

-- 4. Certificates: 수료증 정보 테이블
CREATE TABLE certificates (
                              certificate_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              course_id BIGINT NOT NULL,
                              issue_date DATE NOT NULL,
                              FOREIGN KEY (user_id) REFERENCES users(user_id),
                              FOREIGN KEY (course_id) REFERENCES courses(course_id)
) COMMENT '수료증 정보';

-- =================================================================================
-- 🏆 실습을 위한 샘플 데이터 삽입
-- =================================================================================

INSERT INTO users (user_id, username, email) VALUES (101, '김수강', 'learner1@email.com');
INSERT INTO users (user_id, username, email) VALUES (102, '안결합', 'learner2@email.com');
INSERT INTO courses (course_id, title) VALUES (1, '자바 마스터 클래스');
-- 김수강 학생은 '자바 마스터 클래스'를 '진행 중' 상태로 수강 신청했습니다.
INSERT INTO enrollments (enrollment_id, user_id, course_id, status) VALUES (1, 101, 1, 'IN_PROGRESS');
INSERT INTO enrollments (enrollment_id, user_id, course_id, status) VALUES (2, 102, 1, 'IN_PROGRESS');