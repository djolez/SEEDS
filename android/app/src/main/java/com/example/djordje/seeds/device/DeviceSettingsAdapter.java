package com.example.djordje.seeds.device;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.djordje.seeds.MainActivity;
import com.example.djordje.seeds.R;
import com.example.djordje.seeds.SettingsActivity;
import com.example.djordje.seeds.settings.DeviceSchedule;
import com.example.djordje.seeds.settings.Schedule;
import com.example.djordje.seeds.settings.SensorRange;
import com.example.djordje.seeds.settings.Settings;
import com.example.djordje.seeds.settings.Timing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matteo on 9/7/17.
 */

public class DeviceSettingsAdapter  extends ArrayAdapter<Device> {
    private Context context;
    private Device[] devices;
    private Settings settings;
    private Settings settingsToSave;
    private String[] onHourString;
    private String[] onMinuteString;
    private String[] onSecondString;
    private String[] offHourString;
    private String[] offMinuteString;
    private String[] offSecondString;
    private String[] minValueString;
    private String[] maxValueString;
    List<SensorRange> value_ranges;
    List<DeviceSchedule> device_schedule;
    private boolean relayOrSwithc;

    public DeviceSettingsAdapter(Context context, Device[] devices, Settings settings) {
        super(context, -1, devices);
        this.context = context;
        this.devices = devices;
        this.settings = settings;
        int length = devices.length;
        onHourString = new String[length];
        onMinuteString = new String[length];
        onSecondString = new String[length];
        offHourString = new String[length];
        offMinuteString = new String[length];
        offSecondString = new String[length];
        minValueString = new String[length];
        maxValueString = new String[length];
        value_ranges = new ArrayList<>(devices.length);
        device_schedule = new ArrayList<>(devices.length);

        //Set the values in the settings layout to the ones retrieved from server, but do it only once(constructor)!
        for(int position=0 ;position<devices.length;position++) {
            Device curr = devices[position];
            int devType = curr.getType();

            if(devType == MainActivity.RELAY_type || devType == MainActivity.SWITCH_type)
                relayOrSwithc = true;
            else
                relayOrSwithc = false;

            if (!relayOrSwithc && settings != null && settings.getValue_ranges() != null && settings.getValue_ranges().get(0) != null && devices[position].getId() == settings.getValue_ranges().get(0).getDevice_id()) {
            SensorRange tmp = settings.getValue_ranges().get(position);
            minValueString[position] = tmp.getMin_value() + "";
            maxValueString[position] = tmp.getMax_value() + "";

            if (!relayOrSwithc && settings.getDevice_schedule() != null && settings.getDevice_schedule().get(position) != null && settings.getDevice_schedule().get(position).getSchedule() != null) {
                Timing on = settings.getDevice_schedule().get(position).getSchedule().get(0).getOn();
                Timing off = settings.getDevice_schedule().get(position).getSchedule().get(0).getOff();
                onHourString[position] = "" + on.getHour();
                onMinuteString[position] = "" + on.getMinute();
                onSecondString[position] = "" + on.getSecond();
                offHourString[position] = "" + off.getHour();
                offMinuteString[position] = "" + off.getMinute();
                offSecondString[position] = "" + off.getSecond();
            }
            }
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.device_settings, null);

        /*ListView scheduleList = (ListView) convertView.findViewById(R.id.schedule_list);
        DeviceSchedule[] deviceSchedules = settings.getDevice_schedule().toArray(new DeviceSchedule[settings.getDevice_schedule().size()]);

        for(int i =0; i<deviceSchedules.length;i++){
            if(deviceSchedules[i].getId() == devices[position].getId()) {
                DeviceScheduleAdapter scheduleAdapter = new DeviceScheduleAdapter(context, deviceSchedules[i].getSchedule().toArray(new Schedule[deviceSchedules[i].getSchedule().size()]), settings, devices[position].getId());
                scheduleList.setAdapter(scheduleAdapter);
                DeviceScheduleAdapter.setListViewHeightBasedOnChildren(scheduleList);
            }
        }*/
        ImageButton editButton = convertView.findViewById(R.id.edit_button);
        TextView deviceName = (TextView) convertView.findViewById(R.id.device_settings_name);
        final TextView deviceID = (TextView) convertView.findViewById(R.id.device_settings_id);
        final CheckBox selected = (CheckBox) convertView.findViewById(R.id.device_selected_checkbox);
        final LinearLayout scheduleLinearLayout = (LinearLayout) convertView.findViewById(R.id.value_range_and_schedule_layout);
        final TextView scheduleOnHour = (TextView) convertView.findViewById(R.id.schedule_on_hour);
        final TextView scheduleOnMinute = (TextView) convertView.findViewById(R.id.schedule_on_minute);
        final TextView scheduleOnSecond = (TextView) convertView.findViewById(R.id.schedule_on_sec);
        final TextView scheduleOffHour = (TextView) convertView.findViewById(R.id.schedule_off_hour);
        final TextView scheduleOffMinute = (TextView) convertView.findViewById(R.id.schedule_off_minute);
        final TextView scheduleOffSecond = (TextView) convertView.findViewById(R.id.schedule_off_sec);
        final TextView minValue = (TextView) convertView.findViewById(R.id.value_range_min);
        final TextView maxValue= (TextView) convertView.findViewById(R.id.value_range_max);

        final Device d = this.devices[position];

        int currType = d.getType();
        relayOrSwithc = (currType == MainActivity.RELAY_type || currType == MainActivity.SWITCH_type);

        //TODO fix me with the right code for relay devices
        if(d.getType() == 1000)
            scheduleLinearLayout.setVisibility(View.GONE);

        if(d.isChecked())
            selected.setChecked(true);
        else
            selected.setChecked(false);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.settings_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                TextView deviceName = (TextView) promptsView.findViewById(R.id.device_settings_name_dialog);
                LinearLayout scheduleAndRangeLinearLayoutDialog = (LinearLayout) promptsView.findViewById(R.id.value_range_and_schedule_dialog_layout);
                final EditText scheduleOnHour = (EditText) promptsView.findViewById(R.id.schedule_on_hour_dialog);
                final EditText scheduleOnMinute = (EditText) promptsView.findViewById(R.id.schedule_on_minute_dialog);
                final EditText scheduleOnSecond = (EditText) promptsView.findViewById(R.id.schedule_on_sec_dialog);
                final EditText scheduleOffHour = (EditText) promptsView.findViewById(R.id.schedule_off_hour_dialog);
                final EditText scheduleOffMinute = (EditText) promptsView.findViewById(R.id.schedule_off_minute_dialog);
                final EditText scheduleOffSecond = (EditText) promptsView.findViewById(R.id.schedule_off_sec_dialog);
                final EditText minValue = (EditText) promptsView.findViewById(R.id.value_range_max_dialog);
                final EditText maxValue= (EditText) promptsView.findViewById(R.id.value_range_min_dialog);
                deviceName.setText(d.getName());

                scheduleAndRangeLinearLayoutDialog.setVisibility(View.VISIBLE);
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("SAVE",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text

                                        onHourString[position] = scheduleOnHour.getText().toString();
                                        onMinuteString[position] = scheduleOnMinute.getText().toString();
                                        onSecondString[position] = scheduleOnSecond.getText().toString();
                                        offHourString[position] = scheduleOffHour.getText().toString();
                                        offMinuteString[position] = scheduleOffMinute.getText().toString();
                                        offSecondString[position] = scheduleOffSecond.getText().toString();
                                        maxValueString[position] = maxValue.getText().toString();
                                        minValueString[position] = minValue.getText().toString();
                                        notifyDataSetChanged();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });


