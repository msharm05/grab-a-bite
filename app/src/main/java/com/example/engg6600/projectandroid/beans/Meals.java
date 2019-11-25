package com.example.engg6600.projectandroid.beans;

import java.io.Serializable;

/**
 * Created by ermil on 2017-11-16.
 */

public class Meals implements Serializable{
    private int maxCarb,minCarb,minProtein,maxProtein,minFat,maxFat,calories;
    private String mealName,mealLowerTime,mealTime;
    private String display1;
    private String display2;
    private String display3;

    public String getMealLowerTime() {
        return mealLowerTime;
    }

    public void setMealLowerTime(String mealLowerTime) {
        this.mealLowerTime = mealLowerTime;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public String displayContent()
    {
        display2 = "Carbs : "+getMinCarb()+"-"+getMaxCarb()+"g | "+"Proteins : "+getMinProtein()+"-"+getMaxProtein()+"g | "+"Fats : "+getMinFat()+"-"+getMaxFat()+"g";
        return display2;
    }

    public String displayCalories()
    {
        display3 = "Calories : "+getCalories();
        return display3;
    }

    public int getMaxCarb() {
        return maxCarb;
    }

    public void setMaxCarb(int maxCarb) {
        this.maxCarb = maxCarb;
    }

    public int getMinCarb() {
        return minCarb;
    }

    public void setMinCarb(int minCarb) {
        this.minCarb = minCarb;
    }

    public int getMinProtein() {
        return minProtein;
    }

    public void setMinProtein(int minProtein) {
        this.minProtein = minProtein;
    }

    public int getMaxProtein() {
        return maxProtein;
    }

    public void setMaxProtein(int maxProtein) {
        this.maxProtein = maxProtein;
    }

    public int getMinFat() {
        return minFat;
    }

    public void setMinFat(int minFat) {
        this.minFat = minFat;
    }

    public int getMaxFat() {
        return maxFat;
    }

    public void setMaxFat(int maxFat) {
        this.maxFat = maxFat;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
}
