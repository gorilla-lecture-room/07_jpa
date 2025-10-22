-- 파일명: Module05_Setup.sql

-- =================================================================================
-- 🏆 Module 5: 상속관계 매핑을 위한 데이터베이스 (JOINED 전략 기준)
-- =================================================================================

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS hotel_reservation;
DROP TABLE IF EXISTS product_available_sizes_section01;
DROP TABLE IF EXISTS product_available_sizes;
DROP TABLE IF EXISTS products;

-- 현 모듈 테이블 재생성 (의존성 역순)
DROP TABLE IF EXISTS electronic_products_joined;
DROP TABLE IF EXISTS clothing_products_joined;
DROP TABLE IF EXISTS food_products_joined;
DROP TABLE IF EXISTS products_joined;
DROP TABLE IF EXISTS id_sequences;
DROP TABLE IF EXISTS products;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. SINGLE_TABLE 전략용 테이블
CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          product_type VARCHAR(31) NOT NULL,
                          name VARCHAR(255),
                          price DOUBLE,
                          brand VARCHAR(255),
                          stock_quantity INT,
    -- 자식 속성들
                          warranty_period INT,
                          power_consumption VARCHAR(50),
                          size VARCHAR(50),
                          material VARCHAR(100),
                          color VARCHAR(50),
                          expiration_date DATE,
                          is_organic BOOLEAN,
                          storage_instruction VARCHAR(255)
) COMMENT '상품 통합 정보 (SINGLE_TABLE)';

-- 2. JOINED 전략용 테이블
CREATE TABLE products_joined (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 product_type VARCHAR(31) NOT NULL,
                                 name VARCHAR(255),
                                 price DOUBLE,
                                 brand VARCHAR(255),
                                 stock_quantity INT
) COMMENT '상품 공통 정보 (JOINED 부모)';

CREATE TABLE electronic_products_joined ( id BIGINT PRIMARY KEY, warranty_period INT, power_consumption VARCHAR(50), FOREIGN KEY (id) REFERENCES products_joined(id) ON DELETE CASCADE );
CREATE TABLE clothing_products_joined ( id BIGINT PRIMARY KEY, size VARCHAR(50), material VARCHAR(100), color VARCHAR(50), FOREIGN KEY (id) REFERENCES products_joined(id) ON DELETE CASCADE );
CREATE TABLE food_products_joined ( id BIGINT PRIMARY KEY, expiration_date DATE, is_organic BOOLEAN, storage_instruction VARCHAR(255), FOREIGN KEY (id) REFERENCES products_joined(id) ON DELETE CASCADE );

-- 3. TABLE_PER_CLASS 전략의 키 생성을 위한 시퀀스 테이블
CREATE TABLE id_sequences ( sequence_name VARCHAR(255) PRIMARY KEY, next_val BIGINT );
INSERT INTO id_sequences(sequence_name, next_val) VALUES ('product_id_seq', 1);

-- 샘플 데이터 삽입
-- SINGLE_TABLE용
INSERT INTO products (product_type, name, price, brand, stock_quantity, warranty_period, power_consumption) VALUES ('ELECTRONIC', 'Laptop', 999.99, 'TechBrand', 50, 24, '65W');
-- JOINED용
INSERT INTO products_joined (id, product_type, name, price, brand, stock_quantity) VALUES (1, 'ELECTRONIC', 'Laptop', 999.99, 'TechBrand', 50);
INSERT INTO electronic_products_joined (id, warranty_period, power_consumption) VALUES (1, 24, '65W');



