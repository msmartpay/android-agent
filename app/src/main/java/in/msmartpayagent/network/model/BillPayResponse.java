package in.msmartpayagent.network.model;

import com.google.gson.annotations.SerializedName;

public class BillPayResponse {
    @SerializedName("response-message")
    String responseMessage;
    @SerializedName("response-code")
    String responseCode;

    String customername;
    String duedate;
    String dueamount;
    String reference_id;
    @SerializedName("Api_txn_id")
    String tid;
    String operatorId;

    private String txn_status;

    public String getTxn_status() {
        return txn_status;
    }

    public void setTxn_status(String txn_status) {
        this.txn_status = txn_status;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getDueamount() {
        return dueamount;
    }

    public void setDueamount(String dueamount) {
        this.dueamount = dueamount;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
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
