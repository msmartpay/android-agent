package in.msmartpay.agent.network.model;

import com.google.gson.annotations.SerializedName;

public class MainResponse2 {
    @SerializedName("Status")
    String status;
    @SerializedName("message")
    String message;

    @SerializedName("response-message")
    String responseMessage;
    @SerializedName("response-code")
    String responseCode;

    private String userCode;
    @SerializedName("data")
    Object data;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }



    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
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
