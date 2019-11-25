package com.example.engg6600.projectandroid;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engg6600.projectandroid.beans.User;
import com.example.engg6600.projectandroid.db.DatabaseHandler;
import com.example.engg6600.projectandroid.services.DietPlannerServices;

public class DailyCaloricNeedsActivity extends AppCompatActivity {
    private TextView txtName,txtAge,txtWeight,txtHeight,txtBMR,txtCalories,txtCarbs,txtProteins,txtFats;
    private User user;
    private SharedPreferences sp;
    private int user_id;
    private DatabaseHandler db;
    private DietPlannerServices d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_caloric_needs);
        txtName = (TextView)findViewById(R.id.txtName);
        txtAge = (TextView)findViewById(R.id.txtAge);
        txtWeight = (TextView)findViewById(R.id.txtWeight);
        txtHeight = (TextView)findViewById(R.id.txtHeight);
        txtBMR = (TextView)findViewById(R.id.txtBMR);
        txtCalories = (TextView)findViewById(R.id.txtCalories);
        txtCarbs = (TextView)findViewById(R.id.txtCarbs);
        txtProteins = (TextView)findViewById(R.id.txtProteins);
        txtFats = (TextView)findViewById(R.id.txtFats);
        db = new DatabaseHandler(this);
        sp = getSharedPreferences("Shared_Preferences",MODE_PRIVATE);
        user_id = sp.getInt("User_Id",-1);
        user = db.getUser(user_id);
        d = new DietPlannerServices(user);
        if (user.getGender() != null || user.getAge() != 0 || user.getWeight() != 0 || user.getHeight() != 0)
        {
            getandSetInformation(user);
        }
        else
        {
            Toast.makeText(DailyCaloricNeedsActivity.this, "Complete Profile to view Daily Caloric Needs", Toast.LENGTH_SHORT).show();
        }
    }

    public void getandSetInformation(User user)
    {

        txtName.append(user.getFull_name());
        txtAge.append(String.valueOf(Math.abs(user.getAge())));
        txtHeight.append(String.valueOf(user.getHeight()) + " cm");
        txtWeight.append(String.valueOf(user.getWeight()) + " kg");
        txtBMR.append(String.valueOf(d.bmrCalculator()) + " Calories");
        txtCalories.append(String.valueOf((int)(d.calorieCounter()*1.55)) + " Calories");
        txtCarbs.append(String.valueOf(d.minCarbohydrateCounter()) + " - " + String.valueOf(d.maxCarbohydrateCounter()) + " g");
        txtProteins.append(String.valueOf(d.minProteinCounter()) + " - " + String.valueOf(d.maxProteinCounter()) + " g");
        txtFats.append(String.valueOf(d.minFatCounter()) + " - " + String.valueOf(d.maxProteinCounter() + " g"));
    }

}