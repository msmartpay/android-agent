package in.msmartpay.agent.network.model.dmr;

import in.msmartpay.agent.network.model.dmr.ps.PSSenderRegisterData;

// 102 -- Sender Not registered
public class SenderRegisterResponse {

    private String Status;
    private String message;
    private PSSenderRegisterData data;

    public PSSenderRegisterData getData() {
        return data;
    }

    public void setData(PSSenderRegisterData data) {
        this.data = data;
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