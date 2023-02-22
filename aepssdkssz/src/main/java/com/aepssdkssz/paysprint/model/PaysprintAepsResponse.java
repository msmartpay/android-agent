package com.aepssdkssz.paysprint.model;

import com.google.gson.annotations.SerializedName;

public class PaysprintAepsResponse {

    @SerializedName("Status")
    private int status;
    private String message;
    private PaysprintAepsData payload;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaysprintAepsData getPayload() {
        return payload;
    }

    public void setPayload(PaysprintAepsData payload) {
        this.payload = payload;
    }

}

