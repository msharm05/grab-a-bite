package com.example.engg6600.projectandroid.services;

import android.content.SharedPreferences;

import com.example.engg6600.projectandroid.beans.FoodItem;
import com.example.engg6600.projectandroid.beans.Meals;
import com.example.engg6600.projectandroid.beans.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ermil on 2017-11-16.
 */

public class DietPlannerServices {
    private User user;
    private Meals meal;
    private String time;
    private SharedPreferences sp;

    public DietPlannerServices(User user)
    {
        this.user=user;
    }
    public DietPlannerServices(User user, String time,SharedPreferences sp)
    {
        this.user = user;
        this.time = time;
        this.sp = sp;
    }
    public DietPlannerServices(User user, Meals meal)
    {
        this.user = user;
        this.meal = meal;
    }

    public int bmrCalculator()
    {
        int age  = user.getAge();
        int height = user.getHeight();
        int weight = user.getWeight();
        String gender = user.getGender();
        double bmrdouble;
        int bmr=0;
        if(gender.equalsIgnoreCase("Male"))
        {
            bmrdouble = ((10*weight)+(6.25*height)-(5*age)+5);
            bmr = (int)bmrdouble;
        }
        else if(gender.equalsIgnoreCase("Female"))
        {
            bmrdouble = ((10*weight)+(6.25*height)-(5*age)-161);
            bmr = (int)bmrdouble;
        }
        return bmr;
    }

    public int calorieCounter()
    {
        int calories = bmrCalculator();
        return calories;
    }

    public int maxCarbohydrateCounter()
    {
        int maxCarbs = (int)((calorieCounter()*0.75)/4);
        return maxCarbs;
    }

    public int minCarbohydrateCounter()
    {
        int minCarbs = (int)((calorieCounter()*0.55)/4);
        return minCarbs;
    }

    public int minProteinCounter()
    {
        int minProteins = (int)((calorieCounter()*0.20)/4);
        return minProteins;
    }

    public int maxProteinCounter()
    {
        int maxProteins = (int)((calorieCounter()*0.35)/4);
        return maxProteins;
    }

    public int maxFatCounter()
    {
        int maxFats = (int)((calorieCounter()*0.25)/9);
        return maxFats;
    }
    public int minFatCounter()
    {
        int minFats = (int)((calorieCounter()*0.1)/9);
        return minFats;
    }
    public List<Meals> createMealPlan()
    {
        String[] time = {"6:00 AM","9:30 AM","12:00 PM","3:00 PM","5:30 PM","8:30 PM"};
        String[] mealname = {"Breakfast","Pre-Lunch Snack","Lunch","Evening Snack","Dinner"};
        double[] weight = {0.25,0.15,0.25,0.15,0.20};
        List<Meals> mealsList = new ArrayList<Meals>();
        for(int i=0;i<5;i++)
        {
            Meals meal1 = new Meals();
            meal1.setMealName(mealname[i]);
            meal1.setCalories((int) (calorieCounter()*weight[i]));
            meal1.setMinCarb((int) (minCarbohydrateCounter()*weight[i]));
            meal1.setMaxCarb((int) (maxCarbohydrateCounter()*weight[i]));
            meal1.setMinProtein((int) (minProteinCounter()*weight[i]));
            meal1.setMaxProtein((int) (maxProteinCounter()*weight[i]));
            meal1.setMinFat((int)(minFatCounter()*weight[i]));
            meal1.setMaxFat((int)(maxFatCounter()*weight[i]));
            meal1.setMealTime(time[i+1]);
            meal1.setMealLowerTime(time[i]);
            mealsList.add(meal1);
        }

        return mealsList;
    }
    public List<Meals> updatedMealPlan(FoodItem foodItem)
    {
        List<Meals> updatedlistMeals;
        int index = foodItem.getMealIndex();
        updatedlistMeals = checkMealCompletion();
        Meals meal = updatedlistMeals.remove(foodItem.getMealIndex());
        meal.setMaxCarb(meal.getMaxCarb() - foodItem.getItemCarbs());
        meal.setMaxProtein(meal.getMaxProtein() - foodItem.getItemProteins());
        meal.setMaxFat(meal.getMaxFat() - foodItem.getItemFats());
        if((meal.getMaxCarb() - foodItem.getItemCarbs())<0)
        {
            meal.setMaxCarb(0);
        }
        else
        {
            meal.setMaxCarb(meal.getMaxCarb() -  foodItem.getItemCarbs());
        }
        if((meal.getMaxProtein() - foodItem.getItemProteins())<0)
        {
            meal.setMaxProtein(0);
        }
        else
        {
            meal.setMaxProtein(meal.getMaxProtein() -  foodItem.getItemProteins());
        }
        if((meal.getMaxFat() - foodItem.getItemFats())<0)
        {
            meal.setMaxFat(0);
        }
        else
        {
            meal.setMaxFat(meal.getMaxFat() -  foodItem.getItemFats());
        }
        if((meal.getMinCarb() - foodItem.getItemCarbs())<0)
        {
            meal.setMinCarb(0);
        }
        else
        {
            meal.setMinCarb(meal.getMinCarb() -  foodItem.getItemCarbs());
        }
        if((meal.getMinProtein() - foodItem.getItemProteins())<0)
        {
            meal.setMinProtein(0);
        }
        else
        {
            meal.setMinProtein(meal.getMinProtein() -  foodItem.getItemProteins());
        }
        if((meal.getMinFat() - foodItem.getItemFats())<0)
        {
            meal.setMinFat(0);
        }
        else
        {
            meal.setMinFat(meal.getMinFat() -  foodItem.getItemFats());
        }
        meal.setCalories(meal.getCalories() - foodItem.getItemCalories());
        updatedlistMeals.add(foodItem.getMealIndex(),meal);
        return updatedlistMeals;
    }

    public List<Meals> checkMealCompletion()
    {
        Meals meal;
        String mealTime;
        String mealLowerTime;
        ComparisonServices comparisonServices = new ComparisonServices();
        List<Meals> mealsCompletionList = createMealPlan();
        if(comparisonServices.isTimeWithinInterval("6:00 AM","8:30 PM",this.time))
        {
            for (int i = 0; i < 5; i++) {
                meal = mealsCompletionList.get(i);
                mealTime = meal.getMealTime();
                mealLowerTime = meal.getMealLowerTime();
                if (comparisonServices.isTimeWithinInterval(mealLowerTime, mealTime, this.time)) {
                    for (int j = 0; j < i; j++) {
                        Meals meal1 = mealsCompletionList.remove(j);
                        meal1.setMealTime("Done");
                        mealsCompletionList.add(j, meal1);
                    }
                    return mealsCompletionList;
                }

            }
        }
        else if(comparisonServices.isTimeWithinInterval("8:31 PM","11:59 PM",this.time))
        {
            for(int j=0;j<5;j++)
            {
                Meals meal1 = mealsCompletionList.remove(j);
                meal1.setMealTime("Done");
                mealsCompletionList.add(j,meal1);
            }
            return mealsCompletionList;
        }
        else if(comparisonServices.isTimeWithinInterval("12:00 AM","5:59 AM",this.time))
        {
            return mealsCompletionList;
        }
        return null;
    }

}
