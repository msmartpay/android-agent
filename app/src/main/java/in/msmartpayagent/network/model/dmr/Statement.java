
package in.msmartpayagent.network.model.dmr;
import com.google.gson.annotations.SerializedName;

public class Statement {

    @SerializedName("Amount")
    private String mAmount;
    @SerializedName("BankrefId")
    private String mBankrefId;
    @SerializedName("BeneAccount")
    private String mBeneAccount;
    @SerializedName("BeneBankIfsc")
    private String mBeneBankIfsc;
    @SerializedName("BeneBankName")
    private String mBeneBankName;
    @SerializedName("BeneName")
    private String mBeneName;
    @SerializedName("Dot")
    private String mDot;
    @SerializedName("SenderId")
    private String mSenderId;
    @SerializedName("Status")
    private String mStatus;
    @SerializedName("Tot")
    private String mTot;
    @SerializedName("TranNo")
    private String mTranNo;
    @SerializedName("TransactionType")
    private String mTransactionType;
    @SerializedName("Tid")
    private String tid;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getBankrefId() {
        return mBankrefId;
    }

    public void setBankrefId(String bankrefId) {
        mBankrefId = bankrefId;
    }

    public String getBeneAccount() {
        return mBeneAccount;
    }

    public void setBeneAccount(String beneAccount) {
        mBeneAccount = beneAccount;
    }

    public String getBeneBankIfsc() {
        return mBeneBankIfsc;
    }

    public void setBeneBankIfsc(String beneBankIfsc) {
        mBeneBankIfsc = beneBankIfsc;
    }

    public String getBeneBankName() {
        return mBeneBankName;
    }

    public void setBeneBankName(String beneBankName) {
        mBeneBankName = beneBankName;
    }

    public String getBeneName() {
        return mBeneName;
    }

    public void setBeneName(String beneName) {
        mBeneName = beneName;
    }

    public String getDot() {
        return mDot;
    }

    public void setDot(String dot) {
        mDot = dot;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTot() {
        return mTot;
    }

    public void setTot(String tot) {
        mTot = tot;
    }

    public String getTranNo() {
        return mTranNo;
    }

    public void setTranNo(String tranNo) {
        mTranNo = tranNo;
    }

    public String getTransactionType() {
        return mTransactionType;
    }

    public void setTransactionType(String transactionType) {
        mTransactionType = transactionType;
    }

}
