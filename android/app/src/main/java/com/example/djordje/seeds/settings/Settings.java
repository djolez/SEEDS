package com.example.djordje.seeds.settings;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.example.djordje.seeds.Helper;
import com.example.djordje.seeds.MainActivity;
import com.example.djordje.seeds.R;
import com.example.djordje.seeds.device.Device;
import com.example.djordje.seeds.device.DeviceAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Djordje on 19-Jul-17.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings {
    private static  Context context;
    private int check_interval_minutes;
    private int poll_interval_minutes;
    private List<SensorRange> sensor_ranges;
    private List<DeviceSchedule> device_schedules;
    private List<Device> devices;

    public Settings(Context context){
        this.context = context;
    }
    private class SensorRange {
        private int device_id;
        private List<HashMap<Integer, Integer>> range;
    }

    private class DeviceSchedule {
        private int device_id;
        private List<HashMap<Calendar, Calendar>> periods;
    }

    public void getAllDevices() {
        //new RetrieveDevicesTask(this).execute();
//        new Device.HttpRequestTask().execute();
    }

    public void initData() {

    }

    //HTTP
    private static class RetrieveDevicesTask extends AsyncTask<Void, Void, List<Device>> {
        Settings parent;

        public RetrieveDevicesTask(Settings parent) {
            this.parent = parent;
        }

        @Override
        protected List<Device> doInBackground(Void... params) {
            try {
                //String url = context.getString(R.string.server_address);
                String url = context.getString(R.string.server_address)+"/board/1/device";

                RestTemplate restTemplate = new RestTemplate();
                List<Device> devices = restTemplate.getForObject(url, List.class);
                this.parent.devices = devices;
                return devices;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Device> result) {
//            System.out.println(result.toString());

//            ObjectMapper mapper = new ObjectMapper();
//            Device d;
//            Device[] devices_array = new Device[result.size()];
//
//            for(int i = 0; i < result.size(); i++) {
//                d = mapper.convertValue(result.get(i), Device.class);
//                devices_array[i] = d;
//            }
//
//            final ListView listview = (ListView) ((MainActivity)context).findViewById(R.id.charts_wrapper);
//            DeviceAdapter dAdapter = new DeviceAdapter(((MainActivity)context).getApplicationContext(), devices_array);
//
//            listview.setAdapter(dAdapter);
        }

    }
}
