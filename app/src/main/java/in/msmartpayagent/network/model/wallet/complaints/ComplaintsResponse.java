package in.msmartpayagent.network.model.wallet.complaints;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ComplaintsResponse {
    @SerializedName("response-code")
    private String responseCode;
    @SerializedName("response-message")
    private String responseMessage;
    private List<ComplaintHistoryModel> data;

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

    public List<ComplaintHistoryModel> getData() {
        return data;
    }

    public void setData(List<ComplaintHistoryModel> data) {
        this.data = data;
    }
}
