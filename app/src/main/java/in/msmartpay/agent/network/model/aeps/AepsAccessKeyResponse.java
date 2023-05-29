package in.msmartpay.agent.network.model.aeps;

import com.google.gson.annotations.SerializedName;

public class AepsAccessKeyResponse {
    @SerializedName("response-message")
    String responseMessage;
    @SerializedName("response-code")
    String responseCode;

    AepsAccessKey data;

    public AepsAccessKey getData() {
        return data;
    }

    public void setData(AepsAccessKey data) {
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
}
