package in.msmartpayagent.network.model.wallet;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse {
    @SerializedName("response-code")
    private String responseCode;
    @SerializedName("response-message")
    private String responseMessage;
    private String debit;
    private String credit;
    private int kycStatus;

    public int getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(int kycStatus) {
        this.kycStatus = kycStatus;
    }


    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
