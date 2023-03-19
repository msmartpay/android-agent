
package in.msmartpayagent.network.model.dmr;

import com.google.gson.annotations.SerializedName;

public class SenderDetails {

    @SerializedName("Name")
    private String mName;
    @SerializedName("SenderId")
    private String mSenderId;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

}
