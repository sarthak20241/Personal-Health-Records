package com.example.phrapp;

public class YourResponseModel {
    String txnId;

    public String getTxnId() {
        return txnId;
    }

    public YourResponseModel(String txnId) {
        this.txnId = txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
}
