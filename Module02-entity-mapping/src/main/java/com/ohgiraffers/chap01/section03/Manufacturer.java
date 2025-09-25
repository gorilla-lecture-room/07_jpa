package com.ohgiraffers.chap01.section03;

import jakarta.persistence.Embeddable;

import java.util.Objects;

/*
 * 📌 Manufacturer – 제조사 정보도 Embedded 타입으로 구성
 * ▶ name + country 조합을 한 덩어리로 관리하는 값 객체(Value Object)
 *
 * ------------------------------------------------------------------------
 * 🎯 [Value Object란?]
 * ------------------------------------------------------------------------
 * ✔ VO는 '값 자체가 객체'로서 의미를 가지는 설계 개념이다.
 * ✔ 식별자(ID)가 없고, 그 값 자체가 객체의 정체성을 정의한다.
 * ✔ 값이 같으면 같은 객체로 간주하며, 변경이 불가능한(Immutable) 구조로 설계된다.
 *
 * 예시)
 * - 주소(Address), 기간(Period), 이름(Name), 좌표(Coordinate) 등
 * - "서울특별시 종로구"라는 주소는 같은 값을 가진다면 동일한 의미로 판단한다.
 *
 * ------------------------------------------------------------------------
 * 🎯 [VO 설계 시 특징]
 * ------------------------------------------------------------------------
 * ✔ 불변성 보장: 필드를 final로 설정하고 setter 제거
 * ✔ 동등성 비교: equals()와 hashCode() 재정의 (값 기준 비교)
 * ✔ 상태 변경이 아닌 대체 방식 사용: 값이 바뀌면 새 객체를 생성
 * ✔ 도메인 내의 의미 있는 단위로 캡슐화
 * ------------------------------------------------------------------------
 */
@Embeddable
public class Manufacturer {
    private String name;
    private String country;

    protected Manufacturer() {
        this.name = null;
        this.country = null;
    }

    // 필수 속성은 생성자로 초기화
    public Manufacturer(String name, String country) {
        this.name = name;
        this.country = country;
    }

    // 📌 불변성을 위한 Getter만 제공
    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }


    // 📌 값 객체는 내용 기반으로 동등성을 비교해야 함
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manufacturer)) return false;
        Manufacturer that = (Manufacturer) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(country, that.country);
    }

    // name과 country를 기준으로 동일한 hash를 생성할 수 있도록 하여 동일성을 보장함.
    @Override
    public int hashCode() {
        return Objects.hash(name, country);
    }


    @Override
    public String toString() {
        return "Manufacturer{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
