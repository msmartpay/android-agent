
package in.msmartpayagent.network.model.aeps.onboard;

import com.google.gson.annotations.SerializedName;

public class UserAepsActivateResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("Status")
    private String mStatus;

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
