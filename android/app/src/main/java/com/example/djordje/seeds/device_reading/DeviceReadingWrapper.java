package com.example.djordje.seeds.device_reading;

import com.example.djordje.seeds.board.Board;
import com.example.djordje.seeds.device.Device;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Djordje on 27-Jun-17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceReadingWrapper {
    private Board board;
    private Device device;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
