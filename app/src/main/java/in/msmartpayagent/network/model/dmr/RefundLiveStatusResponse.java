package in.msmartpayagent.network.model.dmr;

public class RefundLiveStatusResponse {
    private String Status;
    private String message;
    private String SenderId;;
    private String Tran_Type;
    private String Bank_Ref_Id;
    private String Tran_Desc;
    private String Amount;
    private String Tran_Status;
    private String TransactionRefNo;

    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getTran_Type() {
        return Tran_Type;
    }

    public void setTran_Type(String tran_Type) {
        Tran_Type = tran_Type;
    }

    public String getBank_Ref_Id() {
        return Bank_Ref_Id;
    }

    public void setBank_Ref_Id(String bank_Ref_Id) {
        Bank_Ref_Id = bank_Ref_Id;
    }

    public String getTran_Desc() {
        return Tran_Desc;
    }

    public void setTran_Desc(String tran_Desc) {
        Tran_Desc = tran_Desc;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getTran_Status() {
        return Tran_Status;
    }

    public void setTran_Status(String tran_Status) {
        Tran_Status = tran_Status;
    }

    public String getTransactionRefNo() {
        return TransactionRefNo;
    }

    public void setTransactionRefNo(String transactionRefNo) {
        TransactionRefNo = transactionRefNo;
    }
}