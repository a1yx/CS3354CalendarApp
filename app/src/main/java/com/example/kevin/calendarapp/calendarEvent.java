package com.example.kevin.calendarapp;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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
    private static Context context;
	Button submitButton, cancelButton;
	String uuid = "", name, description, location, startTime, endTime, startDate, endDate, category;
	TextView random;
	Calendar beginEvent, endEvent;
	long calID, eventID, startLong, endLong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_event);
        submitButton = (Button)findViewById(R.id.submit_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);

        Intent intent = getIntent();
		
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
                if(uuid.isEmpty()) {
                    try {
                        FileOutputStream ofile = new FileOutputStream(file);
                        OutputStreamWriter writer = new OutputStreamWriter(ofile);

                        try {
                            newEvent(writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                // Completely untested
                else {
                    try {
                        FileOutputStream ofile = new FileOutputStream(file);
                        OutputStreamWriter writer = new OutputStreamWriter(ofile);
                        BufferedReader reader = new BufferedReader(new FileReader(file));

                        try {
                            file = editEvent(writer, reader);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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

                //syncCalendar(file);

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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, calendarEvent.class);
                intent.putExtra(uuid, uuid);
                startActivity(intent);
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
	
	public void newEvent(OutputStreamWriter writer) throws IOException {
		// Write all values to a csv file. Separated for readability
        uuid = UUID.randomUUID().toString();
        writer.write(uuid + "," + name + "," + description + "," + location + ",");
		writer.write(startTime + "," + endTime + ",");
		writer.write(startDate + "," + endDate + "," + category + '\n');
		writer.flush();
		writer.close();
		// Display a little bubble to notify the user of the save
		Toast.makeText(getBaseContext(), "Event Saved", Toast.LENGTH_LONG).show();
	}

    public File editEvent(OutputStreamWriter writer, BufferedReader reader) throws IOException {
        // Write all values to a csv file. Separated for readability
        String line;
        File tempFile = new File(getFilesDir(), "temp.csv");
        FileOutputStream ostream = new FileOutputStream(tempFile);
        OutputStreamWriter tempWriter = new OutputStreamWriter(ostream);

        while((line = reader.readLine()) != null) {
            Tokenizer tokenizer = new Tokenizer(line);

            if(tokenizer.next() == uuid) {
                tempWriter.write(UUID.randomUUID().toString() + ",");
                tempWriter.write(name + "," + description + "," + location + ",");
                tempWriter.write(startTime + "," + endTime + ",");
                tempWriter.write(startDate + "," + endDate + "," + category + '\n');
            }

            else {
                for(String tempString; (tempString = tokenizer.next()) != "";)
                    tempWriter.write(tempString);
            }
        }

        // Display a little bubble to notify the user of the save
        Toast.makeText(getBaseContext(), "Event Saved", Toast.LENGTH_LONG).show();
        writer.close();
        tempWriter.flush();
        tempWriter.close();
        return tempFile;
    }
	
	public void fileRead(BufferedReader reader) throws IOException {
		String line;
		line = reader.readLine();
		Tokenizer tokenizer = new Tokenizer(line);

		/*
		 * Uncomment this to debug file reading
		 *
		 */
		//Uses the Tokenizer class to separate the input line into the values
		name = "1 " + tokenizer.next();
		description = "1 " + tokenizer.next();
		location = "1 " + tokenizer.next();
		startTime = "1 " + tokenizer.next();
		endTime = "1 " + tokenizer.next();
		startDate = "1 " + tokenizer.next();
		endDate = "1 " + tokenizer.next();
		category = "1 " + tokenizer.next();
        reader.close();
	}

    public void syncCalendar(File file) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL("http://calendarse-maveptp.rhcloud.com/serv.j");
            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            Message sent = new Message("sync", "Maverick", "pass");

            ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream());

            output.writeObject(sent);
            output.close();

            ObjectInputStream input = new ObjectInputStream(connection.getInputStream());

            Message strs = (Message)input.readObject();
            String success = strs.getMessage();
            input.close();
            System.out.println(success);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}