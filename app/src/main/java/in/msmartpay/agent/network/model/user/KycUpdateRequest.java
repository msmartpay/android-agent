package in.msmartpay.agent.network.model.user;

public class KycUpdateRequest {
    String agentID;
    String txn_key;
    String adharcardNo;
    String adharHoldername;
    String email;
    String mobileno;

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
}
