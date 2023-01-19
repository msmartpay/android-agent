package in.msmartpay.agent.network.model;

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
