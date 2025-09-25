
-- =================================================================================
-- 🏆 JPQL 실습을 위한 LMS 데이터베이스 초기화 스크립트
-- =================================================================================

-- 기존 테이블이 있다면 안전하게 삭제
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS lessons;
DROP TABLE IF EXISTS course_categories;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- 1. Roles: 역할 테이블 (강사, 학생 등)
CREATE TABLE roles (
                       role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL UNIQUE
) COMMENT '역할 정보';

-- 2. Users: 사용자 테이블 (강사 및 학생 정보 통합)
CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role_id BIGINT,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (role_id) REFERENCES roles(role_id)
) COMMENT '사용자 정보';

-- 3. Categories: 강좌 카테고리 테이블
CREATE TABLE categories (
                            category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL UNIQUE
) COMMENT '강좌 카테고리';

-- 4. Courses: 강좌 테이블
CREATE TABLE courses (
                         course_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         instructor_id BIGINT NOT NULL,
                         price DECIMAL(10, 2) DEFAULT 0.00,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (instructor_id) REFERENCES users(user_id)
) COMMENT '강좌 정보';

-- 5. Course_Categories: 강좌와 카테고리 연결 테이블 (다대다 관계)
CREATE TABLE course_categories (
                                   course_id BIGINT,
                                   category_id BIGINT,
                                   PRIMARY KEY (course_id, category_id),
                                   FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
                                   FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
) COMMENT '강좌-카테고리 매핑';

-- 6. Lessons: 개별 강의 테이블
CREATE TABLE lessons (
                         lesson_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         course_id BIGINT NOT NULL,
                         title VARCHAR(255) NOT NULL,
                         content TEXT,
                         video_url VARCHAR(500),
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
) COMMENT '강의 정보';

-- 7. Enrollments: 수강 신청 정보 테이블
CREATE TABLE enrollments (
                             enrollment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             course_id BIGINT NOT NULL,
                             enrolled_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             status ENUM('active', 'completed', 'canceled') DEFAULT 'active',
                             FOREIGN KEY (user_id) REFERENCES users(user_id),
                             FOREIGN KEY (course_id) REFERENCES courses(course_id)
) COMMENT '수강 신청 정보';


-- =================================================================================
-- 🏆 JPQL 실습을 위한 샘플 데이터 삽입
-- =================================================================================

-- 1. 역할 데이터 삽입
INSERT INTO roles (role_id, role_name) VALUES (1, 'ADMIN'), (2, 'INSTRUCTOR'), (3, 'STUDENT');

-- 2. 사용자 데이터 삽입 (강사 2명, 학생 10명)
INSERT INTO users (user_id, username, email, password_hash, role_id) VALUES
                                                                         (1, '김강사', 'instructor1@email.com', 'hashed_pass', 2),
                                                                         (2, '박선생', 'instructor2@email.com', 'hashed_pass', 2),
                                                                         (101, '이학생', 'student1@email.com', 'hashed_pass', 3),
                                                                         (102, '최수강', 'student2@email.com', 'hashed_pass', 3),
                                                                         (103, '오공부', 'student3@email.com', 'hashed_pass', 3),
                                                                         (104, '정열심', 'student4@email.com', 'hashed_pass', 3),
                                                                         (105, '한지혜', 'student5@email.com', 'hashed_pass', 3),
                                                                         (106, '유능한', 'student6@email.com', 'hashed_pass', 3),
                                                                         (107, '고민중', 'student7@email.com', 'hashed_pass', 3),
                                                                         (108, '나합격', 'student8@email.com', 'hashed_pass', 3),
                                                                         (109, '조은하루', 'student9@email.com', 'hashed_pass', 3),
                                                                         (110, '홍길동', 'student10@email.com', 'hashed_pass', 3);

-- 3. 카테고리 데이터 삽입
INSERT INTO categories (category_id, name) VALUES (1, '웹 개발'), (2, '데이터 과학'), (3, '알고리즘'), (4, '네트워크');

-- 4. 강좌 데이터 삽입
INSERT INTO courses (course_id, title, description, instructor_id, price) VALUES
                                                                              (1, '웹 개발 완전 정복', 'HTML, CSS, JavaScript, React를 다룹니다.', 1, 350.00),
                                                                              (2, '머신러닝 입문', 'Python과 Scikit-learn을 사용한 머신러닝 기초.', 2, 400.00),
                                                                              (3, '알고리즘과 자료구조', '코딩 테스트를 위한 필수 알고리즘을 배웁니다.', 2, 300.00),
                                                                              (4, '네트워크 기초', 'TCP/IP와 OSI 7계층을 이해합니다.', 1, 250.00),
                                                                              (5, 'JPA 프로그래밍', '객체지향과 RDB의 패러다임 불일치를 해결합니다.', 2, 380.00);

-- 5. 강좌-카테고리 매핑 데이터 삽입
INSERT INTO course_categories (course_id, category_id) VALUES (1, 1), (2, 2), (3, 3), (4, 4), (5, 1);

-- 6. 강의 데이터 삽입 (COUNT 실습을 위해 충분한 양)
INSERT INTO lessons (course_id, title) VALUES
                                           (1, '1강 - HTML 기초'), (1, '2강 - CSS 심화'), (1, '3강 - JavaScript 기본 문법'), (1, '4강 - React 컴포넌트'), (1, '5강 - State와 Props'),
                                           (2, '1강 - 머신러닝이란?'), (2, '2강 - 선형 회귀'), (2, '3강 - 로지스틱 회귀'), (2, '4강 - 결정 트리'), (2, '5강 - 서포트 벡터 머신'), (2, '6강 - K-최근접 이웃'),
                                           (3, '1강 - 시간 복잡도'), (3, '2강 - 스택과 큐'), (3, '3강 - 정렬 알고리즘'), (3, '4강 - 이진 탐색'),
                                           (4, '1강 - 네트워크의 역사'), (4, '2강 - TCP와 UDP'),
                                           (5, '1강 - JPA 시작하기'), (5, '2강 - 영속성 컨텍스트'), (5, '3강 - 엔티티 매핑'), (5, '4강 - 연관관계 매핑'), (5, '5강 - JPQL'), (5, '6강 - QueryDSL');


-- 7. 수강 신청 데이터 삽입 (고급 미션 조건을 만족하도록 설정)
-- '웹 개발 완전 정복'(course_id=1) 강좌에 6명의 학생 등록
INSERT INTO enrollments (user_id, course_id, status) VALUES
                                                         (101, 1, 'active'), (102, 1, 'active'), (103, 1, 'active'), (104, 1, 'completed'), (105, 1, 'canceled'), (106, 1, 'active'), (107, 1, 'active'), (108, 1, 'active'),
-- '머신러닝 입문'(course_id=2) 강좌에 4명 등록
                                                         (101, 2, 'active'), (103, 2, 'completed'), (105, 2, 'active'), (107, 2, 'active'),
-- 기타 강좌 등록
                                                         (102, 3, 'active'), (104, 3, 'active'), (106, 4, 'completed'), (108, 5, 'active'), (109, 5, 'active'), (110, 5, 'canceled');

-- =================================================================================
-- 🏆 데이터베이스 준비 완료!
-- =================================================================================
