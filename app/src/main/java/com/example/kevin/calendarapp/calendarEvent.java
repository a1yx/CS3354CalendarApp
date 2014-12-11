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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Contains all the information and functions of the events.
 * Includes creating, editing, and removing events.
 * Sets up all the buttons and listeners for the event.
 * Validates all input fields
 *
 * @author Kevin Szwagiel, Garth Gulickson
 */

public class calendarEvent extends Activity {

	// Set up all default values
    private static Context context;
    Button submitButton, cancelButton, deleteButton;
	String uuid = "", name, description, location, startTime, endTime, startDate, endDate, category;
	TextView random;
	Calendar beginEvent, endEvent;
	long calID, eventID, startLong, endLong;
    int kind;

    /**
     * Handles the event when it is created.
     * Creates all the buttons and creates listeners for each button.
     * Validates input for each field
     *
     * @author Kevin Szwagiel, Garth Gulickson
     * @param intent contains information of an event, if an event exists
     * @exception e catches any exception that occurs during input validation
     * @exception e catches IOException and FileNotFoundException for file IO
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_event);
        submitButton = (Button)findViewById(R.id.submit_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        deleteButton = (Button)findViewById(R.id.delete_button);

        kind = 0;

        Intent intent = getIntent();

        final Uri fileUri = Uri.parse(intent.getExtras().getString("fileUri"));
        final File file = new File(fileUri.getPath());

        if(!intent.getExtras().getString("Event").equals("")){
            String[] event = intent.getExtras().getString("Event").split(",");
            ((EditText)findViewById(R.id.name_edit)).setText(event[0]);
            ((EditText)findViewById(R.id.description_edit)).setText(event[1]);
            ((EditText)findViewById(R.id.location_edit)).setText(event[2]);
            ((EditText)findViewById(R.id.startTime_edit)).setText(event[3]);
            ((EditText)findViewById(R.id.endTime_edit)).setText(event[4]);
            ((EditText)findViewById(R.id.startDate_edit)).setText(event[5]);
            ((EditText)findViewById(R.id.endDate_edit)).setText(event[6]);
            ((EditText)findViewById(R.id.category_edit)).setText(event[7]);
            uuid = event[8];
            kind = 1;
        }

        deleteButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(kind == 0){
                    Toast.makeText(getBaseContext(), "Cannot Delete Event, Doesn't Exist Yet", Toast.LENGTH_LONG).show();
                }
                else {
                    syncCalendar(2, uuid);
                    Toast.makeText(getBaseContext(), "Event Deleted", Toast.LENGTH_LONG).show();
                    calendarEvent.this.finish();
                }
            }
        });

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

                String[] checker = startDate.split("/");
                try {
                    if (Integer.parseInt(checker[0]) > 12 || Integer.parseInt(checker[0]) < 1 || Integer.parseInt(checker[1]) < 1 || Integer.parseInt(checker[1]) > 31) {
                        Toast.makeText(getBaseContext(), "Invalid Start Date", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                catch(Exception e){
                    Toast.makeText(getBaseContext(), "Invalid Start Date", Toast.LENGTH_LONG).show();
                    return;
                }

                checker = endDate.split("/");
                try {
                    if (Integer.parseInt(checker[0]) > 12 || Integer.parseInt(checker[0]) < 1 || Integer.parseInt(checker[1]) < 1 || Integer.parseInt(checker[1]) > 31) {
                        Toast.makeText(getBaseContext(), "Invalid End Date", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                catch(Exception e){
                    Toast.makeText(getBaseContext(), "Invalid End Date", Toast.LENGTH_LONG).show();
                    return;
                }

                checker = startTime.split(":| ");
                try{
                    if(Integer.parseInt(checker[0]) > 12 || Integer.parseInt(checker[0]) < 0 || Integer.parseInt(checker[1]) < 0 || Integer.parseInt(checker[1]) > 59 || (!checker[2].equalsIgnoreCase("am") && !checker[2].equalsIgnoreCase("pm"))){
                        Toast.makeText(getBaseContext(), "Invalid Start Time", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                catch(Exception e){
                    Toast.makeText(getBaseContext(), "Invalid Start Time", Toast.LENGTH_LONG).show();
                    return;
                }
                checker = endTime.split(":| ");
                try{
                    if(Integer.parseInt(checker[0]) > 12 || Integer.parseInt(checker[0]) < 0 || Integer.parseInt(checker[1]) < 0 || Integer.parseInt(checker[1]) > 59 || (!checker[2].equalsIgnoreCase("am") && !checker[2].equalsIgnoreCase("pm"))){
                        Toast.makeText(getBaseContext(), "Invalid End Time", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                catch(Exception e){
                    Toast.makeText(getBaseContext(), "Invalid End Time", Toast.LENGTH_LONG).show();
                    return;
                }

                if(description.isEmpty()) description = "No Description";
                if(location.isEmpty()) location = "Not Specified";
                if(category.isEmpty()) category = "None";

                if(name.isEmpty()){((EditText)findViewById(R.id.name_edit)).setText("Name Required".toCharArray(),0,12); return; }
                if(startTime.isEmpty()){((EditText) findViewById(R.id.startTime_edit)).setText("Start Time Required".toCharArray(),0,19); return; }
                if(endTime.isEmpty()){((EditText)findViewById(R.id.endTime_edit)).setText("End Time Required".toCharArray(),0,17); return; }
                if(startDate.isEmpty()){((EditText) findViewById(R.id.startDate_edit)).setText("Start Date Required".toCharArray(),0,19); return; }
                if(endDate.isEmpty()){((EditText)findViewById(R.id.endDate_edit)).setText("End Date Required".toCharArray(),0,17); return; }

                String conf = checkConflicts();

                if(conf != null){
                    Toast.makeText(getBaseContext(), "Time Conflict With: " + conf, Toast.LENGTH_LONG).show();
                    return;
                }

                // Adds an event to calendar.csv
                if(uuid.isEmpty()) {
                    uuid = UUID.randomUUID().toString();
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
                }
                Toast.makeText(getBaseContext(), "Event Saved", Toast.LENGTH_LONG).show();
                syncCalendar(kind,uuid + "," + name + "," + description + "," + location +
                        "," + startTime + "," + endTime + "," + startDate + "," + endDate + "," + category);

                calendarEvent.this.finish();
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

    /**
     * Creates a new event and writes it to a file
     *
     * @author Kevin Szwagiel
     * @param writer The output stream that will write to a file
     * @exception IOException Thrown if IO fails
     */
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

