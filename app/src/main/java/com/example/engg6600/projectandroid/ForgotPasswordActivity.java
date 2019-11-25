package com.example.engg6600.projectandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engg6600.projectandroid.beans.User;
import com.example.engg6600.projectandroid.db.DatabaseHandler;

public class ForgotPasswordActivity extends AppCompatActivity {
    private TextView tvSecurityQuestion;
    private EditText etAnswer;
    private Button btnSubmit;
    private DatabaseHandler db;
    private String user_name,question;
    private String[] questions = {"Null","Which is your favorite sport ?","Which city were you born ?","What is your pet's name ?","What is name of your primary school ?","Which is your favorite car ?"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        tvSecurityQuestion = (TextView)findViewById(R.id.SecurityQuestion);
        etAnswer = (EditText)findViewById(R.id.etAnswer);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        db = new DatabaseHandler(this);
        Intent i = getIntent();
        user_name = i.getStringExtra("User Name");
        question = db.getSecurityQuestion(user_name);
        tvSecurityQuestion.setText(questions[Integer.parseInt(question)]);
    }

    public void submitAnswer(View v)
    {
        if(etAnswer.getText().toString().equalsIgnoreCase(""))
        {
            Toast.makeText(this,"Enter your answer",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String answer = etAnswer.getText().toString().trim();
            User user = db.checkSecurityAnswer(user_name,answer);
            if(user!=null)
            {
                Intent i = new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class);
                i.putExtra("User_Id",user.getId());
                startActivity(i);
                this.finish();
            }
            else
            {
                Toast.makeText(ForgotPasswordActivity.this,"Incorrect Answer",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
