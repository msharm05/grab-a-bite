package com.example.engg6600.projectandroid;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.engg6600.projectandroid.beans.Meals;

import java.util.List;

/**
 * Created by ermil on 2017-11-14.
 */

public class ListViewAdapter extends ArrayAdapter<Meals> {
    private Context context;
    private List<Meals> mealsArrayList;
    private static LayoutInflater inflater;


    public ListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Meals> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mealsArrayList = objects;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_layout, null);
        }
            TextView tvName = (TextView)view.findViewById(R.id.tvMealName);
            TextView tvCalories = (TextView)view.findViewById(R.id.tvCalories);
            TextView tvMacro = (TextView)view.findViewById(R.id.tvMacro);
            TextView tvMealTime = (TextView)view.findViewById(R.id.tvMealTime);
            tvName.setText(mealsArrayList.get(position).getMealName());
            tvMealTime.setText(mealsArrayList.get(position).getMealTime());
            tvCalories.setText(mealsArrayList.get(position).displayCalories());
            tvMacro.setText(mealsArrayList.get(position).displayContent());

        return view;
    }
}
