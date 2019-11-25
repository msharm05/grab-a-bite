package com.example.engg6600.projectandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engg6600.projectandroid.beans.User;
import com.example.engg6600.projectandroid.db.DatabaseHandler;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etLoginUserName, etLoginPassword;
    private TextView tvForgotPassword, tvNewAccout;
    private Button btnLogin;
    private User user;
    private DatabaseHandler db;
    private SharedPreferences sp;
    private int user_id;
    private static final int PERMISSION_REQUEST = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }
        db = new DatabaseHandler(this);
        sp = getSharedPreferences("Shared_Preferences", Activity.MODE_PRIVATE);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        etLoginPassword = (EditText)findViewById(R.id.etLoginPassword);
        etLoginUserName = (EditText)findViewById(R.id.etLoginUserName);
        tvForgotPassword = (TextView)findViewById(R.id.tvForgotPassword);
        tvNewAccout = (TextView)findViewById(R.id.tvSignUp);
        btnLogin.setOnClickListener(this);
        tvNewAccout.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        user_id = sp.getInt("User_Id",-1);
        if(user_id > -1)
        {
            Intent i = new Intent(LoginActivity.this,MenuActivity.class);
            i.putExtra("User_Id",user_id);
            startActivity(i);
            this.finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btnLogin:
            {
               if(etLoginUserName.getText().toString().equalsIgnoreCase(""))
               {
                   Toast.makeText(this,"Enter User Name",Toast.LENGTH_SHORT).show();
               }
               else if(etLoginPassword.getText().toString().equalsIgnoreCase(""))
               {
                   Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
               }
               else
               {
                   user = new User();
                   user.setUser_name(etLoginUserName.getText().toString().trim());
                   user.setPassword(etLoginPassword.getText().toString().trim());
                   if(db.authenticateUser(user)!=null)
                   {
                       SharedPreferences.Editor editor = sp.edit();
                       editor.putInt("User_Id", db.authenticateUser(user).getId());
                       editor.commit();
                       Intent i = new Intent(LoginActivity.this,MenuActivity.class);
                       i.putExtra("User_Id",db.authenticateUser(user).getId());
                       startActivity(i);
                       this.finish();
                   }
                   else
                   {
                       Toast.makeText(this,"Wrong User Name or Password !",Toast.LENGTH_SHORT).show();
                   }
               }
                break;
            }
            case R.id.tvForgotPassword:
            {
                if(etLoginUserName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"You must enter User Name !",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                    i.putExtra("User Name",etLoginUserName.getText().toString().trim());
                    startActivity(i);
                    this.finish();
                }
                break;
            }
            case R.id.tvSignUp:
            {
                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
                this.finish();
                break;
            }
        }
    }
}
