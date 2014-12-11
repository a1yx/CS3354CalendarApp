package com.example.kevin.calendarapp;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;

import java.util.Calendar;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onClickShowCalendar(MenuItem item){
    	//Set date to specific local time
    	Calendar dateToShow = Calendar.getInstance();
    	dateToShow.set(2014, Calendar.NOVEMBER, 8, 10, 15);
    	
    	//Show calendar at specified date
    	showCalendarAtTime(dateToShow);
    }
    
    public void showCalendarAtTime(Calendar dateToShow){
    	long epochMilliseconds = dateToShow.getTimeInMillis();
    	
    	Uri.Builder uriBuilder = CalendarContract.CONTENT_URI.buildUpon();
    	uriBuilder.appendPath("time");
    	ContentUris.appendId(uriBuilder, epochMilliseconds);
    	Uri uri = uriBuilder.build();
    	
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(uri);
    	
    	startActivity(intent);
    }

}
