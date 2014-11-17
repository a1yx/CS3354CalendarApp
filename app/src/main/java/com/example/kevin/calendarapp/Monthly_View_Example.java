package com.example.kevin.calendarapp;

/**
 * Created by MacProJJ on 11/12/14.
 */

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CalendarView;
        import android.widget.CalendarView.OnDateChangeListener;
        import android.widget.Toast;
        import android.app.Activity;

        import java.io.File;

public class Monthly_View_Example extends Activity {
    CalendarView calendar;
    Context context = this;
    Button viewsButton, searchButton, agendaButton, addButton, settingsButton;
    public final static String monthFinal = "com.example.kevin.calendarap.monthString";
    public final static String dayFinal = "com.example.kevin.calendarapp.dayString";
    public final static String yearFinal = "com.example.kevin.calendarapp.yearString";
    public Uri fileUri;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the main layout of the activity
        setContentView(R.layout.activity_monthly_view_example);

        //initializes the calendarview
        file = new File(context.getFilesDir(), "calendar.csv");
        fileUri = Uri.fromFile(file);
        initializeCalendar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_monthly__view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);


        //The background color for the selected week.
        //calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.white));

        //sets the color for the dates of an unfocused month.
        //calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        //calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        //calendar.setSelectedDateVerticalBar(R.color.pink);

        //**This has an OBOB error for month (month is month -1) -- gauche fix
        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            //show the selected date as a toast and pass as intent
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month = month + 1;
                Toast.makeText(getApplicationContext(), month + "/" + day + "/" + year, Toast.LENGTH_LONG).show();
                Context context = calendar.getContext();
                Intent intent = new Intent(context, Daily_View.class);
                String monthString = Months(month);
                intent.putExtra(monthFinal, monthString);
                String dayString = day + "";
                intent.putExtra(dayFinal, dayString);
                String yearString = year + "";
                intent.putExtra(yearFinal, yearString);
                intent.putExtra("fileUri", fileUri.toString());

                startActivity(intent);
            }
        });

        initializeEditBar();

    }

    public void initializeEditBar() {
        final Context context = this;
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
                   Intent intent = new Intent(context, Search_View.class);
                   startActivity(intent);
               }
           }
        });
    }

    // Switch statement to convert the int value of a month to a string
    public String Months(int month) {
        String monthString = "";
        switch(month) {
            case 1:  monthString = "January";
                break;
            case 2:  monthString = "February";
                break;
            case 3:  monthString = "March";
                break;
            case 4:  monthString = "April";
                break;
            case 5:  monthString = "May";
                break;
            case 6:  monthString = "June";
                break;
            case 7:  monthString = "July";
                break;
            case 8:  monthString = "August";
                break;
            case 9:  monthString = "September";
                break;
            case 10: monthString = "October";
                break;
            case 11: monthString = "November";
                break;
            case 12: monthString = "December";
                break;
        }

        return monthString;
    }
}
