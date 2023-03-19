
package in.msmartpayagent.network.model.aeps.onboard;

import com.google.gson.annotations.SerializedName;

public class UserRegisterData {

    @SerializedName("initiator_id")
    private String mInitiatorId;
    @SerializedName("user_code")
    private String mUserCode;

    public String getInitiatorId() {
        return mInitiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        mInitiatorId = initiatorId;
    }

    public String getUserCode() {
        return mUserCode;
    }

    public void setUserCode(String userCode) {
        mUserCode = userCode;
    }

}
