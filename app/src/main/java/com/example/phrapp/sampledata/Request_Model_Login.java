package com.example.phrapp.sampledata;

public class Request_Model_Login {
    String otp;
    String txnid;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public Request_Model_Login(String otp, String txnid) {
        this.otp = otp;
        this.txnid = txnid;
    }
}
