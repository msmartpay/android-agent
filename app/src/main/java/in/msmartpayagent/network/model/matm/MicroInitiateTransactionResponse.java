package in.msmartpayagent.network.model.matm;

import com.google.gson.annotations.SerializedName;

public class MicroInitiateTransactionResponse {

    private String message;
    @SerializedName("Status")
    private String status;
    private MicroInitiateTransactionData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MicroInitiateTransactionData getData() {
        return data;
    }

    public void setData(MicroInitiateTransactionData data) {
        this.data = data;
    }
}
