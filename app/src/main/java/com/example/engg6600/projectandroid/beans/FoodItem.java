package com.example.engg6600.projectandroid.beans;

import java.io.Serializable;

/**
 * Created by ermil on 2017-11-16.
 */

public class FoodItem implements Serializable{
    private int itemCarbs,itemProteins,itemFats,itemCalories,result,mealIndex;
    private String itemtime,item_contents,item_result;
    private Meals meal;

    public FoodItem()
    {}

    public String getItem_result() {
        return item_result;
    }

    public void setItem_result(String item_result) {
        this.item_result = item_result;
    }

    public FoodItem(int itemCarbs, int itemProteins, int itemFats, String item_contents, String itemtime){
        this.itemCarbs = itemCarbs;
        this.itemProteins = itemProteins;
        this.itemFats = itemFats;
        this.itemtime = itemtime;
        this.item_contents = item_contents;
    }

    public void setItemCalories(int itemCalories) {
        this.itemCalories = itemCalories;
    }

    public String getItem_contents() {
        return item_contents;
    }

    public void setItem_contents(String item_contents) {
        this.item_contents = item_contents;
    }

    public int getMealIndex() {
        return mealIndex;
    }

    public void setMealIndex(int mealIndex) {
        this.mealIndex = mealIndex;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Meals getMeal() {
        return meal;
    }

    public void setMeal(Meals meal) {
        this.meal = meal;
    }

    public int getItemCarbs() {
        return itemCarbs;
    }

    public void setItemCarbs(int itemCarbs) {
        this.itemCarbs = itemCarbs;
    }

    public int getItemProteins() {
        return itemProteins;
    }

    public void setItemProteins(int itemProteins) {
        this.itemProteins = itemProteins;
    }

    public int getItemFats() {
        return itemFats;
    }

    public void setItemFats(int itemFats) {
        this.itemFats = itemFats;
    }

    public int getItemCalories() {
        return itemCalories;
    }

    public void setItemCalories() {
        this.itemCalories = ((4*itemCarbs) + (4*itemProteins) + (9*itemFats));
    }

    public String getItemtime() {
        return itemtime;
    }

    public void setItemtime(String itemtime) {
        this.itemtime = itemtime;
    }
}
