package com.example.phrapp.sampledata;

public class Request_Model_Get_Otp {
    String healthid;
//    @SerializedName("userId")
//    int userId;

    public String getHealthid() {
        return healthid;
    }

    public void setHealthid(String healthid) {
        this.healthid = healthid;
    }

    public Request_Model_Get_Otp(String healthid) {
        this.healthid = healthid;
    }
}
