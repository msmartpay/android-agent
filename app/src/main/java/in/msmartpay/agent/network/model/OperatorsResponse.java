package in.msmartpay.agent.network.model;

import com.google.gson.annotations.SerializedName;
import in.msmartpay.agent.network.model.wallet.OperatorModel;

import java.util.List;

public class OperatorsResponse {
    @SerializedName("response-message")
    String responseMessage;
    @SerializedName("response-code")
    String responseCode;
    List<OperatorModel> data;

    public List<OperatorModel> getData() {
        return data;
    }

    public void setData(List<OperatorModel> data) {
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
