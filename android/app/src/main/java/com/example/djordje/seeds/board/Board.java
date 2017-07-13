package com.example.djordje.seeds.board;

import com.example.djordje.seeds.device.Device;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Djordje on 27-Jun-17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {
    private int id;
    private String name;
    private List<Device> devices;

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

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

//    public Board(int id, String name, List<Device> devices) {
//        this.id = id;
//        this.name = name;
//        this.devices = devices;
//    }


}
