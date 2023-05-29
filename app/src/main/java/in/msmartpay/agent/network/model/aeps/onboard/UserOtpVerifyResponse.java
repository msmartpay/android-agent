
package in.msmartpay.agent.network.model.aeps.onboard;

import com.google.gson.annotations.SerializedName;

public class UserOtpVerifyResponse {

    @SerializedName("data")
    private UserOtpVerifyData mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("Status")
    private String mStatus;

    public UserOtpVerifyData getData() {
        return mData;
    }

    public void setData(UserOtpVerifyData data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
