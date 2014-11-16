package com.example.kevin.calendarapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.UUID;

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
        final Uri fileUri = Uri.parse(intent.getExtras().getString("fileUri"));
        final File file = new File(fileUri.getPath());

		
		// Set listener for the submit button
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
                name = ((EditText)findViewById(R.id.name_edit)).getText().toString();
                description = ((EditText)findViewById(R.id.description_edit)).getText().toString();
                location = ((EditText)findViewById(R.id.location_edit)).getText().toString();
                startTime = ((EditText)findViewById(R.id.startTime_edit)).getText().toString();
                endTime = ((EditText)findViewById(R.id.endTime_edit)).getText().toString();
                startDate = ((EditText)findViewById(R.id.startDate_edit)).getText().toString();
                endDate = ((EditText)findViewById(R.id.endDate_edit)).getText().toString();
                category = ((EditText)findViewById(R.id.category_edit)).getText().toString();

                if(description.isEmpty()) description = "No Description";
                if(location.isEmpty()) location = "Not Specified";
                if(category.isEmpty()) category = "None";

                if(name.isEmpty()){((EditText)findViewById(R.id.name_edit)).setText("Name Required".toCharArray(),0,12); return; }
                if(startTime.isEmpty()){((EditText) findViewById(R.id.startTime_edit)).setText("Start Time Required".toCharArray(),0,19); return; }
                if(endTime.isEmpty()){((EditText)findViewById(R.id.endTime_edit)).setText("End Time Required".toCharArray(),0,17); return; }
                if(startDate.isEmpty()){((EditText) findViewById(R.id.startDate_edit)).setText("Start Date Required".toCharArray(),0,19); return; }
                if(endDate.isEmpty()){((EditText)findViewById(R.id.endDate_edit)).setText("End Date Required".toCharArray(),0,17); return; }

				// Adds an event to calendar.csv
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
                // Edits an event in calendar.csv
                else {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));

                        try {
                            editEvent(reader, file);
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

                calendarEvent.this.finish();

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
                Toast.makeText(getBaseContext(), "Event Not Saved", Toast.LENGTH_LONG).show();
                calendarEvent.this.finish();
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
        syncCalendar(0,uuid + "," + name + "," + description + "," + location +
                "," + startTime + "," + endTime + "," + startDate + "," + endDate + "," + category);

    }

    public void editEvent(BufferedReader reader, File file)
            throws IOException {
        // Write all values to a csv file. Separated for readability
        String line;
        File tempFile = new File(getFilesDir(), "temp.csv");
        FileOutputStream ostream = new FileOutputStream(tempFile);
        OutputStreamWriter tempWriter = new OutputStreamWriter(ostream);
        Tokenizer tokenizer = new Tokenizer();

        while((line = reader.readLine()) != null) {
            tokenizer.setString(line);

            if(tokenizer.next().compareTo(uuid.toString()) == 0) {
                tempWriter.write(UUID.randomUUID().toString() + ",");
                tempWriter.write(name + "," + description + "," + location + ",");
                tempWriter.write(startTime + "," + endTime + ",");
                tempWriter.write(startDate + "," + endDate + "," + category + '\n');
            }

            else {
                tempWriter.write(line);
            }
            System.out.println(line);
        }

        // Display a little bubble to notify the user of the save
        Toast.makeText(getBaseContext(), "Event Saved", Toast.LENGTH_LONG).show();
        tempWriter.flush();
        tempWriter.close();

        BufferedReader tempReader = new BufferedReader(new FileReader(tempFile));
        FileWriter writer = new FileWriter(file, false);

        while((line = tempReader.readLine())!=null) {
            System.out.println(line);
            writer.write(line + '\n');
        }

        writer.flush();
        writer.close();
        tempReader.close();
        tempFile.delete();
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
        uuid = tokenizer.next();
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

    public void syncCalendar(int type, String s) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String id = Build.BOARD + Build.BOOTLOADER + Build.ID + Build.SERIAL;
            id = new String(Hex.encodeHex(DigestUtils.sha(id)));

            System.out.println(id);

            //Opens a connection to the server
            URL u = new URL("http://calendarse-maveptp.rhcloud.com/serv.j");
            URLConnection connection = u.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream output = connection.getOutputStream();

            //Byte array for return message from server
            byte[] t = null;

            //Holds message to be sent to server
            String message = null;

            //Will add an event to the database
            if(type == 0) {
                message = " ADD," + id + "," + s;
                output.write(message.getBytes());
                output.close();

                InputStream input = connection.getInputStream();
                t = new byte[input.available()];
                input.read(t);
                System.out.println(new String(t));
                input.close();
            }

            /*
            *    Will delete an event from the database
            *    When implementing, just pass in the UUID of the event as a string, and the
            *    Servlet will take care of the rest.
            */
            else if(type == 1){
                message = " REMOVE," + id + "," + s;
                output.write(message.getBytes());
                output.close();

                InputStream input = connection.getInputStream();
                t = new byte[input.available()];
                input.read(t);
                input.close();
            }

            //Will edit an existing event, for the string, please pass in all the fields for the event, use the same UUID as before
            else if(type == 2){
                message = " EDIT," + id + "," + s;
                output.write(message.getBytes());
                output.close();

                InputStream input = connection.getInputStream();
                t = new byte[input.available()];
                input.read(t);
                input.close();
            }

            //Method for downloading all the user's events, all events are downloaded at once and seperated by \n's.
            else if(type == 3){
                message = " DOWNLOAD," + id;
                output.write(message.getBytes());
                output.close();

                InputStream input = connection.getInputStream();
                t = new byte[input.available()];
                input.read(t);
                input.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}