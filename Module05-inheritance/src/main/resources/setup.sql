-- =================================================================================
-- 🏆 Module 05: 상속 관계 매핑 실습을 위한 데이터베이스 초기화 스크립트 (수정본)
-- =================================================================================

-- 기존 테이블이 있다면 안전하게 삭제 (의존성 역순 고려)
SET FOREIGN_KEY_CHECKS = 0;

-- Section 02: Single Table Strategy
DROP TABLE IF EXISTS products;

-- Section 03: Joined Strategy
DROP TABLE IF EXISTS electronic_products_joined;
DROP TABLE IF EXISTS clothing_products_joined;
DROP TABLE IF EXISTS food_products_joined;
DROP TABLE IF EXISTS products_joined;

-- Section 04: Table Per Class Strategy
DROP TABLE IF EXISTS electronic_products_tpc;
DROP TABLE IF EXISTS clothing_products_tpc;
DROP TABLE IF EXISTS food_products_tpc;
DROP TABLE IF EXISTS product_id_seq; -- Sequence simulation table for MySQL

-- Mission a_basic & c_deep: Payment (Joined Strategy based on c_deep)
DROP TABLE IF EXISTS card_payments;
DROP TABLE IF EXISTS bank_transfers;
DROP TABLE IF EXISTS payment_joined;

-- Mission b_middle: Content (Joined Strategy)
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS videos;
DROP TABLE IF EXISTS contents;
SET FOREIGN_KEY_CHECKS = 1;


-- =================================================================================
-- Section 02: 단일 테이블 전략 (Single Table Strategy) 테이블 생성
-- =================================================================================
CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
                          product_type VARCHAR(31) NOT NULL COMMENT '구분 컬럼 (DTYPE)',
    -- Common fields from Product
                          name VARCHAR(255),
                          price DOUBLE PRECISION,
                          brand VARCHAR(255),
                          stock_quantity INT,
    -- Fields from ElectronicProduct
                          warranty_period INT,
                          power_consumption VARCHAR(255),
    -- Fields from ClothingProduct
                          size VARCHAR(255),
                          material VARCHAR(255),
                          color VARCHAR(255),
    -- Fields from FoodProduct
                          expiration_date DATE,
                          is_organic BIT, -- MySQL uses BIT or TINYINT(1) for boolean
                          storage_instruction VARCHAR(255)
) COMMENT '상품 정보 (단일 테이블 전략)';

-- =================================================================================
-- Section 03: 조인 전략 (Joined Strategy) 테이블 생성 (⛔ 수정됨)
-- =================================================================================
CREATE TABLE products_joined (
                                 id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'PK (UNSIGNED로 수정)',
                                 product_type VARCHAR(31) NOT NULL COMMENT '구분 컬럼',
                                 name VARCHAR(255),
                                 price DOUBLE PRECISION,
                                 brand VARCHAR(255),
                                 stock_quantity INT
) COMMENT '상품 공통 정보 (조인 전략 부모)';

