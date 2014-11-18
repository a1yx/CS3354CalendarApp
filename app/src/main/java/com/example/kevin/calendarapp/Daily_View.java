package com.example.kevin.calendarapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by MacProJJ on 11/12/14.
 */
public class Daily_View extends Activity {

    Context context;
    Button viewsButton, searchButton, agendaButton, addButton, settingsButton;
    ListView list;
    ArrayList<String> items;
    EditText datethingy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the main layout of the activity
        setContentView(R.layout.activity_daily_view);

        Intent intent = getIntent();
        context = this;

        datethingy = (EditText) findViewById(R.id.daily_date);

        //Creates list for displaying and selecting events and an array list to keep track of which
        //Event belongs to which list item
        list = (ListView)findViewById(R.id.eventList);
        items = new ArrayList<String>();

        initializeDaily(datethingy, intent);
    }

    public void initializeDaily(EditText datethingy, Intent intent){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String month, day, year;

        month = intent.getStringExtra(Monthly_View_Example.monthFinal);
        day = intent.getStringExtra(Monthly_View_Example.dayFinal);
        year = intent.getStringExtra(Monthly_View_Example.yearFinal);
        datethingy.setText(month + " " + day + ", " + year);

        //Gets a string readable by the server
        String request = (getMonth(month) + "/" + day + "/" + year);

        //Generates the phone's unique ID
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
            ArrayList<String> eventList = new ArrayList<String>();
            items = new ArrayList<String>();

            for (String str : response) {
                String[] others = str.split(",");
                if(StringUtils.containsIgnoreCase(others[5], request)){
                    String mes = (others[0] + " - " + others[3] + "-" + others[4]);
                    eventList.add(mes);
                    items.add(str);
                }
                System.out.println(str);
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row, eventList);
            list.setAdapter(adapter);

            //set color of the event
            for(int i = 0, current; i < list.getChildCount(); i++) {
                list.getChildAt(i).setBackgroundColor(Color.BLUE);
            }
        }

        catch(Exception e){
            e.printStackTrace();
        }
        initializeEditBar(intent);
    }

    public void initializeEditBar(Intent intent) {
        final Context context = this;
        final Uri fileUri = Uri.parse(intent.getExtras().getString("fileUri"));
        viewsButton = (Button) findViewById(R.id.change_views);
        searchButton = (Button) findViewById(R.id.search);
        agendaButton = (Button) findViewById(R.id.agenda);
        addButton = (Button) findViewById(R.id.add_event);
        settingsButton = (Button) findViewById(R.id.settings);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.add_event) {
                    Intent intent = new Intent(context, calendarEvent.class);
                    intent.putExtra("fileUri", fileUri.toString());
                    intent.putExtra("Event","");
                    startActivity(intent);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(view.getId() == R.id.search){
                    Intent intent = new Intent(getBaseContext(), Search_View.class);
                    startActivity(intent);
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, calendarEvent.class);
                intent.putExtra("fileUri","");
                intent.putExtra("Event",items.get(position));
                startActivity(intent);
            }
        });
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
