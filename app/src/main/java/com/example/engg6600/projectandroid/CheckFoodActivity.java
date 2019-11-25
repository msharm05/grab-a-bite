package com.example.engg6600.projectandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.engg6600.projectandroid.beans.FoodItem;
import com.example.engg6600.projectandroid.beans.Meals;
import com.example.engg6600.projectandroid.beans.User;
import com.example.engg6600.projectandroid.db.DatabaseHandler;
import com.example.engg6600.projectandroid.services.ComparisonServices;
import com.example.engg6600.projectandroid.services.DietAssistantServices;
import com.example.engg6600.projectandroid.services.DietPlannerServices;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.engg6600.projectandroid.R.id.lvMeals;

public class CheckFoodActivity extends AppCompatActivity {

    private User user,user1;
    private Button btnCapture,btnCheckFood,btnTryAgain;
    private Bitmap mBitmap;
    private ImageView mImageView;;
    private String mCurrentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private DatabaseHandler db;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_food);
        sp = getSharedPreferences("Shared_Preferences",MODE_PRIVATE);
        int user_id = sp.getInt("User_Id",-1);
        db = new DatabaseHandler(this);
        user1 = db.getUser(user_id);
        mImageView = (ImageView) findViewById(R.id.cameraView);
        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnCheckFood = (Button)findViewById(R.id.btnCheck);
        btnTryAgain = (Button)findViewById(R.id.btnTryAgain);
        btnCheckFood.setVisibility(View.GONE);
        btnTryAgain.setVisibility(View.GONE);
        if (user1.getGender() != null && user1.getAge() != 0 && user1.getWeight() != 0 && user1.getHeight() != 0)
        {
            btnCapture.setVisibility(View.VISIBLE);
        }
        else
        {
            btnCapture.setVisibility(View.GONE);
            Toast.makeText(this, "Create a Meal Schedule to compare a Food Product.", Toast.LENGTH_SHORT).show();
        }
    }

    public void capturePhoto(View v) {
        dispatchTakePictureIntent();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

         mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(mBitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
            btnCapture.setVisibility(View.GONE);
            btnTryAgain.setVisibility(View.VISIBLE);
            btnCheckFood.setVisibility(View.VISIBLE);
        }
    }

    public void checkFood(View v) {
        DietAssistantServices dietAssistantServices = new DietAssistantServices(mBitmap, getApplicationContext());
        FoodItem foodItem = dietAssistantServices.getFoodItem();
        if (foodItem != null) {
            ComparisonServices comparisonServices = new ComparisonServices(user1,foodItem);
            final FoodItem foodItem1 = comparisonServices.compareFoodItem();
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Food Check");
            alertDialogBuilder.setMessage(foodItem1.getItem_result());
            if(foodItem1.getResult()!= 3) {
                alertDialogBuilder.setNegativeButton("Grab", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteFoodItem(user1);
                        db.addFoodItem(user1,foodItem1);
                        Intent intent = new Intent(CheckFoodActivity.this, MealScheduleActivity.class);
                        intent.putExtra("User_Id", user1.getId());
                        startActivity(intent);
                        dialogInterface.dismiss();

                    }
                });
            }
            alertDialogBuilder.setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {  Toast.makeText(CheckFoodActivity.this,"Please try again !",Toast.LENGTH_SHORT).show();}
    }

    public void tryAgain(View v)
    {
        dispatchTakePictureIntent();
    }

}

