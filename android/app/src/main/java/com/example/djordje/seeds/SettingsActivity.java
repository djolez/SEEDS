package com.example.djordje.seeds;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.djordje.seeds.device.Device;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {
    private String[] available_devices;
    private static ArrayList<Integer> available_devices_ids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        available_devices_ids = new ArrayList();
        getAvailableDeviceNames();
        getAvailableDeviceIds();
        new Device.RetrieveDeviceListTask(SettingsActivity.this, "SettingsActivity").execute();
    }

    private void getAvailableDeviceIds() {
        int [] devs = this.getIntent().getIntArrayExtra("AvailableDevicesIDs");

        if(devs == null)
            return;

        for(int i =0 ;i<devs.length;i++){
            System.out.println(devs[i]);
            available_devices_ids.add(devs[i]);
        }
        //available_devices_ids.addAll(Arrays.asList(this.getIntent().getIntArrayExtra("AvailableDevicesIDs")));
        //System.out.println(Arrays.asList(this.getIntent().getIntArrayExtra("AvailableDevicesIDs")).get(0));
    }

    public void getAvailableDeviceNames() {
        available_devices =this.getIntent().getStringArrayExtra("AvailableDevices");
    }

    public static void addSelectedDevicesList(Integer id){
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
