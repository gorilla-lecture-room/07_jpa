package com.ohgiraffers.valueobject.mission.a_basic;

import jakarta.persistence.Embeddable;

@Embeddable
public class FullName {
    private String firstName;
    private String lastName;

    protected FullName() {}

    public FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}