package com.example.laba1;

import java.io.Serializable;

public class Recipe implements Serializable {
    private String name;
    private String ingredients;
    private int difficulty;
    private int time;
    private int calorie;

    public Recipe(String name, String ingredients, int difficulty, int time, int calorie) {
        this.name = name;
        this.ingredients = ingredients;
        this.difficulty = difficulty;
        this.time = time;
        this.calorie = calorie;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getTime() {
        return time;
    }

    public int getCalorie() {
        return calorie;
    }
}