CREATE TABLE electronic_products_joined (
                                            id BIGINT UNSIGNED PRIMARY KEY COMMENT 'PK이자 FK (UNSIGNED로 수정)',
                                            warranty_period INT,
                                            power_consumption VARCHAR(255),
                                            FOREIGN KEY (id) REFERENCES products_joined(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '전자제품 정보 (조인 전략 자식)';

CREATE TABLE clothing_products_joined (
                                          id BIGINT UNSIGNED PRIMARY KEY COMMENT 'PK이자 FK (UNSIGNED로 수정)',
                                          size VARCHAR(255),
                                          material VARCHAR(255),
                                          color VARCHAR(255),
                                          FOREIGN KEY (id) REFERENCES products_joined(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '의류 정보 (조인 전략 자식)';

CREATE TABLE food_products_joined (
                                      id BIGINT UNSIGNED PRIMARY KEY COMMENT 'PK이자 FK (UNSIGNED로 수정)',
                                      expiration_date DATE,
                                      is_organic BIT,
                                      storage_instruction VARCHAR(255),
                                      FOREIGN KEY (id) REFERENCES products_joined(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '식품 정보 (조인 전략 자식)';

-- =================================================================================
-- Section 04: 클래스별 테이블 전략 (Table Per Class Strategy) 테이블 생성
-- =================================================================================
-- Sequence simulation table for MySQL (used by @SequenceGenerator)
CREATE TABLE product_id_seq (
                                next_val BIGINT
);
INSERT INTO product_id_seq VALUES ( 1 ); -- Initialize sequence

CREATE TABLE electronic_products_tpc (
                                         id BIGINT PRIMARY KEY COMMENT 'PK',
                                         name VARCHAR(255),
                                         price DOUBLE PRECISION,
                                         brand VARCHAR(255),
                                         stock_quantity INT,
                                         warranty_period INT,
                                         power_consumption VARCHAR(255)
) COMMENT '전자제품 정보 (클래스별 테이블 전략)';

CREATE TABLE clothing_products_tpc (
                                       id BIGINT PRIMARY KEY COMMENT 'PK',
                                       name VARCHAR(255),
                                       price DOUBLE PRECISION,
                                       brand VARCHAR(255),
                                       stock_quantity INT,
                                       size VARCHAR(255),
                                       material VARCHAR(255),
                                       color VARCHAR(255)
) COMMENT '의류 정보 (클래스별 테이블 전략)';

CREATE TABLE food_products_tpc (
                                   id BIGINT PRIMARY KEY COMMENT 'PK',
                                   name VARCHAR(255),
                                   price DOUBLE PRECISION,
                                   brand VARCHAR(255),
                                   stock_quantity INT,
                                   expiration_date DATE,
                                   is_organic BIT,
                                   storage_instruction VARCHAR(255)
) COMMENT '식품 정보 (클래스별 테이블 전략)';

-- =================================================================================
-- Mission a_basic & c_deep: Payment 테이블 생성 (Joined Strategy - c_deep 기준) (⛔ 수정됨)
-- =================================================================================
CREATE TABLE payment_joined (
                                id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'PK (UNSIGNED로 수정)',
                                payment_type VARCHAR(31) NOT NULL COMMENT '구분 컬럼',
                                amount INT NOT NULL
) COMMENT '결제 공통 정보 (조인 전략 부모)';

CREATE TABLE card_payments (
                               id BIGINT UNSIGNED PRIMARY KEY COMMENT 'PK이자 FK (UNSIGNED로 수정)',
                               card_company VARCHAR(255) NOT NULL,
                               card_type VARCHAR(255) NOT NULL,
                               card_number VARCHAR(255),
                               FOREIGN KEY (id) REFERENCES payment_joined(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '카드 결제 정보 (조인 전략 자식)';

CREATE TABLE bank_transfers (
                                id BIGINT UNSIGNED PRIMARY KEY COMMENT 'PK이자 FK (UNSIGNED로 수정)',
                                account_number VARCHAR(255) NOT NULL,
                                bank_name VARCHAR(255),
                                FOREIGN KEY (id) REFERENCES payment_joined(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '계좌 이체 정보 (조인 전략 자식)';

-- =================================================================================
-- Mission b_middle: Content 테이블 생성 (Joined Strategy) (⛔ 수정됨)
-- =================================================================================
CREATE TABLE contents (
                          id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'PK (UNSIGNED로 수정)',
                          content_type VARCHAR(31) NOT NULL COMMENT '구분 컬럼',
                          title VARCHAR(255)
) COMMENT '콘텐츠 공통 정보 (조인 전략 부모)';

CREATE TABLE posts (
                       id BIGINT UNSIGNED PRIMARY KEY COMMENT 'PK이자 FK (UNSIGNED로 수정)',
                       body TEXT,
                       FOREIGN KEY (id) REFERENCES contents(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '게시글 정보 (조인 전략 자식)';

CREATE TABLE videos (
                        id BIGINT UNSIGNED PRIMARY KEY COMMENT 'PK이자 FK (UNSIGNED로 수정)',
                        url VARCHAR(255),
                        FOREIGN KEY (id) REFERENCES contents(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT '영상 정보 (조인 전략 자식)';

-- =================================================================================
-- 🏆 데이터베이스 준비 완료!
-- =================================================================================