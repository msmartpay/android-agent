package in.msmartpay.agent.network.model;

import com.google.gson.annotations.SerializedName;

public class MainResponse {
    @SerializedName("response-message")
    private String responseMessage;
    @SerializedName("response-code")
    private String responseCode;

    @SerializedName("Api_txn_id")
    private String txnId;

    @SerializedName("operator_id")
    private String operatorId;

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
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
