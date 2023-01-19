
package in.msmartpay.agent.network.model.dmr;

public class BankDetailsDmrResponse {

    private String message;
    private String Status;

    private String isverificationavailable;
    private String available_channels;
    private String ifsc_status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIsverificationavailable() {
        return isverificationavailable;
    }

    public void setIsverificationavailable(String isverificationavailable) {
        this.isverificationavailable = isverificationavailable;
    }

    public String getAvailable_channels() {
        return available_channels;
    }

    public void setAvailable_channels(String available_channels) {
        this.available_channels = available_channels;
    }

    public String getIfsc_status() {
        return ifsc_status;
    }

    public void setIfsc_status(String ifsc_status) {
        this.ifsc_status = ifsc_status;
    }
}
