package in.msmartpayagent.network.model.dmr;

// 102 -- Sender Not registered
public class AddBeneficiaryResponse {

    private String Status;
    private String message;

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