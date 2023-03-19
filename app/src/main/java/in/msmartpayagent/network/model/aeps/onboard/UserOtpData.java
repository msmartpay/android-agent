
package in.msmartpayagent.network.model.aeps.onboard;

import com.google.gson.annotations.SerializedName;

public class UserOtpData {

    @SerializedName("initiator_id")
    private String mInitiatorId;
    @SerializedName("mobile")
    private String mMobile;

    public String getInitiatorId() {
        return mInitiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        mInitiatorId = initiatorId;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

}
