package com.aepssdkssz.network.model.paysprint;

import com.google.gson.annotations.SerializedName;

public class PaysprintTwoFactorResponse {
    private String message;
    @SerializedName("Status")
    private String status;

    private PaysprintTwoFactorResponseData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaysprintTwoFactorResponseData getData() {
        return data;
    }

    public void setData(PaysprintTwoFactorResponseData data) {
        this.data = data;
    }
}
