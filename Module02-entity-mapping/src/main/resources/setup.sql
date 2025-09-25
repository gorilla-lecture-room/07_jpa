
SHOW tables;
SET FOREIGN_KEY_CHECKS = 0;
-- 기존 테이블이 있다면 안전하게 삭제
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Roles 테이블: Enum 매핑을 위한 역할 정보
CREATE TABLE roles
(
    role_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(20) NOT NULL UNIQUE
) COMMENT '역할 정보 (Enum 매핑용)';

-- 2. Users 테이블: 기본 엔티티 및 @Embedded 값 객체 실습용
CREATE TABLE users
(
    user_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(100) NOT NULL UNIQUE,
    email         VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    birth_date    DATE,
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    role_id       VARCHAR(20)  NOT NULL COMMENT '@Enumerated(EnumType.STRING)용',
    -- 주소(Address) 값 객체를 위한 컬럼들
    zipcode       VARCHAR(20),
    address1      VARCHAR(255),
    address2      VARCHAR(255)
) COMMENT '사용자 정보 (기본 엔티티)';

-- 3. Products 테이블: 여러 @Embedded 값 객체 실습용
CREATE TABLE products
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                 VARCHAR(255),
    -- 가격(Money) 값 객체를 위한 컬럼들
    price_amount         DECIMAL(10, 2),
    price_currency       VARCHAR(10),
    -- 제조사(Manufacturer) 값 객체를 위한 컬럼들
    manufacturer_name    VARCHAR(255),
    manufacturer_country VARCHAR(255)
) COMMENT '상품 정보 (@Embedded 실습용)';


-- 샘플 데이터 삽입
INSERT INTO roles (role_name) VALUES ('STUDENT'), ('INSTRUCTOR'), ('ADMIN');

INSERT INTO users (username, email, password_hash, birth_date, role_id, zipcode, address1, address2) VALUES
                                                                                                         ('앨리스', 'alice@example.com', 'hashed_pw_1', '1990-01-15', 'STUDENT', '12345', '서울시 강남구', '테헤란로 123'),
                                                                                                         ('밥', 'bob@example.com', 'hashed_pw_2', '1992-05-23', 'INSTRUCTOR', '54321', '경기도 성남시', '분당구 판교역로');

INSERT INTO products (name, price_amount, price_currency, manufacturer_name, manufacturer_country) VALUES
                                                                                                       ('스마트폰', 799.99, 'USD', '삼성전자', '대한민국'),
                                                                                                       ('노트북', 1299.99, 'USD', 'LG전자', '대한민국');
