package in.msmartpayagent.network.model;

public class PaytmWalletRequest {

    private String mobile;
    private String benename;
    private String amount;
    private String txntoken;
    private String reqMappingId;
    private String otp;
    private String refid;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBenename() {
        return benename;
    }

    public void setBenename(String benename) {
        this.benename = benename;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxntoken() {
        return txntoken;
    }

    public void setTxntoken(String txntoken) {
        this.txntoken = txntoken;
    }

    public String getReqMappingId() {
        return reqMappingId;
    }

    public void setReqMappingId(String reqMappingId) {
        this.reqMappingId = reqMappingId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }


}