    /**
     * Edits an event. Takes in an existing event and finds that event in the file.
     * It copies every line to a new file and only replaces the line that is different.
     *
     * @author Kevin Szwagiel
     * @param reader The file stream that will be read by the function
     * @param file The file that is being read. Used to change pointers.
     * @exception IOException Thrown if any IO operations fail
     */
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

    /**
     * @deprecated Since jdk 1.1. This function was used for debugging.
     *
     * Reads the file to pull data about the event. This is for offline use.
     * This is used to display event details
     *
     * @author Kevin Szwagiel
     * @param reader The file stream to read input
     * @exception IOException Thrown if an IO operation fails
     */
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
        name = tokenizer.next();
        description = tokenizer.next();
        location = tokenizer.next();
        startTime = tokenizer.next();
        endTime = tokenizer.next();
        startDate = tokenizer.next();
        endDate = tokenizer.next();
        category = tokenizer.next();
        reader.close();
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
            else if(type == 2){
                message = " REMOVE," + id + "," + s;
                output.write(message.getBytes());
                output.close();

                InputStream input = connection.getInputStream();
                t = new byte[input.available()];
                input.read(t);
                input.close();
            }

            //Will edit an existing event, for the string, please pass in all the fields for the event, use the same UUID as before
            else if(type == 1){
                message = " EDIT," + id + "," + s;
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

    public String checkConflicts(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        Date initialDate = null, endingDate = null;
        try {
            initialDate = sdf.parse(startDate + " " + startTime);
            endingDate = sdf.parse(endDate + " " + endTime);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String id = Build.BOARD + Build.BOOTLOADER + Build.ID + Build.SERIAL;
        id = new String(Hex.encodeHex(DigestUtils.sha(id)));
        String message = " DOWNLOAD," + id;

        //Sends a message to the server, reads it, and puts all valid events in the list from earlier
        try {
            URL u = new URL("http://calendarse-maveptp.rhcloud.com/serv.j");
            URLConnection connection = u.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream output = connection.getOutputStream();
            byte[] t = null;

            output.write(message.getBytes());
            output.close();

            InputStream input = connection.getInputStream();
            t = new byte[input.available()];
            input.read(t);

            String[] response = new String(t).split("\n");

            for (String str : response) {

                String[] others = str.split(",");
                String resp = others[5] + " " + others[3];
                Date checkThis = sdf.parse(resp);
                if(checkThis.after(initialDate) && checkThis.before(endingDate)){
                    return others[2];
                }
            }
            return null;

        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getMonth(String m){
        if(m.equals("January")) return "1";
        if(m.equals("February")) return "2";
        if(m.equals("March")) return "3";
        if(m.equals("April")) return "4";
        if(m.equals("May")) return "5";
        if(m.equals("June")) return "6";
        if(m.equals("July")) return "7";
        if(m.equals("August")) return "8";
        if(m.equals("September")) return "9";
        if(m.equals("October")) return "10";
        if(m.equals("November")) return "11";
        else{ return "12"; }
    }
}