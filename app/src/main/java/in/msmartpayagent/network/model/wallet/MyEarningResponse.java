package in.msmartpayagent.network.model.wallet;

import com.google.gson.annotations.SerializedName;

public class MyEarningResponse {
    @SerializedName("response-code")
    String responseCode;
    @SerializedName("response-message")
    String responseMessage;
    @SerializedName("response-reqamt")
    String responseReqamt;
    @SerializedName("response-deduction")
    String responseDeduction;
    @SerializedName("response-comm")
    String responseComm;

    public String getResponseReqamt() {
        return responseReqamt;
    }

    public void setResponseReqamt(String responseReqamt) {
        this.responseReqamt = responseReqamt;
    }

    public String getResponseDeduction() {
        return responseDeduction;
    }

    public void setResponseDeduction(String responseDeduction) {
        this.responseDeduction = responseDeduction;
    }

    public String getResponseComm() {
        return responseComm;
    }

    public void setResponseComm(String responseComm) {
        this.responseComm = responseComm;
    }

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

}
