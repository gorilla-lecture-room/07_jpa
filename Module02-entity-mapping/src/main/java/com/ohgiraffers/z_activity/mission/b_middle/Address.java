package com.ohgiraffers.z_activity.mission.b_middle;


import jakarta.persistence.Embeddable;

@Embeddable // 값 타입을 정의하는 곳에 표시
public class Address {
    private String zipcode;
    private String address1;
    private String address2;

    protected Address() {}

    public Address(String zipcode, String address1, String address2) {
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
    }
}
