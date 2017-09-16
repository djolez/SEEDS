package com.example.djordje.seeds;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.djordje.seeds.device.Device;
import com.example.djordje.seeds.device.DeviceAdapter;

import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_ACTIVITY_REQUEST_CODE = 1;
    public static Date start_date = new Date();
    public static Date end_date = new Date();
    TextView start_date_text;
    TextView end_date_text;
    public static int[] available_devices_ids;
    public static String[] available_devices_names;
    private int[] selectedDevicesIds;


    private static Device[] available_devices;
    private SwipeRefreshLayout refreshLayout;

    public static Device[] getAvailable_devices() {
        return available_devices;
    }

    public static void setAvailable_devices(Device[] available_devices) {
        MainActivity.available_devices = available_devices;
    }
    //ON_SYSTEM_EVENTS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_search);

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: read settings from local storage
                if(selectedDevicesIds==null) //this is the first time you call this, it means you never selected devices to show
                    Device.showSelected(MainActivity.this, available_devices_ids, start_date, end_date);
                else
                    Device.showSelected(MainActivity.this, selectedDevicesIds, start_date, end_date);
            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        refreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimaryDark,
                R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(selectedDevicesIds==null) //this is the first time you call this, it means you never selected devices to show
                    Device.showSelected(MainActivity.this, available_devices_ids, start_date, end_date);
                else
                    Device.showSelected(MainActivity.this, selectedDevicesIds, start_date, end_date);

            }
        });
        new Device.RetrieveDeviceListTask(MainActivity.this,"MainActivity").execute();
    }

    @Override
    public void onBackPressed() {
        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        this.finish();
        super.onBackPressed();
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
            //if you previously selected some device, then call the activity showing them selected
            if(selectedDevicesIds!= null && selectedDevicesIds.length != 0)
                startSettingsActivityIntent.putExtra("AvailableDevicesIDs", selectedDevicesIds);
            else
                startSettingsActivityIntent.putExtra("AvailableDevicesIDs", available_devices_ids);

            //startSettingsActivityIntent.putExtra("AvailableDevices",available_devices_names);
            startSettingsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(startSettingsActivityIntent,SETTINGS_ACTIVITY_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SETTINGS_ACTIVITY_REQUEST_CODE) : {
                if (resultCode == RESULT_OK) {
                    selectedDevicesIds = data.getIntArrayExtra("SelectedDevicesIds");
                    System.out.println(Arrays.toString(selectedDevicesIds));
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
