package com.example.engg6600.projectandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.engg6600.projectandroid.beans.FoodItem;
import com.example.engg6600.projectandroid.beans.Meals;
import com.example.engg6600.projectandroid.beans.User;
import com.example.engg6600.projectandroid.db.DatabaseHandler;
import com.example.engg6600.projectandroid.services.ComparisonServices;
import com.example.engg6600.projectandroid.services.DietPlannerServices;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MealScheduleActivity extends AppCompatActivity {
    private User user,user1;
    private FoodItem foodItem=null;
    private int user_id;
    private DatabaseHandler databaseHandler;
    private ListView lvMeals;
    private List<Meals> mealsList;
    private DietPlannerServices dietPlannerServices;
    private ListViewAdapter listViewAdapter;
    private SharedPreferences sp;
    private String dateStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_schedule);
        Calendar calendar1 = Calendar.getInstance();
        Date date = calendar1.getTime();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String date1 = dateFormat1.format(date);
        String time = dateFormat.format(date);
        sp = getSharedPreferences("Shared_Preferences", MODE_PRIVATE);
        databaseHandler = new DatabaseHandler(this);
        int user_id = sp.getInt("User_Id",-1);
        user1 = databaseHandler.getUser(user_id);

        dateStart = sp.getString("Date Start", "2017-11-24");
        if (!date1.equalsIgnoreCase(dateStart)) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Date Start", date1);
            editor.commit();
            databaseHandler.deleteFoodItem(user1);
        }
            String food= user1.getFood_item();
            Gson gson = new Gson();
            foodItem = gson.fromJson(food, FoodItem.class);
            lvMeals = (ListView) findViewById(R.id.lvMeals);
            dietPlannerServices = new DietPlannerServices(user1, time, sp);

            if (user1.getGender() != null && user1.getAge() != 0 && user1.getWeight() != 0 && user1.getHeight() != 0) {
                if (foodItem == null) {

                    mealsList = dietPlannerServices.checkMealCompletion();
                    listViewAdapter = new ListViewAdapter(this, 0, mealsList);
                    lvMeals.setAdapter(listViewAdapter);
                } else {
                    mealsList = dietPlannerServices.updatedMealPlan(foodItem);
                    listViewAdapter = new ListViewAdapter(this, 0, mealsList);
                    lvMeals.setAdapter(listViewAdapter);
                }

            } else {
                Toast.makeText(MealScheduleActivity.this, "Complete Profile to Create a Meal Schedule", Toast.LENGTH_SHORT).show();
            }
        }

}

