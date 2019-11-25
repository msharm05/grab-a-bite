package com.example.engg6600.projectandroid.services;

import com.example.engg6600.projectandroid.beans.FoodItem;
import com.example.engg6600.projectandroid.beans.Meals;
import com.example.engg6600.projectandroid.beans.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ermil on 2017-11-16.
 */

public class ComparisonServices {
    private User user;
    private FoodItem foodItem;

    public ComparisonServices(){}

    public ComparisonServices(User user, FoodItem foodItem)
    {
        this.user = user;
        this.foodItem = foodItem;
    }

    public Meals getMeal(int index)
    {
        List<Meals> listMeals = null;
        DietPlannerServices dietPlannerServices = new DietPlannerServices(user);
        listMeals = dietPlannerServices.createMealPlan();
        Meals meal = listMeals.get(index);
        return meal;
    }

    public boolean isTimeWithinInterval(String lwrLimit, String uprLimit, String time){
        Date time_1 = null;
        Date time_2 = null;
        Date d = null;
        try {
            time_1 = new SimpleDateFormat("hh:mm a").parse(lwrLimit);
            Calendar calendar_1 = Calendar.getInstance();
            calendar_1.setTime(time_1);

            time_2 = new SimpleDateFormat("hh:mm a").parse(uprLimit);
            Calendar calendar_2 = Calendar.getInstance();
            calendar_2.setTime(time_2);

            d = new SimpleDateFormat("hh:mm a").parse(time);
            Calendar calendar_3 = Calendar.getInstance();
            calendar_3.setTime(d);

            Date x = calendar_3.getTime();
            if (x.after(calendar_1.getTime()) && x.before(calendar_2.getTime())) {
                return true;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return false;
    }

    public FoodItem compareFoodItem()
    {
        int result=0;
        String lowertime,uppertime = null;
        int a=foodItem.getItemCarbs();
        int b =foodItem.getItemProteins();
        int c = foodItem.getItemFats();
        String time = foodItem.getItemtime();
        foodItem.setItemCalories();
        foodItem.setResult(5);
        int carbCheck=0, proteinCheck=0 , fatCheck=0, carbCheck1=0, proteinCheck1=0 , fatCheck1=0;
        Meals meal=null;
        for(int i=0;i<5;i++)
        {
            meal = getMeal(i);
            lowertime = meal.getMealLowerTime();
            uppertime = meal.getMealTime();
            carbCheck = (int)(meal.getMaxCarb()*0.50);
            proteinCheck = (int)(meal.getMaxProtein()*0.50);
            fatCheck = (int)(meal.getMaxFat()*0.50);
            carbCheck1 =(int)(meal.getMaxCarb()*0.70);
            proteinCheck1 = (int)(meal.getMaxProtein()*0.70);
            fatCheck1 =(int)(meal.getMaxFat()*0.70);
            if(isTimeWithinInterval(lowertime,uppertime,time))
            {
                if( a > meal.getMaxCarb())
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(3);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Big NO ! Look for something with Carbohydrates less than "+String.valueOf(carbCheck1)+"g");
                    return foodItem;
                }
                else if( b > meal.getMaxProtein())
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(3);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Big NO ! Look for something with Proteins less than "+String.valueOf(proteinCheck1)+"g");
                    return foodItem;
                }
                else if( c > meal.getMaxFat())
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(3);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Big NO ! Look for something with Fats less than "+String.valueOf(fatCheck1)+"g");
                    return foodItem;
                }
                else if((a < carbCheck) && (b < proteinCheck) && (c < fatCheck))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(1);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Grab-A-Bite!!");
                    return foodItem;

                }
                else if((a > carbCheck) && (b < proteinCheck) && (c < fatCheck) && (a < carbCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(1);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Grab-A-Bite !!");
                    return foodItem;

                }
                else if((a < carbCheck) && (b > proteinCheck) && (c < fatCheck) && (b < proteinCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(1);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Grab-A-Bite !!");
                    return foodItem;

                }
                else if((a < carbCheck) && (b < proteinCheck) && (c > fatCheck) && (c<fatCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(1);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Grab-A-Bite !!");
                    return foodItem;

                }
                else if((a > carbCheck1) && (b < proteinCheck) && (c < fatCheck))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(2);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("You can make a better choice ! Look for something with Carbohydrates less than "+String.valueOf(carbCheck1)+"g");
                    return foodItem;

                }
                else if((a < carbCheck) && (b > proteinCheck1) && (c < fatCheck))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(2);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("You can make a better choice ! Look for something with Proteins less than "+String.valueOf(proteinCheck1)+"g");
                    return foodItem;

                }
                else if((a < carbCheck) && (b < proteinCheck) && (c > fatCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(2);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("You can make a better choice ! Look for something with Fats less than "+String.valueOf(fatCheck1)+"g");
                    return foodItem;

                }
                else if ((a < carbCheck1) && (b < proteinCheck1) && (c < fatCheck1) && (a > carbCheck) && (b > proteinCheck) && (c > fatCheck))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(1);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Grab-A-Bite !!");
                    return foodItem;
                }
                else if ((a > carbCheck1) && (b < proteinCheck1) && (c < fatCheck1) &&   (b > proteinCheck) && (c > fatCheck))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(2);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("You can make a better choice ! Look for something with Carbohydrates less than "+String.valueOf(carbCheck1)+"g");
                    return foodItem;
                }
                else if ((a < carbCheck1) &&   (c < fatCheck1) && (a > carbCheck) && (b > proteinCheck1) && (c > fatCheck))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(2);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("You can make a better choice ! Look for something with Proteins less than "+String.valueOf(proteinCheck1)+"g");
                    return foodItem;
                }
                else if ((a < carbCheck1) && (b < proteinCheck1)  && (a > carbCheck) && (b > proteinCheck) && (c > fatCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(2);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("You can make a better choice ! Look for something with Fats less than "+String.valueOf(fatCheck1)+"g");
                    return foodItem;
                }
                else if((a > carbCheck1) && (b > proteinCheck1) && (c > fatCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(3);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Big NO ! Look for something with Carbohydrates less than "+String.valueOf(carbCheck1)+"g "+ "Proteins less than "+String.valueOf(proteinCheck1)+"g "+"Fats less than "+String.valueOf(fatCheck1)+"g");
                    return foodItem;
                }
                else if((a > carbCheck1) && (b > proteinCheck1) && (c < fatCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(3);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Big NO ! Look for something with Carbohydrates less than "+String.valueOf(carbCheck1)+"g "+ "Proteins less than "+String.valueOf(proteinCheck1)+"g "+"Fats less than "+String.valueOf(fatCheck1)+"g");
                    return foodItem;
                }
                else if((a > carbCheck1) && (b < proteinCheck1) && (c > fatCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(3);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Big NO ! Look for something with Carbohydrates less than "+String.valueOf(carbCheck1)+"g "+ "Proteins less than "+String.valueOf(proteinCheck1)+"g "+"Fats less than "+String.valueOf(fatCheck1)+"g");
                    return foodItem;
                }
                else if((a < carbCheck1) && (b > proteinCheck1) && (c > fatCheck1))
                {
                    foodItem.setMeal(meal);
                    foodItem.setResult(3);
                    foodItem.setMealIndex(i);
                    foodItem.setItem_result("Big NO ! Look for something with Carbohydrates less than "+String.valueOf(carbCheck1)+"g "+ "Proteins less than "+String.valueOf(proteinCheck1)+"g "+"Fats less than "+String.valueOf(fatCheck1)+"g");
                    return foodItem;
                }
            }
        }
        return foodItem;
    }
}
