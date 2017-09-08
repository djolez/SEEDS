package com.example.djordje.seeds.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.djordje.seeds.R;
import com.example.djordje.seeds.SettingsActivity;
import com.example.djordje.seeds.settings.Settings;

/**
 * Created by matteo on 9/7/17.
 */

public class DeviceSettingsAdapter  extends ArrayAdapter<Device> {
    private Context context;
    private Device[] devices;

    public DeviceSettingsAdapter(Context context, Device[] devices) {
        super(context, -1, devices);
        this.context = context;
        this.devices = devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.device_settings, null);
        EditText deviceName = (EditText) convertView.findViewById(R.id.device_settings_name);
        final CheckBox selected = (CheckBox) convertView.findViewById(R.id.device_selected_checkbox);

        final Device d = this.devices[position];

        //...is this possible, Android?
        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected.isChecked())
                    SettingsActivity.addSelectedDevicesList(new Integer(d.getId()));
                else
                    SettingsActivity.removeSelectedDevicesList(new Integer(d.getId()));

            }
        });
        deviceName.setText(d.getName());
        selected.setChecked(true);



        return convertView;
    }

}