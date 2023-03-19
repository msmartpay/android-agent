
package in.msmartpayagent.network.model.aeps.onboard;

import com.google.gson.annotations.SerializedName;

public class UserOtpResponse {

    @SerializedName("data")
    private UserOtpData mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private Long mStatus;

    public UserOtpData getData() {
        return mData;
    }

    public void setData(UserOtpData data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
