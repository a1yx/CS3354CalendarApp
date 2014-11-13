package com.example.kevin.calendarapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.kevin.calendarapp.R;

import java.util.Date;

/**
 * Created by MacProJJ on 11/12/14.
 */
public class Daily_View extends Activity {

    EditText datethingy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the main layout of the activity
        setContentView(R.layout.activity_daily_view);

        Intent intent = getIntent();

        datethingy = (EditText) findViewById(R.id.daily_date);

        initializeDaily(datethingy, intent);
    }

    public void initializeDaily(EditText datethingy, Intent intent){
        String month, day, year;
        month = intent.getStringExtra(Monthly_View_Example.monthFinal);
        day = intent.getStringExtra(Monthly_View_Example.dayFinal);
        year = intent.getStringExtra(Monthly_View_Example.yearFinal);
        datethingy.setText(month + " " + day + ", " + year);
    }
}
