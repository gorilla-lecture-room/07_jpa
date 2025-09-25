package com.ohgiraffers.valueobject.mission.c_deep;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mission_recipe")
public class Recipe {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipeName;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Ingredient> ingredients = new ArrayList<>();

    protected Recipe() {}

    public Recipe(String recipeName) { this.recipeName = recipeName; }

    public List<Ingredient> getIngredients() { return ingredients; }
}