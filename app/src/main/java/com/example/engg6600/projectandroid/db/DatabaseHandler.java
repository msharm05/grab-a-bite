package com.example.engg6600.projectandroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.engg6600.projectandroid.beans.FoodItem;
import com.example.engg6600.projectandroid.beans.User;
import com.google.gson.Gson;

/**
 * Created by ermil on 2017-11-23.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "applicationDatabase";

    private static final String TABLE_USER = "userTable";

    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_AGE = "age";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_QUESTION = "question_id";
    private static final String KEY_DOB = "dob";
    private static final String KEY_FOODITEM = "food_item";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_FIRST_NAME + " TEXT," + KEY_LAST_NAME + " TEXT,"
                + KEY_USERNAME + " TEXT UNIQUE," + KEY_PASSWORD + " TEXT," + KEY_ANSWER + " TEXT," + KEY_GENDER + " TEXT," +KEY_AGE + " INTEGER," + KEY_WEIGHT + " INTEGER," + KEY_HEIGHT + " INTEGER," + KEY_QUESTION + " INTEGER," + KEY_DOB + " TEXT," + KEY_FOODITEM + " TEXT" + ");";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(sqLiteDatabase);

    }

    public User authenticateUser(User user)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_USER + " where " + KEY_USERNAME + " =?";
        Cursor cursor = db.rawQuery(query,new String[]{user.getUser_name()});
        if (cursor != null && cursor.moveToFirst())
        {
            String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            if(user.getPassword().equals(password))
            {
                User user1 = new User();
                user1.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                user1.setFirst_name(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                user1.setLast_name(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                user1.setAge(cursor.getInt(cursor.getColumnIndex(KEY_AGE)));
                user1.setWeight(cursor.getInt(cursor.getColumnIndex(KEY_WEIGHT)));
                user1.setHeight(cursor.getInt(cursor.getColumnIndex(KEY_HEIGHT)));
                user1.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
                user1.setDob(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
                return user1;
            }
        }
        return null;
    }

    public Boolean newAccount(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, user.getFirst_name());
        values.put(KEY_LAST_NAME, user.getLast_name());
        values.put(KEY_USERNAME, user.getUser_name());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_QUESTION, user.getQuestion_id());
        values.put(KEY_ANSWER, user.getAnswer());
        long a = db.insert(TABLE_USER, null, values);
        if (a > 0) {
                return true;
            }
        return false;
    }

    public User getUser(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+TABLE_USER+" where "+KEY_ID+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst())
        {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            user.setFirst_name(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
            user.setLast_name(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
            user.setAge(cursor.getInt(cursor.getColumnIndex(KEY_AGE)));
            user.setHeight(cursor.getInt(cursor.getColumnIndex(KEY_HEIGHT)));
            user.setWeight(cursor.getInt(cursor.getColumnIndex(KEY_WEIGHT)));
            user.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
            user.setDob(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
            user.setFood_item(cursor.getString(cursor.getColumnIndex(KEY_FOODITEM)));

            return user;
        }
        return null;
    }

    public Boolean updateUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, user.getFirst_name());
        values.put(KEY_LAST_NAME, user.getLast_name());
        values.put(KEY_AGE, user.getAge());
        values.put(KEY_HEIGHT, user.getHeight());
        values.put(KEY_WEIGHT, user.getWeight());
        values.put(KEY_DOB, user.getDob());
        values.put(KEY_GENDER, user.getGender());
        long a = db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        if(a>0)
        {
            return true;
        }
        return false;
    }

    public Boolean addFoodItem(User user,FoodItem foodItem)
    {
        Gson gson = new Gson();
        String json = gson.toJson(foodItem);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FOODITEM,json);
        long a = db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        if(a>0)
        {
            return true;
        }
        return false;
    }

    public Boolean deleteFoodItem(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FOODITEM,"");
        long a = db.update(TABLE_USER,values,KEY_ID + " = ?",new String[]{String.valueOf(user.getId())});
        if(a>0)
        {
            return true;
        }
        return false;
    }

    public String getSecurityQuestion(String user_name)
    {
        String question=null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+TABLE_USER+" where "+KEY_USERNAME+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{user_name});
        if (cursor != null && cursor.moveToFirst())
        {

            question = cursor.getString(cursor.getColumnIndex(KEY_QUESTION));

            return question;
        }
        return question;
    }

    public User checkSecurityAnswer(String user_name,String response)
    {
        User user=null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+TABLE_USER+" where "+KEY_USERNAME+" =?";
        Cursor cursor = db.rawQuery(query,new String[]{user_name});
        if (cursor != null && cursor.moveToFirst())
        {

            String answer = cursor.getString(cursor.getColumnIndex(KEY_ANSWER));
            if(answer.equalsIgnoreCase(response))
            {
                user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                return user;
            }
        }
        return null;

    }

    public Boolean resetPassword(int user_id,String newPassword)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PASSWORD, newPassword);
        long a = db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] {String.valueOf(user_id)});
        if(a>0)
        {
            return true;
        }
        return false;
    }

}

