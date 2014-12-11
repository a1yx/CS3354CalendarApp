package com.example.kevin.calendarapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by MacProJJ on 11/12/14.
 */
public class Edit_Bar extends Activity implements OnItemSelectedListener {
    Spinner view_spinner;
    Button search;
    Button agenda;
    Button add;
    Button settings;

    //testing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the main layout of the activity
        setContentView(R.layout.edit_bar_constant);

        view_spinner = (Spinner) findViewById(R.id.views_spinner);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.view_array,R.layout.edit_bar_constant);
        view_spinner.setAdapter(adapter);
    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        TextView selectedView = (TextView) view;
        // An item was selected. You can retrieve the selected item using
        Toast.makeText(this, "Changing view to " + selectedView.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
