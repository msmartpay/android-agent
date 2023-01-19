package in.msmartpay.agent.network.model;

import com.google.gson.annotations.SerializedName;

public class MainResponse {
    @SerializedName("response-message")
    String responseMessage;
    @SerializedName("response-code")
    String responseCode;

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
}
