package com.example.djordje.seeds.settings;

/**
 * Created by matteo on 9/10/17.
 */

public class SensorRange {
    private int device_id;
    private int min_value;
    private int max_value;

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getMin_value() {
        return min_value;
    }

    public void setMin_value(int min_value) {
        this.min_value = min_value;
    }

    public int getMax_value() {
        return max_value;
    }

    public void setMax_value(int max_value) {
        this.max_value = max_value;
    }
}