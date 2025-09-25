package com.ohgiraffers.valueobject.mission.c_deep;

import jakarta.persistence.Embeddable;

@Embeddable
public class Ingredient {
    private String name;
    private double quantity;
    private String unit;

    protected Ingredient() {}

    public Ingredient(String name, double quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }
}