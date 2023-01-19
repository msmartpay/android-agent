
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.SerializedName;


public class FastagRechargeResponse {

    @SerializedName("ackno")
    private String mAckno;
    @SerializedName("client_ref_id")
    private String mClientRefId;
    @SerializedName("date_time")
    private String mDateTime;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("operator_id")
    private String mOperatorId;
    @SerializedName("Status")
    private String mStatus;
    @SerializedName("txn_status")
    private String mTxnStatus;

    public String getAckno() {
        return mAckno;
    }

    public void setAckno(String ackno) {
        mAckno = ackno;
    }

    public String getClientRefId() {
        return mClientRefId;
    }

    public void setClientRefId(String clientRefId) {
        mClientRefId = clientRefId;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getOperatorId() {
        return mOperatorId;
    }

    public void setOperatorId(String operatorId) {
        mOperatorId = operatorId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTxnStatus() {
        return mTxnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        mTxnStatus = txnStatus;
    }

}
