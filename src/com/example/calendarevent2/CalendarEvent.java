package com.example.calendarevent2;

import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalendarEvent extends ActionBarActivity {
	
	Button submitButton;
	String name, description, location, startTime, endTime, startDate, endDate, category;
	TextView random;
	Calendar beginEvent, endEvent;
	long calID, eventID, startLong, endLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_event);
		calID = 3;
		beginEvent = Calendar.getInstance();
		beginEvent.set(2014, 9, 14, 7, 30);
		startLong = beginEvent.getTimeInMillis();
		endEvent = Calendar.getInstance();
		endEvent.set(2014, 9, 14, 8, 45);
		endLong = endEvent.getTimeInMillis();
		submitButton = (Button) findViewById(R.id.button1);
		
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				name = ((EditText)findViewById(R.id.editText1)).getText().toString();
				description = ((EditText)findViewById(R.id.editText2)).getText().toString();
				location = ((EditText)findViewById(R.id.editText3)).getText().toString();
				startTime = ((EditText)findViewById(R.id.editText4)).getText().toString();
				endTime = ((EditText)findViewById(R.id.editText5)).getText().toString();
				startDate = ((EditText)findViewById(R.id.editText6)).getText().toString();
				endDate = ((EditText)findViewById(R.id.editText7)).getText().toString();
				category = ((EditText)findViewById(R.id.editText8)).getText().toString();
				
				/*
				ContentResolver cr = getContentResolver();
				ContentValues eventValues = new ContentValues();
				eventValues.put(Events.DTSTART, startLong);
				eventValues.put(Events.DTEND, endLong);
				eventValues.put(Events.TITLE, "Jazzercise");
				eventValues.put(Events.DESCRIPTION, "Group workout");
				eventValues.put(Events.CALENDAR_ID, calID);
				eventValues.put(Events.EVENT_TIMEZONE, "America/Los_Angeles");
				Uri uri = cr.insert(Events.CONTENT_URI, eventValues);
				eventID = Long.parseLong(uri.getLastPathSegment());
				
				uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
				Intent intent = new Intent(Intent.ACTION_VIEW)
				   .setData(uri);
				startActivity(intent);
				///////////////////////////////////////////////////////////////////
				
				name = (EditText)findViewById(R.id.editText1);
				description = (EditText)findViewById(R.id.editText1);
				location = (EditText)findViewById(R.id.editText1);
				//startTime = (EditText)findViewById(R.id.editText1);
				//endTime = (EditText)findViewById(R.id.editText1);
				startDate = (EditText)findViewById(R.id.editText1);
				endDate = (EditText)findViewById(R.id.editText1);
				category = (EditText)findViewById(R.id.editText1);
				
				Intent intent = new Intent(Intent.ACTION_INSERT)
					.setData(Events.CONTENT_URI)
					.putExtra(Events.TITLE, name.getText().toString())
					.putExtra(Events.DESCRIPTION, description.getText().toString())
					.putExtra(Events.EVENT_LOCATION, location.getText().toString());
				startActivity(intent);
				
				
				Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventID);
				Intent intent2 = new Intent(Intent.ACTION_VIEW)
				   .setData(uri);
				startActivity(intent);
			}
		});
		*/
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_event, menu);
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
