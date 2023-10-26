
package com.aepssdkssz.network.model.aepstransaction.ekoekyc;

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
