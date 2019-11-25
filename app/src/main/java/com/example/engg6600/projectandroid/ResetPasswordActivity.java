package com.example.engg6600.projectandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.engg6600.projectandroid.db.DatabaseHandler;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText etNewPassword,etConfirmPassword;
    private Button btnReset;
    private DatabaseHandler db;
    private int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        db = new DatabaseHandler(this);
        Intent i =getIntent();
        user_id = i.getIntExtra("User_Id",-1);
        etNewPassword = (EditText)findViewById(R.id.etResetPassword);
        etConfirmPassword = (EditText)findViewById(R.id.etResetConfirmPassword);
        btnReset = (Button)findViewById(R.id.btnReset);
    }

    public void resetPassword(View v)
    {
        if(etNewPassword.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
        }
        else if(etConfirmPassword.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Enter Confirm Password",Toast.LENGTH_SHORT).show();
        }
        else if(!etConfirmPassword.getText().toString().equals(etNewPassword.getText().toString()))
        {
            Toast.makeText(this,"Password and Confirm Password does not match !",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String newPassword = etNewPassword.getText().toString().trim();
            if(db.resetPassword(user_id,newPassword))
            {
                Toast.makeText(ResetPasswordActivity.this,"Password Changed",Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("Shared_Preferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("User_Id", user_id);
                editor.commit();
                Intent i = new Intent(ResetPasswordActivity.this,MenuActivity.class);
                i.putExtra("User_Id",user_id);
                startActivity(i);
                this.finish();
            }
            else
            {
                Toast.makeText(ResetPasswordActivity.this,"Password Change Failed !",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
