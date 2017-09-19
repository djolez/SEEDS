package com.example.djordje.seeds.device;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.djordje.seeds.Helper;
import com.example.djordje.seeds.MainActivity;
import com.example.djordje.seeds.R;
import com.example.djordje.seeds.device_reading.DeviceReading;
import com.example.djordje.seeds.device_reading.DeviceReadingWrapper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Djordje on 01-Jul-17.
 */

public class DeviceAdapter extends ArrayAdapter<Device> {
    private final Context context;
    private Device[] readings;

    public DeviceAdapter(Context context, Device[] readings) {
        super(context, -1, readings);
        this.context = context;
        this.readings = readings;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.device_chart, null);


        final TextView noDataText = (TextView) convertView.findViewById(R.id.no_data_text);
        LineChart mChart = (LineChart) convertView.findViewById(R.id.chart);

        TextView device_name_view = (TextView) convertView.findViewById(R.id.device_name);
        TextView last_value_view = (TextView) convertView.findViewById(R.id.last_value);

        if(readings == null || readings.length == 0) {
            mChart.removeAllViews();
            return null;
        }


        device_name_view.setText(this.readings[position].getName());
        last_value_view.setText(this.readings[position].getLast_value() + "");

        if(isDeviceReadingEmpty(this.readings[position]) == true) {
            noDataText.setVisibility(View.VISIBLE);
            mChart.setVisibility(View.GONE);
        } else {
            noDataText.setVisibility(View.GONE);
            mChart.setVisibility(View.VISIBLE);

            List<List<Entry>> chart_entries = new ArrayList<>();
            List<ILineDataSet> chart_iline_datasets = new ArrayList<>();

            Date[] xAxisDates = new Date[0];
            boolean xAxisInitialized = false;

            if(this.readings[position].getSub_devices().size() > 0) {
                int sub_device_counter = 0;

                for(Device sub_device: this.readings[position].getSub_devices()) {
                    chart_entries.add(new ArrayList<Entry>());
                    if(!xAxisInitialized)
                        xAxisDates = new Date[sub_device.getValues().size()];

                    int i = 0;
                    for(DeviceReading dr: sub_device.getValues()) {
                        if(!xAxisInitialized)
                            xAxisDates[i] = dr.getTimestamp();

                        Entry tmp_entry = new Entry(i, dr.getValue());
                        chart_entries.get(sub_device_counter).add(tmp_entry);
                        i++;

                        if(i == sub_device.getValues().size())
                            xAxisInitialized = true;
                    }

                    LineDataSet sub_device_line = new LineDataSet(chart_entries.get(sub_device_counter), sub_device.getName());
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                    sub_device_line.setColors(color);
                    sub_device_line.setDrawValues(false);
                    sub_device_line.setDrawCircles(false);

                    chart_iline_datasets.add(sub_device_line);
                    sub_device_counter++;
                }
            }

            if(this.readings[position].getValues().size() > 0) {
                int i = 0;
                xAxisDates = new Date[this.readings[position].getValues().size()];
                List<LineDataSet> lineData = new ArrayList<>();

                for(DeviceReading reading: this.readings[position].getValues()) {
                    chart_entries.add(new ArrayList<Entry>());

                    if(!xAxisInitialized)
                        xAxisDates[i] = reading.getTimestamp();

                    Entry tmp_entry = new Entry(i, reading.getValue());
                    chart_entries.get(0).add(tmp_entry);

                    if(++i == this.readings[position].getValues().size())
                        xAxisInitialized = true;
                }

                LineDataSet line = new LineDataSet(chart_entries.get(0), this.readings[position].getName());
                line.setDrawValues(false);
                line.setDrawCircles(false);

                chart_iline_datasets.add(line);
            }

            LineData chart_data = new LineData(chart_iline_datasets);
            mChart.setData(chart_data);
            XAxis xAxis = mChart.getXAxis();
            //xAxis.enableGridDashedLine(10f, 10f, 0f);
//        xAxis.setLabelCount(3, true);
//        xAxis.setGranularity(100);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new DateValueFormatter(xAxisDates));
            mChart.invalidate();

        }

        return convertView;

    }

    @Override
    public void clear() {
        this.readings = new Device[0];
    }

    private boolean isDeviceReadingEmpty(Device device) {
        if(device==null) return true;

        if( device.getSub_devices() != null && device.getSub_devices().size() > 0) {
            for(Device sd: device.getSub_devices()) {
                if(!sd.getValues().isEmpty())
                    return false;
            }
        }

        if(device.getValues()!=null && device.getValues().size() > 0)
            return false;
        return true;
    }


    public class DateValueFormatter implements IAxisValueFormatter{
        private Date[] dates;
        private int index = 0;

        public DateValueFormatter(Date[] dates) {
            this.dates = dates;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return Helper.formatDate(this.dates[(int) value], "HH:mm:ss");
        }
    }
}
