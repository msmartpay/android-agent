
package in.msmartpay.agent.network.model.dmr;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SenderLimitDetails {

    @SerializedName("AvailableLimit")
    private Long mAvailableLimit;
    @SerializedName("KycStatus")
    private String mKycStatus;
    @SerializedName("SenderLimit")
    private Long mSenderLimit;
    @SerializedName("UsedLimit")
    private Long mUsedLimit;

    public Long getAvailableLimit() {
        return mAvailableLimit;
    }

    public void setAvailableLimit(Long availableLimit) {
        mAvailableLimit = availableLimit;
    }

    public String getKycStatus() {
        return mKycStatus;
    }

    public void setKycStatus(String kycStatus) {
        mKycStatus = kycStatus;
    }

    public Long getSenderLimit() {
        return mSenderLimit;
    }

    public void setSenderLimit(Long senderLimit) {
        mSenderLimit = senderLimit;
    }

    public Long getUsedLimit() {
        return mUsedLimit;
    }

    public void setUsedLimit(Long usedLimit) {
        mUsedLimit = usedLimit;
    }

}
