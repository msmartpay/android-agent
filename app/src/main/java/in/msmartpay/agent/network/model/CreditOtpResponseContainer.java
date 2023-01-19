package in.msmartpay.agent.network.model;


import com.google.gson.annotations.SerializedName;

public class CreditOtpResponseContainer {
    @SerializedName("Status")
    String status;
    @SerializedName("message")
    String message;
    private CreditOtpResponse data;

    @Override
    public String toString() {
        return "CreditOtpResponseContainer{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
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

    public CreditOtpResponse getData() {
        return data;
    }

    public void setData(CreditOtpResponse data) {
        this.data = data;
    }
}
