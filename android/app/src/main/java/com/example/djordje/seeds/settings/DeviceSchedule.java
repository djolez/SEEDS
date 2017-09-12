package com.example.djordje.seeds.settings;

import java.util.List;

/**
 * Created by matteo on 9/10/17.
 */

public class DeviceSchedule {
    private int id;
    private List<Schedule> schedule;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }
}
