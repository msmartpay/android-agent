
package in.msmartpay.agent.network.model.aeps.onboard;

import com.google.gson.annotations.SerializedName;


public class UserRegisterResponse {

    @SerializedName("data")
    private UserRegisterData mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("response_status_id")
    private Long mResponseStatusId;
    @SerializedName("response_type_id")
    private Long mResponseTypeId;
    @SerializedName("status")
    private Long mStatus;

    public UserRegisterData getData() {
        return mData;
    }

    public void setData(UserRegisterData data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getResponseStatusId() {
        return mResponseStatusId;
    }

    public void setResponseStatusId(Long responseStatusId) {
        mResponseStatusId = responseStatusId;
    }

    public Long getResponseTypeId() {
        return mResponseTypeId;
    }

    public void setResponseTypeId(Long responseTypeId) {
        mResponseTypeId = responseTypeId;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
