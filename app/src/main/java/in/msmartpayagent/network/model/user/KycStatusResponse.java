package in.msmartpayagent.network.model.user;

public class KycStatusResponse {
    String status;
    String message;
    String adharcardNo;
    String adharHoldername;
    String kycRequeststatus;

    public String getAdharcardNo() {
        return adharcardNo;
    }

    public void setAdharcardNo(String adharcardNo) {
        this.adharcardNo = adharcardNo;
    }

    public String getAdharHoldername() {
        return adharHoldername;
    }

    public void setAdharHoldername(String adharHoldername) {
        this.adharHoldername = adharHoldername;
    }

    public String getKycRequeststatus() {
        return kycRequeststatus;
    }

    public void setKycRequeststatus(String kycRequeststatus) {
        this.kycRequeststatus = kycRequeststatus;
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

}
