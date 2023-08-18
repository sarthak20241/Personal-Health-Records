package com.example.phrapp;

import com.google.gson.annotations.SerializedName;

public class RequestModel {
//    @SerializedName("healthid")
    String healthid;
//    @SerializedName("userId")
//    int userId;

    public String getHealthid() {
        return healthid;
    }

    public void setHealthid(String healthid) {
        this.healthid = healthid;
    }

    public RequestModel(String healthid) {
        this.healthid = healthid;
    }
}
