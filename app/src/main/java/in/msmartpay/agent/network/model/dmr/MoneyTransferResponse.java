package in.msmartpay.agent.network.model.dmr;

public class MoneyTransferResponse {
    private String Status;
    private String message;
    private String SenderId;
    private String SenderName;
    private String BeneAccount;
    private String BeneName;
    private String Ifsc;
    private String BankName;
    private String BeneMobile;
    private String TxnAmount;
    private String BankRRN;
    private String Remark;
    private String TxnDate;
    private String TxnTime;
    private String ip;
    private String TxnStatus;
    private String tid;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTxnStatus() {
        return TxnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        TxnStatus = txnStatus;
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

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getBeneAccount() {
        return BeneAccount;
    }

    public void setBeneAccount(String beneAccount) {
        BeneAccount = beneAccount;
    }

    public String getBeneName() {
        return BeneName;
    }

    public void setBeneName(String beneName) {
        BeneName = beneName;
    }

    public String getIfsc() {
        return Ifsc;
    }

    public void setIfsc(String ifsc) {
        Ifsc = ifsc;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBeneMobile() {
        return BeneMobile;
    }

    public void setBeneMobile(String beneMobile) {
        BeneMobile = beneMobile;
    }

    public String getTxnAmount() {
        return TxnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        TxnAmount = txnAmount;
    }

    public String getBankRRN() {
        return BankRRN;
    }

    public void setBankRRN(String bankRRN) {
        BankRRN = bankRRN;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getTxnDate() {
        return TxnDate;
    }

    public void setTxnDate(String txnDate) {
        TxnDate = txnDate;
    }

    public String getTxnTime() {
        return TxnTime;
    }

    public void setTxnTime(String txnTime) {
        TxnTime = txnTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}