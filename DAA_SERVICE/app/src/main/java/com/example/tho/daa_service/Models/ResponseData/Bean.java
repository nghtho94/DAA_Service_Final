package com.example.tho.daa_service.Models.ResponseData;

import java.io.Serializable;

/**
 * Created by tho on 1/15/17.
 */

public class Bean implements Serializable {

    public boolean isChecked;


    String name;
    String job;
    String time;

    public Bean(String name, String time,String job, boolean success){
        this.name = name;
        this.time = time;
        this.job = job;
        this.isChecked = success;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getJob() {
        return job;
    }
}

