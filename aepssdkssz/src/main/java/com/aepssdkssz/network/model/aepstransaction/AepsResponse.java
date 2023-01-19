package com.aepssdkssz.network.model.aepstransaction;

public class AepsResponse {

    private int status;
    private String message;
    private AepsData data;

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

    public AepsData getData() {
        return data;
    }

    public void setData(AepsData data) {
        this.data = data;
    }
}

