
package com.aepssdkssz.network.model.aepstransaction.ekoekyc;

import com.google.gson.annotations.SerializedName;

public class EKYCRequestOtpResponseData {

    @SerializedName("user_code")
    private String userCode;
    @SerializedName("reference_tid")
    private String referenceTid;

    @SerializedName("otp_ref_id")
    private String otpRefId;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getReferenceTid() {
        return referenceTid;
    }

    public void setReferenceTid(String referenceTid) {
        this.referenceTid = referenceTid;
    }

    public String getOtpRefId() {
        return otpRefId;
    }

    public void setOtpRefId(String otpRefId) {
        this.otpRefId = otpRefId;
    }
}
