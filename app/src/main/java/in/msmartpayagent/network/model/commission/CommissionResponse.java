package in.msmartpayagent.network.model.commission;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommissionResponse {
    @SerializedName("response-code")
    String responseCode;
    @SerializedName("response-message")
    String responseMessage;
    List<CommissionModel> CommList;

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

    public List<CommissionModel> getCommList() {
        return CommList;
    }

    public void setCommList(List<CommissionModel> commList) {
        CommList = commList;
    }
}
