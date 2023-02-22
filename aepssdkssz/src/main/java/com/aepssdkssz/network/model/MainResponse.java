package com.aepssdkssz.network.model;

import com.google.gson.annotations.SerializedName;

public class MainResponse {
    int status;
    String message;
    @SerializedName("Status")
    int status2;

    public int getStatus2() {
        return status2;
    }

    public void setStatus2(int status2) {
        this.status2 = status2;
    }


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
}
