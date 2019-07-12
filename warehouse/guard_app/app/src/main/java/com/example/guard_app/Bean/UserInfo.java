package com.example.guard_app.Bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;
import com.bin.david.form.data.column.Column;



public class UserInfo {

    private String id;
    private double longitude;
    private double latitude;
    private double altitude;
    private String status;
    private String speed;
    private String scope;
    private String time;

    public UserInfo(String id, double longitude, double latitude, double altitude, String status, String speed, String scope, String time) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.status = status;
        this.speed = speed;
        this.scope = scope;
        this.time = time;

    }

}


