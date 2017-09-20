package com.example.djordje.seeds.device;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.djordje.seeds.Helper;
import com.example.djordje.seeds.MainActivity;
import com.example.djordje.seeds.R;
import com.example.djordje.seeds.SettingsActivity;
import com.example.djordje.seeds.device_reading.DeviceReading;
import com.example.djordje.seeds.settings.Settings;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Djordje on 27-Jun-17.
 */

//public enum Type {
//    DS18B20 (0),
//    DHT11 (1);
//}

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
    private int id;
    private int parent_id;
    private String name;
    private int type;
    private String display_name;
    private float last_value;
    private float avg_value;
    private List<Device> sub_devices;
    private List<DeviceReading> values;
    private boolean checked = false;
    private static Context context;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public float getAvg_value() {
        return avg_value;
    }

    public void setAvg_value(float avg_value) {
        this.avg_value = avg_value;
    }

    public List<Device> getSub_devices() {
        return sub_devices;
    }

    public void setSub_devices(List<Device> sub_devices) {
        this.sub_devices = sub_devices;
    }

    public float getLast_value() {
        return last_value;
    }

    public void setLast_value(float last_value) {
        this.last_value = last_value;
    }

    public List<DeviceReading> getValues() {
        return values;
    }

    public void setValues(List<DeviceReading> values) {
        this.values = values;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Device.context = context;
    }

    public static void showSelected(Context ctx, int[] ids, Date start, Date end) {
        if(context == null)
            context = ctx;
        if(ids == null){
            new Device.RetrieveDeviceListTask(context,"MainActivity").execute();
        }else {
            RetrieveWithValuesTask r = new RetrieveWithValuesTask(ids, start, end, ctx);
            r.execute();
        }
    }

    //HTTP
    public static class RetrieveWithValuesTask extends AsyncTask<Void, Void, List<Device>> {
        private int[] devs;
        private Date start_date;
        private Date end_date;
        private SwipeRefreshLayout refreshLayout;
        private Context context;

        public RetrieveWithValuesTask (int[] devs, Date start, Date end, Context context){
            this.devs = devs;
            this.start_date = start;
            this.end_date = end;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            refreshLayout = (SwipeRefreshLayout) ((MainActivity) context).findViewById(R.id.swiperefresh);
            refreshLayout.setRefreshing(true);

        }

        @Override
        protected List<Device> doInBackground(Void... params) {
            try {
                /*if(!isInternetAvailable() || !isNetworkConnected(context)){
                    Log.d("Device class","doinbackground");
                    return null;
                }*/
                String url = context.getString(R.string.server_address);// + "http://192.168.1.8:5000/device/from/01-07-2017 00:00:00/to/15-07-2017 23:59:59";
                url += "/device/from/";
                // TODO: Change with the values that the user selected
//                Calendar endDate = Calendar.getInstance();
//                Calendar startDate = Calendar.getInstance();
//                startDate.add(Calendar.DATE, -18);
                url += Helper.formatDate(this.getStart_date(), null);
                url += "/to/";
                url += Helper.formatDate(this.getEnd_date(), null);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                MultiValueMap<String,Integer> map = new LinkedMultiValueMap<>();


                for (int id : this.devs) {
                    map.add("ids", id);
                }


                List<Device> devices = restTemplate.postForObject(url, map, List.class);
                return devices;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }


        @Override
        protected void onPostExecute(List<Device> result) {
            ObjectMapper mapper = new ObjectMapper();
            Device d;
            ListView listview = (ListView) ((MainActivity)context).findViewById(R.id.charts_wrapper);
            DeviceAdapter dAdapter;
            TextView noDataText = (TextView) ((MainActivity)context).findViewById(R.id.no_data_available_mainactivity);

            if(result == null || result.isEmpty()) {
                noDataText.setVisibility(View.VISIBLE);
                listview.setVisibility(View.INVISIBLE);
                Log.d("Device class","listview "+listview.getVisibility());
            }else {

                LinearLayout relayButtonsLayout = (LinearLayout) ((MainActivity) context).findViewById(R.id.relays_buttons_layout);
                relayButtonsLayout.removeAllViews(); //avoid inserting buttons for the same relay every time we refresh
                ArrayList<Device> temp = new ArrayList<>();
                for (int i = 0; i < result.size(); i++) {
                    d = mapper.convertValue(result.get(i), Device.class);
                    if(d.getType() == MainActivity.RELAY_type){
                        final Button relayButton = new Button(context);
                        relayButton.setText(d.getName());
                        relayButtonsLayout.addView(relayButton);
                        relayButton.setGravity(Gravity.CENTER);
                        final Device finalDevice = d;
                        relayButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //TODO: call API "device_write_value" to switch the value of a relay. This means, create new AsyncTask
                                if(relayButton.isPressed()) {
                                    new SetRelayValueTask(context, finalDevice.getId(), 0).execute();
                                    relayButton.setPressed(true);
                                }
                                else{
                                    new SetRelayValueTask(context,finalDevice.getId(),1).execute();
                                    relayButton.setPressed(false);
                                }

                            }
                        });
                        result.remove(d);
                    }else
                        temp.add(d);
                }
                Device[] devices_array;
                devices_array = temp.toArray(new Device[temp.size()]);

                dAdapter = new DeviceAdapter(((MainActivity) context), devices_array);

                noDataText.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
                listview.setAdapter(dAdapter);


            }
            refreshLayout.setRefreshing(false);
            //new Settings(context).getAllSettings();
        }

        public Date getStart_date() {
            return start_date;
        }

        public void setStart_date(Date start_date) {
            this.start_date = start_date;
        }

        public Date getEnd_date() {
            return end_date;
        }

        public void setEnd_date(Date end_date) {
            this.end_date = end_date;
        }

    }


    public static class SetRelayValueTask extends AsyncTask<Void, Void, String> {

        private Context cont;
        private int relayValue;
        private int relayID;
        public SetRelayValueTask(Context context, int relayID, int relayValue){
            this.cont = context;
            this.relayValue = relayValue;
            this.relayID = relayID;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                /*if(!isNetworkConnected(cont) || !isInternetAvailable()) {
                    return null;
                }*/
                String url = cont.getString(R.string.server_address);// + "http://192.168.1.8:5000/device/from/01-07-2017 00:00:00/to/15-07-2017 23:59:59";
                url += "/device/"+relayID+"/write/"+relayValue;

                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                String devices = restTemplate.getForObject(url, String.class);
                return devices;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Sending data to relay",result);
        }

    }

    public static class RetrieveDeviceListTask extends AsyncTask<Void, Void, List<Device>> {

        private String activity;
        private Context cont;
        public RetrieveDeviceListTask(Context context, String activity){
            this.cont = context;
            this.activity = activity;
        }

        @Override
        protected List<Device> doInBackground(Void... params) {
            try {
                /*if(!isNetworkConnected(cont) || !isInternetAvailable()) {
                    return null;
                }*/
                String url = cont.getString(R.string.server_address);// + "http://192.168.1.8:5000/device/from/01-07-2017 00:00:00/to/15-07-2017 23:59:59";
                url += "/board/1/device";

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                List<Device> devices = restTemplate.getForObject(url, List.class);
                return devices;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Device> result) {
            ObjectMapper mapper = new ObjectMapper();
            Device d;

            if(result == null || result.isEmpty()) {
                //dummy request.
                Device.showSelected(cont, new int[0], MainActivity.start_date, MainActivity.end_date);
                return;
            }

            Device[] devices_array = new Device[result.size()];

            for (int i = 0; i < result.size(); i++) {
                d = mapper.convertValue(result.get(i), Device.class);
                devices_array[i] = d;
                devices_array[i].setChecked(true);
            }

            MainActivity.setAvailable_devices(devices_array);
            MainActivity.available_devices_names = new String[devices_array.length];
            MainActivity.available_devices_ids = new int[devices_array.length];
            int i = 0;
            for (Device dv :devices_array) {
                MainActivity.available_devices_ids[i] = dv.getId();
                MainActivity.available_devices_names[i] = dv.getName();
                i++;
            }

            //TODO do we need to add stuff here?
            Device.showSelected(cont, MainActivity.available_devices_ids, MainActivity.start_date, MainActivity.end_date);
        }

    }

    public static boolean isNetworkConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            Log.d("Device class","internet not available");
            return false;
        }

    }
}
