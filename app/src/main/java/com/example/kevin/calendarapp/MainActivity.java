package com.example.kevin.calendarapp;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kevin.calendarapp.R;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
