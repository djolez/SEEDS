package com.example.djordje.seeds;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.djordje.seeds.device.Device;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static Date start_date = new Date();
    public static Date end_date = new Date();
    TextView start_date_text;
    TextView end_date_text;
    Button search_button;
    public static int[] available_devices_ids;
    public static String[] available_devices_names;
    private int[] selectedDevicesIds;
    private boolean firstStart=true;

    //ON_SYSTEM_EVENTS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDevicePicker();
            }
        });

        //CONFIGURE DATE FIELDS
        start_date_text = (TextView) findViewById(R.id.datepicker_start);
        end_date_text = (TextView) findViewById(R.id.datepicker_end);

        Date now = new Date();

        datetimeSelected(start_date, start_date_text, new Date(now.getYear(), now.getMonth(), now.getDate(), now.getHours() - 1, now.getMinutes()));
        datetimeSelected(end_date, end_date_text, now);

        start_date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker(start_date, start_date_text);
            }
        });

        end_date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepicker(end_date, end_date_text);
            }
        });

        //SEARCH BUTTON
        search_button = (Button) findViewById(R.id.button_search);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: read settings from local storage

                Device.showSelected(getApplicationContext(), available_devices_ids, start_date, end_date);
            }
        });

        new Device.RetrieveDeviceListTask("MainActivity").execute();
        Device.showSelected(this, available_devices_ids, start_date, end_date);
    }

    private void showDevicePicker() {
        final View dialogView = View.inflate(this, R.layout.device_picker_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO: read settings from local storage


//        new HttpRequestTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent startSettingsActivityIntent = new Intent(MainActivity.this, SettingsActivity.class);

            startSettingsActivityIntent.putExtra("AvailableDevicesIDs", available_devices_ids);
            startSettingsActivityIntent.putExtra("AvailableDevices",available_devices_names);
            startSettingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(startSettingsActivityIntent,1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == RESULT_OK) {
                    selectedDevicesIds = data.getIntArrayExtra("SelectedDevicesIds");
                    for(int i=0;i<selectedDevicesIds.length;i++) System.out.println("DEV"+selectedDevicesIds[i]);
                    Device.showSelected(MainActivity.this,selectedDevicesIds,start_date,end_date);
                }
                break;
            }
        }
    }

    //DATEPICKER

    private void datetimeSelected(Date datetime_model, TextView datetime_view, Date value) {
        datetime_model.setYear(value.getYear());
        datetime_model.setMonth(value.getMonth());
        datetime_model.setDate(value.getDate());
        datetime_model.setHours(value.getHours());
        datetime_model.setMinutes(value.getMinutes());

        datetime_view.setText(Helper.formatDate(datetime_model, "dd-MM-YYYY\nHH:mm"));
    }

    protected void showDatepicker(final Date datetime_model, final TextView datetime_view){

        final View dialogView = View.inflate(this, R.layout.datetime_dialog, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        datePicker.init(datetime_model.getYear() + 1900, datetime_model.getMonth(), datetime_model.getDate(), null);

        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);
        timePicker.setCurrentHour(datetime_model.getHours());
        timePicker.setCurrentMinute(datetime_model.getMinutes());

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                Date value = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                datetimeSelected(datetime_model, datetime_view, value);

                alertDialog.dismiss();
            }});
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

}
