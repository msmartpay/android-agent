package in.msmartpayagent.network.model.dmr;

// 102 -- Sender Not registered
public class AccountVerifyResponse {

    private String Status;
    private String message;
    private String BeneName;

    public String getBeneName() {
        return BeneName;
    }

    public void setBeneName(String beneName) {
        BeneName = beneName;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}