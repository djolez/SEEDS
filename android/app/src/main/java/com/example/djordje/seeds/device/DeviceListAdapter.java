package com.example.djordje.seeds.device;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.djordje.seeds.R;

import java.util.List;

/**
 * Created by Djordje on 22-Jul-17.
 */

public class DeviceListAdapter extends ArrayAdapter<Device> {
    private Context context;
    private Device[] devices;

    public DeviceListAdapter(Context context, Device[] devices) {
        super(context, -1, devices);
        this.context = context;
        this.devices = devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.device_checkbox, null);
        final Device d = this.devices[position];

        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.device_checkbox);
        TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
        deviceName.setText(d.getName());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                d.setChecked(b);
            }
        });

        return convertView;
    }

    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Device[] getDevices() {
        return devices;
    }

    public void setDevices(Device[] devices) {
        this.devices = devices;
    }
}
