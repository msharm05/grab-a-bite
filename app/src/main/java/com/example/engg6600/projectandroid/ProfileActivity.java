package com.example.engg6600.projectandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.engg6600.projectandroid.beans.User;
import com.example.engg6600.projectandroid.db.DatabaseHandler;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener{
    private EditText etFirstName,etLastName,etAge,etWeight,etHeight;
    private RadioButton rbMale,rbFemale;
    private RadioGroup rgGender;
    private Button btnEdit, btnSave;
    private int year,month,day,age=0;
    private User user;
    private int user_id;
    private DatabaseHandler db;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sp = getSharedPreferences("Shared_Preferences",MODE_PRIVATE);
        user_id = sp.getInt("User_Id",-1);
        db = new DatabaseHandler(this);
        etFirstName = (EditText)findViewById(R.id.etProfileFirstName);
        etLastName = (EditText)findViewById(R.id.etProfileLastName);
        etAge = (EditText)findViewById(R.id.etProfileDOB);
        etWeight = (EditText)findViewById(R.id.etProfileWeight);
        etHeight = (EditText)findViewById(R.id.etProfileHeight);
        btnEdit = (Button)findViewById(R.id.btnEdit);
        btnSave = (Button)findViewById(R.id.btnSave);
        rgGender = (RadioGroup)findViewById(R.id.rgGender);
        rbMale = (RadioButton)findViewById(R.id.rbMale);
        rbFemale = (RadioButton)findViewById(R.id.rbFemale);
        btnSave.setVisibility(View.GONE);
        btnSave.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        etAge.setOnClickListener(this);
        user = db.getUser(user_id);
        etFirstName.setText(user.getFirst_name());
        etLastName.setText(user.getLast_name());
        if(user.getHeight()!=0)
        {
            etHeight.setText(String.valueOf(user.getHeight()));
        }
        else
        {
            etHeight.setText("");
        }
        if(user.getWeight()!=0)
        {
            etWeight.setText(String.valueOf(user.getWeight()));
        }
        else
        {
            etWeight.setText("");
        }
        if(user.getGender()!= null)
        {
            if(user.getGender().equals("Male"))
            {
                rbMale.setChecked(true);
            }
            else if(user.getGender().equals("Female"))
            {
                rbFemale.setChecked(true);
            }
        }
        if(user.getDob()!=null)
        {
            etAge.setText(user.getDob());
        }
        else
        {
            etAge.setText("");
        }
        editableFields(false);
    }

    public void editableFields(Boolean b)
    {
        etFirstName.setEnabled(b);
        etLastName.setEnabled(b);
        etWeight.setEnabled(b);
        etHeight.setEnabled(b);
        etAge.setEnabled(b);
        rgGender.setClickable(b);
        for (int i = 0; i < rgGender.getChildCount(); i++) {
            rgGender.getChildAt(i).setEnabled(b);
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        if (i > year) {
            Toast.makeText(ProfileActivity.this,"Invalid Date of Birth !",Toast.LENGTH_SHORT).show();
        } else {
            age = i - year;
            String dob = String.valueOf(i) + " - " + String.valueOf(i1+1) + " - " + String.valueOf(i2);
            etAge.setText(dob);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnSave:
            {

                if(etFirstName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter First Name",Toast.LENGTH_SHORT).show();
                }
                else if(etLastName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter Last Name",Toast.LENGTH_SHORT).show();
                }
                else if(etAge.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter DOB",Toast.LENGTH_SHORT).show();
                }
                else if(etHeight.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter Height",Toast.LENGTH_SHORT).show();
                }
                else if(etWeight.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(this,"Enter Weight",Toast.LENGTH_SHORT).show();
                }
                else if(rgGender.getCheckedRadioButtonId()!= rbMale.getId() && rgGender.getCheckedRadioButtonId()!= rbFemale.getId())
                {
                    Toast.makeText(this,"Choose a Gender",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RadioButton rb = (RadioButton)findViewById(rgGender.getCheckedRadioButtonId());
                    String gender = (String)rb.getText();
                    user.setId(user_id);
                    user.setFirst_name(etFirstName.getText().toString().trim());
                    user.setLast_name(etLastName.getText().toString().trim());
                    user.setGender(gender);
                    user.setHeight(Integer.parseInt(etHeight.getText().toString().trim()));
                    user.setWeight(Integer.parseInt(etWeight.getText().toString().trim()));
                    user.setDob(etAge.getText().toString().trim());
                    if(user.getAge()==0)
                    {
                        user.setAge(age);
                    }
                    if(db.updateUser(user))
                    {
                        Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                        btnSave.setVisibility(View.GONE);
                        btnEdit.setVisibility(View.VISIBLE);
                        editableFields(false);
                    }
                    else
                    {
                        Toast.makeText(ProfileActivity.this,"Profile Updation Failed !",Toast.LENGTH_SHORT).show();

                    }

                }
                break;
            }
            case R.id.btnEdit:
            {
                btnEdit.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                editableFields(true);
                break;
            }
            case R.id.etProfileDOB:
            {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,year,month,day);
                datePickerDialog.show();
                break;
            }

        }

    }
}
