package com.example.kevin.calendarapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kevin.calendarapp.R;

/**
 * Created by MacProJJ on 11/12/14.
 */
public class Edit_Bar extends Activity{
    Button view;
    Button search;
    Button agenda;
    Button add;
    Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the main layout of the activity
        setContentView(R.layout.edit_bar_constant);

//        view = new Button();


        intializeViewButton();
    }

    public void intializeViewButton(){
//        view.setOnClickListener(this);


    }


}
