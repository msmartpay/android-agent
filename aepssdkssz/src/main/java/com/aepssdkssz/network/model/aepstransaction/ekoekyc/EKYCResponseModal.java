
package com.aepssdkssz.network.model.aepstransaction.ekoekyc;

import com.google.gson.annotations.SerializedName;

public class EKYCRequestOTPResponse {

    @SerializedName("data")
    private EKYCRequestOtpResponseData data;
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

    public EKYCRequestOtpResponseData getData() {
        return data;
    }

    public void setData(EKYCRequestOtpResponseData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
