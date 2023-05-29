package in.msmartpay.agent.network.model.matm;

public class MicroPostTransactionData {

    private boolean status;
    private String ipAddress;
    private String message;
    private double transAmount;
    private double balAmount;
    private String bankRrn;
    private String transType;
    private String cardNum;
    private String bankName;
    private String cardType;
    private String terminalId;
    private String fpTid;
    private String clientRefId;
    private String errorCode;
    private String statusCode;
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }

    public double getBalAmount() {
        return balAmount;
    }

    public void setBalAmount(double balAmount) {
        this.balAmount = balAmount;
    }

    public String getBankRrn() {
        return bankRrn;
    }

    public void setBankRrn(String bankRrn) {
        this.bankRrn = bankRrn;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getFpTid() {
        return fpTid;
    }

    public void setFpTid(String fpTid) {
        this.fpTid = fpTid;
    }

    public String getClientRefId() {
        return clientRefId;
    }

    public void setClientRefId(String clientRefId) {
        this.clientRefId = clientRefId;
    }
}
