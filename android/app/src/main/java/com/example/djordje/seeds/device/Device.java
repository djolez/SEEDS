package com.example.djordje.seeds.device;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

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
    private String name;
    private int type;
    private String display_name;
    private int last_value;
    private HashMap<String, List<DeviceReading>> values;
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

    public int getLast_value() {
        return last_value;
    }

    public void setLast_value(int last_value) {
        this.last_value = last_value;
    }

    public HashMap<String, List<DeviceReading>> getValues() {
        return values;
    }

    public void setValues(HashMap<String, List<DeviceReading>> values) {
        this.values = values;
    }

    public static void showSelected(Context ctx, int[] ids, Date start, Date end) {
        if(context == null)
            context = ctx;


    }


    //HTTP
    private class HttpRequestTask extends AsyncTask<Void, Void, List<DeviceReadingWrapper>> {
        @Override
        protected List<DeviceReadingWrapper> doInBackground(Void... params) {
            try {
                final String url = context.getString(R.string.server_address);// + "http://192.168.1.8:5000/device/from/01-07-2017 00:00:00/to/15-07-2017 23:59:59";
                url += "/device/from/"

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                MultiValueMap<String,Integer> map = new LinkedMultiValueMap<>();
                map.add("ids",1);
                map.add("ids",2);
                List<DeviceReadingWrapper> readings = restTemplate.postForObject(url, map, List.class);
                return readings;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<DeviceReadingWrapper> result) {
            System.out.println(result.toString());

            ObjectMapper mapper = new ObjectMapper();
            DeviceReadingWrapper dr;
            DeviceReadingWrapper[] readings_array = new DeviceReadingWrapper[result.size()];

            for(int i = 0; i < result.size(); i++) {
                dr = mapper.convertValue(result.get(i), DeviceReadingWrapper.class);
                readings_array[i] = dr;
            }

            final ListView listview = (ListView) ((MainActivity)context).findViewById(R.id.charts_wrapper);
            DeviceAdapter dAdapter = new DeviceAdapter(((MainActivity)context).getApplicationContext(), readings_array);

            listview.setAdapter(dAdapter);
        }

    }

}
