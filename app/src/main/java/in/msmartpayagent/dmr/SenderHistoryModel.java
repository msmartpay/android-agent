package in.msmartpayagent.dmr;

/**
 * Created by Smartkinda on 6/18/2017.
 */

public class SenderHistoryModel {

    private String SenderId;
    private String TranNo;
    private String Dot;
    private String Tot;
    private String Amount;
    private String Status;
    private String BankrefId;

    private String BeneName;
    private String BeneBankName;
    private String BeneBankIfsc;
    private String BeneAccount;
    private String TransferType;



    //=======================================================================
    public String getTransferType() {
        return TransferType;
    }

    public void setTransferType(String transferType) {
        TransferType = transferType;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getTranNo() {
        return TranNo;
    }

    public void setTranNo(String tranNo) {
        TranNo = tranNo;
    }

    public String getDot() {
        return Dot;
    }

    public void setDot(String dot) {
        Dot = dot;
    }

    public String getTot() {
        return Tot;
    }

    public void setTot(String tot) {
        Tot = tot;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBankrefId() {
        return BankrefId;
    }

    public void setBankrefId(String bankrefId) {
        BankrefId = bankrefId;
    }

    public String getBeneName() {
        return BeneName;
    }

    public void setBeneName(String beneName) {
        BeneName = beneName;
    }

    public String getBeneBankName() {
        return BeneBankName;
    }

    public void setBeneBankName(String beneBankName) {
        BeneBankName = beneBankName;
    }

    public String getBeneBankIfsc() {
        return BeneBankIfsc;
    }

    public void setBeneBankIfsc(String beneBankIfsc) {
        BeneBankIfsc = beneBankIfsc;
    }

    public String getBeneAccount() {
        return BeneAccount;
    }

    public void setBeneAccount(String beneAccount) {
        BeneAccount = beneAccount;
    }
}
