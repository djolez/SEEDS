package com.example.djordje.seeds.settings;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.djordje.seeds.MainActivity;
import com.example.djordje.seeds.R;
import com.example.djordje.seeds.SettingsActivity;
import com.example.djordje.seeds.device.Device;
import com.example.djordje.seeds.device.DeviceSettingsAdapter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Djordje on 19-Jul-17.
 */

//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreProperties({"context","devices"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Settings {
    @JsonIgnore
    private Context context;
    @JsonIgnore
    private DeviceSettingsAdapter dAdapter;

    private int check_interval_minutes;
    private int poll_interval_minutes;
    private List<SensorRange> value_ranges;
    private List<DeviceSchedule> device_schedule;
    @JsonIgnore
    private List<Device> devices;

    public Settings(){}
    @JsonIgnore
    public Settings(Context context){
        this.context = context;
    }


    @JsonIgnore
    public void getAllSettings() {
        new RetrieveSettingsTask(context).execute();
    }

    @JsonIgnore
    public void saveSettings(){

        new SendSettingsTask(context,this).execute();
    }

    @JsonIgnore
    public Context getContext() {
        return context;
    }
    @JsonIgnore
    public void setContext(Context context) {
        this.context = context;
    }

    public int getCheck_interval_minutes() {
        return check_interval_minutes;
    }

    public void setCheck_interval_minutes(int check_interval_minutes) {
        this.check_interval_minutes = check_interval_minutes;
    }

    public int getPoll_interval_minutes() {
        return poll_interval_minutes;
    }

    public void setPoll_interval_minutes(int poll_interval_minutes) {
        this.poll_interval_minutes = poll_interval_minutes;
    }

    public List<SensorRange> getValue_ranges() {
        return value_ranges;
    }

    public void setValue_ranges(List<SensorRange> value_ranges) {
        this.value_ranges = value_ranges;
    }

    public List<DeviceSchedule> getDevice_schedule() {
        return device_schedule;
    }

    public void setDevice_schedule(List<DeviceSchedule> device_schedule) {
        this.device_schedule = device_schedule;
    }
    @JsonIgnore
    public List<Device> getDevices() {
        return devices;
    }
    @JsonIgnore
    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    //HTTP

    class SendSettingsTask extends AsyncTask<Void, Void, String> {
        Context ctx;
        Settings toSend;
        public SendSettingsTask(Context context,Settings toSend) {
            this.ctx = context;
            this.toSend = toSend;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //String url = context.getString(R.string.server_address);
                String url = ctx.getString(R.string.server_address)+"/settings";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                //ObjectMapper mapper = new ObjectMapper();
                //String jsonInString = mapper.writeValueAsString(toSend);

                HttpEntity<Settings> requestEntity = new HttpEntity<Settings>(toSend,headers);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                String res = restTemplate.postForObject(url,requestEntity,String.class);
                return res;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                Snackbar.make(((SettingsActivity)context).findViewById(R.id.settings_main_linearlayout), "An error occured while saving settings on server", Snackbar.LENGTH_LONG)
                        .show();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String a) {
            if(a.equals("OK"))
                Snackbar.make(((SettingsActivity)context).findViewById(R.id.settings_main_linearlayout), "Settings correctly saved on server", Snackbar.LENGTH_LONG)
                    .show();
            else
                Snackbar.make(((SettingsActivity)context).findViewById(R.id.settings_main_linearlayout), "An error occured while saving settings on server", Snackbar.LENGTH_LONG)
                        .show();
            return;
        }

    }
    //HTTP
    class RetrieveSettingsTask extends AsyncTask<Void, Settings, Settings> {
        Context ctx;

        public RetrieveSettingsTask(Context context) {
            this.ctx = context;
        }

        @Override
        protected Settings doInBackground(Void... params) {
            try {
                //String url = context.getString(R.string.server_address);
                String url = ctx.getString(R.string.server_address)+"/settings";



                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


                Settings s = restTemplate.getForObject(url, Settings.class);
                return s;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Settings result) {
            ListView view = (ListView) ((SettingsActivity) ctx).findViewById(R.id.device_schedule_list);

            dAdapter = new DeviceSettingsAdapter(ctx, MainActivity.getAvailable_devices(),result);
            view.setAdapter(dAdapter);

            EditText poll  = (EditText) ((SettingsActivity) ctx).findViewById(R.id.polling_interval);
            EditText check = (EditText) ((SettingsActivity) ctx).findViewById(R.id.check_interval);
            if(result!=null) {
                poll.setText(Integer.toString(result.getPoll_interval_minutes()));
                check.setText(Integer.toString(result.getCheck_interval_minutes()));
            }

            SettingsActivity.setSettingsToSave(result);
        }

    }


}

