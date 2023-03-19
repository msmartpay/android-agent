
package in.msmartpayagent.network.model.aeps.onboard;

import com.google.gson.annotations.SerializedName;

public class UserOtpVerifyData {

    @SerializedName("csp_id")
    private String mCspId;

    public String getCspId() {
        return mCspId;
    }

    public void setCspId(String cspId) {
        mCspId = cspId;
    }

}
