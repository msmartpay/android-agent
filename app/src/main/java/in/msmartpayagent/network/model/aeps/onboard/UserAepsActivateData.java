
package in.msmartpayagent.network.model.aeps.onboard;

import com.google.gson.annotations.SerializedName;

public class UserAepsActivateData {

    @SerializedName("balance")
    private String mBalance;
    @SerializedName("initiator_id")
    private String mInitiatorId;
    @SerializedName("service_code")
    private String mServiceCode;
    @SerializedName("service_status")
    private String mServiceStatus;
    @SerializedName("service_status_desc")
    private String mServiceStatusDesc;
    @SerializedName("user_code")
    private String mUserCode;

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        mBalance = balance;
    }

    public String getInitiatorId() {
        return mInitiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        mInitiatorId = initiatorId;
    }

    public String getServiceCode() {
        return mServiceCode;
    }

    public void setServiceCode(String serviceCode) {
        mServiceCode = serviceCode;
    }

    public String getServiceStatus() {
        return mServiceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        mServiceStatus = serviceStatus;
    }

    public String getServiceStatusDesc() {
        return mServiceStatusDesc;
    }

    public void setServiceStatusDesc(String serviceStatusDesc) {
        mServiceStatusDesc = serviceStatusDesc;
    }

    public String getUserCode() {
        return mUserCode;
    }

    public void setUserCode(String userCode) {
        mUserCode = userCode;
    }

}
