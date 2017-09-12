package com.example.djordje.seeds.settings;

/**
 * Created by matteo on 9/10/17.
 */

public class Schedule {
    private Timing off;
    private Timing on;

    public Timing getOff() {
        return off;
    }

    public void setOff(Timing off) {
        this.off = off;
    }

    public Timing getOn() {
        return on;
    }

    public void setOn(Timing on) {
        this.on = on;
    }
}
