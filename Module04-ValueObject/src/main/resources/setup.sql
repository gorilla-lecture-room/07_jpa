
SET FOREIGN_KEY_CHECKS =0;
-- 이전 모듈 테이블 삭제
DROP TABLE IF EXISTS deliveries;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;

DROP TABLE IF EXISTS hotel_reservation;
drop table if exists product_available_sizes_section01;
DROP TABLE IF EXISTS product_available_sizes;
DROP TABLE IF EXISTS products;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Hotel_Reservation 테이블: @Embedded 값 객체 실습 및 에러 수정
CREATE TABLE hotel_reservation
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_name       VARCHAR(255) COMMENT '오류 해결: guestName -> guest_name',
    room_number      VARCHAR(255),
    -- GuestCount 값 객체의 필드 (number_of_guests로 변경)
    number_of_guests INT,
    -- StayPeriod 값 객체의 필드
    check_in_date    DATE,
    check_out_date   DATE,
    room_rate        INT
) COMMENT '호텔 예약 정보';

-- 2. Products, Product_Available_Sizes 테이블: @ElementCollection 실습용
CREATE TABLE products
(
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255)
) COMMENT '상품 정보';


CREATE TABLE product_available_sizes_section01
(
    id             BIGINT      auto_increment comment "key",
    product_id     BIGINT       NOT NULL COMMENT '소유자 엔티티의 FK',
    label          VARCHAR(50)  NOT NULL,
    stock_quantity INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
) COMMENT '상품별 사이즈 정보';



CREATE TABLE product_available_sizes
(
    product_id     BIGINT       NOT NULL COMMENT '소유자 엔티티의 FK',
    label          VARCHAR(50)  NOT NULL,
    stock_quantity INT          NOT NULL,
    PRIMARY KEY (product_id, label),
    FOREIGN KEY (product_id) REFERENCES products (product_id) ON DELETE CASCADE
) COMMENT '상품별 사이즈 정보';

-- 샘플 데이터 삽입
INSERT INTO products (name) VALUES ('멋쟁이 티셔츠');
INSERT INTO product_available_sizes (product_id, label, stock_quantity) VALUES
                                                                            (1, 'S', 10), (1, 'M', 20), (1, 'L', 15);