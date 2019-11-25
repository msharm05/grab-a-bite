package com.example.engg6600.projectandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.engg6600.projectandroid.beans.User;
import com.example.engg6600.projectandroid.db.DatabaseHandler;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnViewProfile, btnDailyNeeds, btnMealSchedule, btnCheckFoodProduct, btnExit;
    private int user_id;
    private SharedPreferences sp;
    private User user;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sp = getSharedPreferences("Shared_Preferences", MODE_PRIVATE);
        user_id = sp.getInt("User_Id", -1);
        db = new DatabaseHandler(this);
        sp = getSharedPreferences("Shared_Preferences", MODE_PRIVATE);
        btnViewProfile = (Button) findViewById(R.id.btnViewProfile);
        btnDailyNeeds = (Button) findViewById(R.id.btnDailyNeeds);
        btnMealSchedule = (Button) findViewById(R.id.btnMealSchedule);
        btnCheckFoodProduct = (Button) findViewById(R.id.btnCheckFoodProduct);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnViewProfile.setOnClickListener(this);
        btnDailyNeeds.setOnClickListener(this);
        btnMealSchedule.setOnClickListener(this);
        btnCheckFoodProduct.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        user = db.getUser(user_id);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnViewProfile: {
                Intent i = new Intent(MenuActivity.this, ProfileActivity.class);
                i.putExtra("User_Id", user_id);
                startActivity(i);
                break;
            }
            case R.id.btnDailyNeeds: {
                Intent i = new Intent(MenuActivity.this,DailyCaloricNeedsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btnMealSchedule: {
                Intent i = new Intent(MenuActivity.this, MealScheduleActivity.class);
                i.putExtra("User_Id", user_id);
                startActivity(i);
                break;
            }
            case R.id.btnCheckFoodProduct: {
                Intent i = new Intent(MenuActivity.this, CheckFoodActivity.class);
                i.putExtra("User_Id", user_id);
                startActivity(i);
                break;
            }
            case R.id.btnExit: {
                this.finish();
                System.exit(0);
                break;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.application_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout: {
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("User_Id");
                editor.commit();
                Intent i = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(i);
                this.finish();
            }
        }
        return true;
    }
}

