package com.example.kevin.calendarapp;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import android.app.Activity;
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
import android.widget.Toast;

public class calendarEvent extends Activity {
	
	// Set up all default values
	Button submitButton;
	String name, description, location, startTime, endTime, startDate, endDate, category;
	TextView random;
	Calendar beginEvent, endEvent;
	long calID, eventID, startLong, endLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_event);
		submitButton = (Button) findViewById(R.id.submit_button);
		
		// Set listener for the submit button
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				// Set all variable names to user input
				name = ((EditText)findViewById(R.id.name_edit)).getText().toString();
				description = ((EditText)findViewById(R.id.description_edit)).getText().toString();
				location = ((EditText)findViewById(R.id.location_edit)).getText().toString();
				startTime = ((EditText)findViewById(R.id.startTime_edit)).getText().toString();
				endTime = ((EditText)findViewById(R.id.endTime_edit)).getText().toString();
				startDate = ((EditText)findViewById(R.id.startDate_edit)).getText().toString();
				endDate = ((EditText)findViewById(R.id.endDate_edit)).getText().toString();
				category = ((EditText)findViewById(R.id.category_edit)).getText().toString();
				File file = new File(getFilesDir(), "calendar.csv");
				
				// Write to the calendar.csv file. WARNING: have not tested if it appends or not
				try {
					FileOutputStream ofile = new FileOutputStream(file);
					OutputStreamWriter writer = new OutputStreamWriter(ofile);
					
					try {
						fileWrite(writer);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				// Read the file back. To be implemented more fully later
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					
					try {
						fileRead(reader);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
				// Set the edit text fields to the user input
				// This will be useful for editing events
				((EditText)findViewById(R.id.name_edit)).setText(name);
				((EditText)findViewById(R.id.description_edit)).setText(description);
				((EditText)findViewById(R.id.location_edit)).setText(location);
				((EditText)findViewById(R.id.startTime_edit)).setText(startTime);
				((EditText)findViewById(R.id.endTime_edit)).setText(endTime);
				((EditText)findViewById(R.id.startDate_edit)).setText(startDate);
				((EditText)findViewById(R.id.endDate_edit)).setText(endDate);
				((EditText)findViewById(R.id.category_edit)).setText(category);

                syncCalendar(file);
				
				/* 
				 * This uses the calendar provider directly. uncomment to test
				 * 
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
				*
				* This uses the calendar provider using intents. uncomment to test
				* 
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
	
	public void fileWrite(OutputStreamWriter writer) throws IOException {
		// Write all values to a csv file. Separated for readability
		writer.write(name + "," + description + "," + location + ",");
		writer.write(startTime + "," + endTime + ",");
		writer.write(startDate + "," + endDate + "," + category + '\n');
		writer.flush();
		writer.close();
		// Display a little bubble to notify the user of the save
		Toast.makeText(getBaseContext(), "Event Saved", Toast.LENGTH_LONG).show();
	}
	
	public void fileRead(BufferedReader reader) throws IOException {
		String line;
		line = reader.readLine();
		Tokenizer tokenizer = new Tokenizer(line);

		/*
		 * Uncomment this to debug file reading
		 * 
		//Uses the Tokenizer class to separate the input line into the values
		name = "1 " + tokenizer.next();
		description = "1 " + tokenizer.next();
		location = "1 " + tokenizer.next();
		startTime = "1 " + tokenizer.next();
		endTime = "1 " + tokenizer.next();
		startDate = "1 " + tokenizer.next();
		endDate = "1 " + tokenizer.next();
		category = "1 " + tokenizer.next();
		*/
	}

    public void syncCalendar(File file) {
        try {
            URL url = new URL("http://calendarse-maveptp.rhcloud.com/serv.j");
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}