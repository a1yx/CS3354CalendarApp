package com.example.kevin.calendarapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.kevin.calendarapp.R;

import java.util.Date;

/**
 * Created by MacProJJ on 11/12/14.
 */
public class Daily_View extends Activity {

    Button viewsButton, searchButton, agendaButton, addButton, settingsButton;
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
                    startActivity(intent);
                }
            }
        });
    }
}
