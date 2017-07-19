package com.example.djordje.seeds.device;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.example.djordje.seeds.Helper;
import com.example.djordje.seeds.MainActivity;
import com.example.djordje.seeds.R;
import com.example.djordje.seeds.device_reading.DeviceReading;
import com.example.djordje.seeds.device_reading.DeviceReadingWrapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

    public static void showSelected(Context ctx, int[] ids, Date start, Date end) {
        if(context == null)
            context = ctx;

        new HttpRequestTask().execute();
    }

    public void getAll() {
        new HttpRequestTask().execute();
    }
    //HTTP
    private static class HttpRequestTask extends AsyncTask<Void, Void, List<Device>> {
        @Override
        protected List<Device> doInBackground(Void... params) {
            try {
                String url = context.getString(R.string.server_address);// + "http://192.168.1.8:5000/device/from/01-07-2017 00:00:00/to/15-07-2017 23:59:59";
                url += "/device/from/";
                Calendar endDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                startDate.add(Calendar.DATE, -18);
                url += Helper.formatCalendar(startDate, null);
                url += "/to/";
                url += Helper.formatCalendar(endDate, null);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                MultiValueMap<String,Integer> map = new LinkedMultiValueMap<>();
                map.add("ids",1);
                map.add("ids",2);
                List<Device> devices = restTemplate.postForObject(url, map, List.class);
                return devices;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Device> result) {
//            System.out.println(result.toString());

            ObjectMapper mapper = new ObjectMapper();
            Device d;
            Device[] devices_array = new Device[result.size()];

            for(int i = 0; i < result.size(); i++) {
                d = mapper.convertValue(result.get(i), Device.class);
                devices_array[i] = d;
            }

            final ListView listview = (ListView) ((MainActivity)context).findViewById(R.id.charts_wrapper);
            DeviceAdapter dAdapter = new DeviceAdapter(((MainActivity)context).getApplicationContext(), devices_array);

            listview.setAdapter(dAdapter);
        }

    }

}
