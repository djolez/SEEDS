package com.example.djordje.seeds.device;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.djordje.seeds.R;
import com.example.djordje.seeds.settings.DeviceSchedule;
import com.example.djordje.seeds.settings.Schedule;
import com.example.djordje.seeds.settings.Settings;

import java.util.ArrayList;

/**
 * Created by matteo on 9/10/17.
 */

public class DeviceScheduleAdapter extends ArrayAdapter<Schedule> {
    private Schedule[] devs;
    private Context context;
    private Settings settings;
    private int parentID;
    private int positionInList;

    public DeviceScheduleAdapter(Context context, Schedule[] devices, Settings settings,int parentID){
        super(context,-1,devices);
        this.context= context;
        this.devs = devices;
        this.settings = settings;
        this.parentID = parentID;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.schedule_layout, null);
        EditText scheduleOnHour = (EditText) convertView.findViewById(R.id.schedule_on_hour);
        EditText scheduleOnMinute = (EditText) convertView.findViewById(R.id.schedule_on_minute);
        EditText scheduleOnSecond = (EditText) convertView.findViewById(R.id.schedule_on_sec);

        if(getSchedulesForThisDevice()!=null) {
            for(int i = 0; i<settings.getDevice_schedule().get(positionInList).getSchedule().size();i++) {
                scheduleOnHour.setText("" + settings.getDevice_schedule().get(positionInList).getSchedule().get(i).getOn().getHour());
                scheduleOnMinute.setText("" + settings.getDevice_schedule().get(positionInList).getSchedule().get(i).getOn().getMinute());
                scheduleOnSecond.setText(""+settings.getDevice_schedule().get(positionInList).getSchedule().get(i).getOn().getSecond());

            }

        }


        return convertView;
    }

    private ArrayList<Schedule> getSchedulesForThisDevice(){
        ArrayList deviceSchedules = new ArrayList(settings.getDevice_schedule());
        for(int i = 0; i<deviceSchedules.size();i++){
            DeviceSchedule tmp = (DeviceSchedule)deviceSchedules.get(i);
            positionInList = i;
            if(tmp.getId() == parentID)
                return (ArrayList)tmp.getSchedule();
        }
        return null;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
