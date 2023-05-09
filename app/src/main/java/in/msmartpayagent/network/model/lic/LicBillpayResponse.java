package in.msmartpayagent.network.model.lic;

import com.google.gson.annotations.SerializedName;

public class LicBillpayResponse {
    private String referenceid;
    @SerializedName("Status")
    private String status;
    private String message;

    public String getTxn_status() {
        return txn_status;
    }

    public void setTxn_status(String txn_status) {
        this.txn_status = txn_status;
    }

    private String txn_status;


    private LicBillpayResponseData data;

    public String getReferenceid() {
        return referenceid;
    }

    public void setReferenceid(String referenceid) {
        this.referenceid = referenceid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LicBillpayResponseData getData() {
        return data;
    }

    public void setData(LicBillpayResponseData data) {
        this.data = data;
    }
}
