package com.example.kevin.calendarapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by MacProJJ on 11/12/14.
 */
public class Edit_Bar extends Activity /*implements OnItemSelectedListener*/{
//    Spinner view_spinner;
    Button view;
    Button search;
    Button agenda;
    Button add;
    Button settings;

    //testing
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

//    public void onItemSelected(AdapterView<?> parent, View view,
//                               int pos, long id) {
//        // An item was selected. You can retrieve the selected item using
//        // parent.getItemAtPosition(pos)
//        Spinner spinner = (Spinner) findViewById(R.id.views_spinner);
//        spinner.setOnItemSelectedListener(this);
//    }
//
//    public void onNothingSelected(AdapterView<?> parent) {
//        // Another interface callback
//    }


}
