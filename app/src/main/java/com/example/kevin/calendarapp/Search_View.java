package com.example.kevin.calendarapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
 * @author Garth Gulickson
 */
public class Search_View extends Activity{

    Context context;
    Button submitButton, cancelButton;
    ListView list;
    EditText submission;
    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.search_view);

        Intent intent = getIntent();

        submitButton = (Button)findViewById(R.id.submit_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        submission = (EditText)findViewById(R.id.search_text);
        list = (ListView)findViewById(R.id.listItems);

        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                initializeSearch(getIntent());
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, calendarEvent.class);
                intent.putExtra("fileUri","");
                intent.putExtra("Event",items.get(position));
                startActivity(intent);
            }
        });

    }

    /**
     *
     * @param intent
     */
    public void initializeSearch(Intent intent){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String request = ((EditText)findViewById(R.id.search_text)).getText().toString();

        String id = Build.BOARD + Build.BOOTLOADER + Build.ID + Build.SERIAL;
        id = new String(Hex.encodeHex(DigestUtils.sha(id)));
        String message = " DOWNLOAD," + id;
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
                if(StringUtils.containsIgnoreCase(str,request)){
                    String[] others = str.split(",");
                    eventList.add(others[0] + " - " + others[1] + " - " + others[2]);
                    items.add(str);
                }
                System.out.println(str);
            }

            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.row, eventList);
            list.setAdapter(adapter);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

}