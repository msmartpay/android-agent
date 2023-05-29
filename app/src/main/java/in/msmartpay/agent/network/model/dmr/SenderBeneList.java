
package in.msmartpay.agent.network.model.dmr;

import com.google.gson.annotations.SerializedName;

public class SenderBeneList {

    @SerializedName("Account")
    private String mAccount;
    @SerializedName("BankName")
    private String mBankName;
    @SerializedName("BeneMobile")
    private String mBeneMobile;
    @SerializedName("BeneName")
    private String mBeneName;
    @SerializedName("Channel")
    private String mChannel;
    @SerializedName("IMPS")
    private String mIMPS;
    @SerializedName("Ifsc")
    private String mIfsc;
    @SerializedName("NEFT")
    private String mNEFT;
    @SerializedName("RecipientId")
    private String mRecipientId;
    @SerializedName("RecipientIdType")
    private String mRecipientIdType;

    @SerializedName("is_verified")
    private String isVerified;

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getBankName() {
        return mBankName;
    }

    public void setBankName(String bankName) {
        mBankName = bankName;
    }

    public String getBeneMobile() {
        return mBeneMobile;
    }

    public void setBeneMobile(String beneMobile) {
        mBeneMobile = beneMobile;
    }

    public String getBeneName() {
        return mBeneName;
    }

    public void setBeneName(String beneName) {
        mBeneName = beneName;
    }

    public String getChannel() {
        return mChannel;
    }

    public void setChannel(String channel) {
        mChannel = channel;
    }

    public String getIMPS() {
        return mIMPS;
    }

    public void setIMPS(String iMPS) {
        mIMPS = iMPS;
    }

    public String getIfsc() {
        return mIfsc;
    }

    public void setIfsc(String ifsc) {
        mIfsc = ifsc;
    }

    public String getNEFT() {
        return mNEFT;
    }

    public void setNEFT(String nEFT) {
        mNEFT = nEFT;
    }

    public String getRecipientId() {
        return mRecipientId;
    }

    public void setRecipientId(String recipientId) {
        mRecipientId = recipientId;
    }

    public String getRecipientIdType() {
        return mRecipientIdType;
    }

    public void setRecipientIdType(String recipientIdType) {
        mRecipientIdType = recipientIdType;
    }

}
