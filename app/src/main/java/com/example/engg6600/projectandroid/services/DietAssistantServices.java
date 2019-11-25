package com.example.engg6600.projectandroid.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;

import com.example.engg6600.projectandroid.beans.FoodItem;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ermil on 2017-11-18.
 */

public class DietAssistantServices {
    private Bitmap mBitmap;
    private Context context;
    private static final String CARBOHYDRATE = "Carbohydrate";
    private static final String PROTEIN = "Protein";
    private static final String FAT = "Total Fat";

    public DietAssistantServices() {
    }

    public DietAssistantServices(Bitmap bitmap, Context context) {
        this.mBitmap = bitmap;
        this.context = context;
    }

    public String recognizeText() {
        String text = null;
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        if (!textRecognizer.isOperational()) {
            Log.e("Main Activity", "Dependencies not Working !!");
        } else {
            Frame frame = new Frame.Builder().setBitmap(mBitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); ++i) {
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
            }
            text = stringBuilder.toString();
        }
        return text;
    }

    public String processText(String text, String value) {
        String a = null, b = null, d = null, s = null, value1 = null;
        int c = 0, g = 0;
        try {
            a = text.replace(value, "@");
            b = a.replaceAll("g", "&");
            c = b.indexOf("@");
            d = b.substring(c);
            s = d.replaceFirst("&", "*");
            g = s.indexOf("*");
            value1 = s.substring(1, g).trim();
            return value1;
        } catch (Exception e) {
            return null;
        }
    }

    public FoodItem getFoodItem() {
        int c = 0, p = 0, f = 0;
        String text = recognizeText();
        String carbs = processText(text, CARBOHYDRATE);
        String proteins = processText(text, PROTEIN);
        String fats = processText(text, FAT);
        if (carbs!=null && proteins!=null && fats!=null) {
            String contents = "Carbs : " + carbs + "g Proteins : " + proteins + "g Fats : " + fats + "g";
            try {
                c = Integer.parseInt(carbs);
                p = Integer.parseInt(proteins);
                f = Integer.parseInt(fats);
            } catch (Exception e) {
                return null;
            }
            Calendar calendar1 = Calendar.getInstance();
            Date date = calendar1.getTime();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String time = dateFormat.format(date);
            FoodItem foodItem = new FoodItem(c, p, f, contents, time);
            return foodItem;
        } else {
            return null;
        }

    }
}
