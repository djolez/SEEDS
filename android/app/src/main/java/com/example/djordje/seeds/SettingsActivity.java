package com.example.djordje.seeds;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.djordje.seeds.device.Device;
import com.example.djordje.seeds.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {
    private String[] available_devices;
    private static ArrayList<Integer> available_devices_ids;
    private static Settings toSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        available_devices_ids = new ArrayList();
        //getAvailableDeviceNames();
        final EditText polling_interval = (EditText) findViewById(R.id.polling_interval);
        final EditText checking_interval = (EditText) findViewById(R.id.check_interval);
        FloatingActionButton done = (FloatingActionButton) findViewById(R.id.settings_done_button);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) done.getLayoutParams();
        done.setLayoutParams(p);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSave.setCheck_interval_minutes(Integer.parseInt(checking_interval.getText().toString()+""));
                toSave.setCheck_interval_minutes(Integer.parseInt(polling_interval.getText().toString()+""));
                toSave.setContext(SettingsActivity.this);
                toSave.saveSettings();

            }
        });

        getAvailableDeviceIds();
        //new Device.RetrieveDeviceListTask(SettingsActivity.this, "SettingsActivity").execute();
        toSave = new Settings(SettingsActivity.this);
        toSave.getAllDevices();
    }

    private void getAvailableDeviceIds() {
        int [] devs = this.getIntent().getIntArrayExtra("AvailableDevicesIDs");

        if(devs == null)
            return;

        for(int i =0 ;i<devs.length;i++){
            available_devices_ids.add(devs[i]);
        }
        //available_devices_ids.addAll(Arrays.asList(this.getIntent().getIntArrayExtra("AvailableDevicesIDs")));
        //System.out.println(Arrays.asList(this.getIntent().getIntArrayExtra("AvailableDevicesIDs")).get(0));
    }

    public void getAvailableDeviceNames() {
        available_devices =this.getIntent().getStringArrayExtra("AvailableDevices");
    }

    public static Settings getSettingsToSave(){
        return toSave;
    }

    public static void setSettingsToSave(Settings s){
        toSave = s;
    }

    public static void addSelectedDevicesList(Integer id){
        if(!available_devices_ids.contains(id))
            available_devices_ids.add(id);
    }
    public static void removeSelectedDevicesList(Integer id){
        available_devices_ids.remove(id);
    }


    @Override
    public void onBackPressed() {
        Intent goback = new Intent(this,MainActivity.class);
        goback.putExtra("SelectedDevicesIds",buildIntArray(available_devices_ids));
        goback.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        setResult(RESULT_OK,goback);
        finish();
        super.onBackPressed();

    }

    private int[] buildIntArray(ArrayList<Integer> integers) {
        int[] ints = new int[integers.size()];
        int i = 0;
        for (Integer n : integers) {
            ints[i++] = n;
        }
        return ints;
    }
    public static ArrayList<Integer> getSelectedDevices(){
        return available_devices_ids;
    }
}
