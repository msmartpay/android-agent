
package in.msmartpayagent.network.model.dmr;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SenderDetailsResponse {

    @SerializedName("BeneList")
    private List<SenderBeneList> mBeneList;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("SenderDetails")
    private SenderDetails mSenderDetails;
    @SerializedName("SenderLimitDetails")
    private SenderLimitDetails mSenderLimitDetails;
    @SerializedName("Status")
    private String mStatus;

    @SerializedName("otp_required")
    private int otpRequired;
    @SerializedName("otp_ref_id")
    private String otpRefId;

    public int getOtpRequired() {
        return otpRequired;
    }

    public void setOtpRequired(int otpRequired) {
        this.otpRequired = otpRequired;
    }

    public String getOtpRefId() {
        return otpRefId;
    }

    public void setOtpRefId(String otpRefId) {
        this.otpRefId = otpRefId;
    }


    public List<SenderBeneList> getBeneList() {
        return mBeneList;
    }

    public void setBeneList(List<SenderBeneList> beneList) {
        mBeneList = beneList;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public SenderDetails getSenderDetails() {
        return mSenderDetails;
    }

    public void setSenderDetails(SenderDetails senderDetails) {
        mSenderDetails = senderDetails;
    }

    public SenderLimitDetails getSenderLimitDetails() {
        return mSenderLimitDetails;
    }

    public void setSenderLimitDetails(SenderLimitDetails senderLimitDetails) {
        mSenderLimitDetails = senderLimitDetails;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
