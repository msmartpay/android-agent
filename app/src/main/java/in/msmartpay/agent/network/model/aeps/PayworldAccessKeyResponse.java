package in.msmartpay.agent.network.model.aeps;

import com.google.gson.annotations.SerializedName;

public class PayworldAccessKeyResponse {
    @SerializedName("response-message")
    String responseMessage;
    @SerializedName("response-code")
    String responseCode;

    PayworldAccessKey data;

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

    public PayworldAccessKey getData() {
        return data;
    }

    public void setData(PayworldAccessKey data) {
        this.data = data;
    }
}
