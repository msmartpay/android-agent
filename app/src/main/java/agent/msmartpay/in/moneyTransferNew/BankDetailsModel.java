package agent.msmartpay.in.moneyTransferNew;

/**
 * Created by Smartkinda on 6/18/2017.
 */

public class BankDetailsModel {
    private String RecipientId;
    private String RecipientIdType;
    private String BeneName;
    private String Account;
    private String Ifsc;
    private String BankName;
    private String BeneMobile;
    private String IMPS;
    private String NEFT;
    private String Channel;


    //==========================================================================


    public String getRecipientId() {
        return RecipientId;
    }

    public void setRecipientId(String recipientId) {
        RecipientId = recipientId;
    }

    public String getRecipientIdType() {
        return RecipientIdType;
    }

    public void setRecipientIdType(String recipientIdType) {
        RecipientIdType = recipientIdType;
    }

    public String getBeneName() {
        return BeneName;
    }

    public void setBeneName(String beneName) {
        BeneName = beneName;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
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

    public String getIMPS() {
        return IMPS;
    }

    public void setIMPS(String IMPS) {
        this.IMPS = IMPS;
    }

    public String getNEFT() {
        return NEFT;
    }

    public void setNEFT(String NEFT) {
        this.NEFT = NEFT;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }





}