        //...is this possible, Android?
        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected.isChecked()) {
                    settingsToSave = SettingsActivity.getSettingsToSave();
                    SettingsActivity.addSelectedDevicesList(new Integer(d.getId()));
                    d.setChecked(true);

                    DeviceSchedule deviceSchedule = new DeviceSchedule();
                    List<Schedule> schedule = new ArrayList<>(1);
                    Schedule s = new Schedule();
                    Timing on = new Timing();
                    Timing off = new Timing();
                    SensorRange sensorRange = new SensorRange();

                    sensorRange.setMax_value(Integer.parseInt(maxValueString[position]!= null && !maxValueString[position].isEmpty()? maxValueString[position] : "0"));
                    sensorRange.setMin_value(Integer.parseInt(minValueString[position]!=null && !minValueString[position].isEmpty()? minValueString[position]: "0"));
                    sensorRange.setDevice_id(Integer.parseInt(deviceID.getText().toString()));

                    if(value_ranges.size()<=position)
                        value_ranges.add(sensorRange);
                    else
                        value_ranges.set(position,sensorRange);

                    on.setHour(Integer.parseInt(onHourString[position]!=null && !onHourString[position].isEmpty()? onHourString[position]: "00"));
                    on.setMinute(Integer.parseInt(onMinuteString[position] != null && !onMinuteString[position].isEmpty()? onMinuteString[position]: "00"));
                    on.setSecond(Integer.parseInt(onSecondString[position]!=null && !onSecondString[position].isEmpty()?onSecondString[position] : "00"));
                    off.setHour(Integer.parseInt(offHourString[position]!=null && !offHourString[position].isEmpty()? offHourString[position]:"00"));
                    off.setMinute(Integer.parseInt(offMinuteString[position]!=null && !offMinuteString[position].isEmpty()? offMinuteString[position] : "00"));
                    off.setSecond(Integer.parseInt(onSecondString[position]!=null && !onSecondString[position].isEmpty()? onSecondString[position]:"00"));
                    s.setOn(on);
                    s.setOff(off);
                    schedule.add(s);

                    deviceSchedule.setId(Integer.parseInt(deviceID.getText().toString()));
                    deviceSchedule.setSchedule(schedule);

                    if(device_schedule.size()<=position)
                        device_schedule.add(deviceSchedule);
                    else
                        device_schedule.set(position,deviceSchedule);

                    settingsToSave.setValue_ranges(value_ranges);
                    settingsToSave.setDevice_schedule(device_schedule);
                    SettingsActivity.setSettingsToSave(settingsToSave);

                }
                else {
                    SettingsActivity.removeSelectedDevicesList(new Integer(d.getId()));
                    d.setChecked(false);
                }

            }
        });

        deviceName.setText(d.getName());
        deviceID.setText(""+d.getId());

        if(!relayOrSwithc) {
            scheduleLinearLayout.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            scheduleOffSecond.setText(offSecondString[position]);
            scheduleOffMinute.setText(offMinuteString[position]);
            scheduleOffHour.setText(offHourString[position]);
            scheduleOnSecond.setText(onSecondString[position]);
            scheduleOnMinute.setText(onMinuteString[position]);
            scheduleOnHour.setText(onHourString[position]);
            minValue.setText(minValueString[position]);
            maxValue.setText(maxValueString[position]);
        }
        else{
            scheduleLinearLayout.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
        }

        return convertView;
    }


}
