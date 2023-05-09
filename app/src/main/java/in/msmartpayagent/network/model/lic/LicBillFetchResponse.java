package in.msmartpayagent.network.model.lic;

import com.google.gson.annotations.SerializedName;

public class LicBillFetchResponse {

    @SerializedName("Status")
    private String status;
    private String message;
    private LicBillFetchResponseData data;

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

    public LicBillFetchResponseData getData() {
        return data;
    }

    public void setData(LicBillFetchResponseData data) {
        this.data = data;
    }
}
