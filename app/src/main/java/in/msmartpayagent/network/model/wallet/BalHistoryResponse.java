package in.msmartpayagent.network.model.wallet;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BalHistoryResponse {
    @SerializedName("response-code")
    String responseCode;
    @SerializedName("response-message")
    String responseMessage;
    List<BalHistoryModel> data;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<BalHistoryModel> getData() {
        return data;
    }

    public void setData(List<BalHistoryModel> data) {
        this.data = data;
    }
}
