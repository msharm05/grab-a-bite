package com.example.engg6600.projectandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.engg6600.projectandroid.beans.User;
import com.example.engg6600.projectandroid.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText etFirstName,etLastName,etUserName,etPassword,etConfrimPassword,etAnswer;
    private Button btnCreate,btnReset;
    private Spinner spinner;
    private List<String> securityQuestions;
    private DatabaseHandler db;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sp = getSharedPreferences("Shared_Preferences",MODE_PRIVATE);
        db = new DatabaseHandler(this);
        etFirstName = (EditText)findViewById(R.id.etSignUpFirstName);
        etLastName = (EditText)findViewById(R.id.etSignUpLastName);
        etUserName = (EditText)findViewById(R.id.etSignUpUserName);
        etPassword = (EditText)findViewById(R.id.etSignUpPassword);
        etConfrimPassword = (EditText)findViewById(R.id.etSignUpConfirmPassword);
        etAnswer = (EditText)findViewById(R.id.etSecurityAnswer);
        btnCreate = (Button)findViewById(R.id.btnSignUpCreate);
        btnReset = (Button)findViewById(R.id.btnSignUpReset);
        spinner = (Spinner) findViewById(R.id.spSignUpQuestions);
        btnCreate.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        securityQuestions = new ArrayList<String>();
        securityQuestions.add(0,"Choose a Security Question");
        securityQuestions.add(1,"Which is your favorite sport ?");
        securityQuestions.add(2,"Which city were you born ?");
        securityQuestions.add(3,"What is your pet's name ?");
        securityQuestions.add(4,"What is name of your primary school ?");
        securityQuestions.add(5,"Which is your favorite car ?");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,securityQuestions);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSignUpCreate:
            {
                if(etFirstName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter First Name",Toast.LENGTH_SHORT).show();
                }
                else if(etLastName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter Last Name",Toast.LENGTH_SHORT).show();
                }
                else if(etUserName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter User Name",Toast.LENGTH_SHORT).show();
                }
                else if(etPassword.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
                }
                else if(etConfrimPassword.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter Confirm Password",Toast.LENGTH_SHORT).show();
                }
                else if(!etConfrimPassword.getText().toString().equals(etPassword.getText().toString()))
                {
                    Toast.makeText(this,"Password and Confirm Password does not match !",Toast.LENGTH_SHORT).show();
                }
                else if(spinner.getSelectedItemPosition() == 0)
                {
                    Toast.makeText(this,"Choose a Security Question",Toast.LENGTH_SHORT).show();
                }
                else if(etAnswer.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter Security Answer",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    User user = new User();
                    user.setFirst_name(etFirstName.getText().toString().trim());
                    user.setLast_name(etLastName.getText().toString().trim());
                    user.setUser_name(etUserName.getText().toString().trim());
                    user.setPassword(etPassword.getText().toString().trim());
                    user.setQuestion_id(spinner.getSelectedItemPosition());
                    user.setAnswer(etAnswer.getText().toString().trim());
                    if(db.newAccount(user))
                    {
                        User user1 = db.authenticateUser(user);
                        Toast.makeText(this,"Sign Up Successful",Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("User_Id",user1.getId());
                        editor.commit();
                        Intent i = new Intent(SignUpActivity.this,MenuActivity.class);
                        i.putExtra("User_Id",user1.getId());
                        startActivity(i);
                        this.finish();
                    }
                    else
                    {
                        Toast.makeText(this,"User Name already Exists !!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case R.id.btnSignUpReset:
            {
                etFirstName.setText("");
                etLastName.setText("");
                etUserName.setText("");
                etPassword.setText("");
                etConfrimPassword.setText("");
                spinner.setSelection(0);
                etAnswer.setText("");
                break;
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String question = (String) adapterView.getItemAtPosition(i);
        spinner.setPrompt(question);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
