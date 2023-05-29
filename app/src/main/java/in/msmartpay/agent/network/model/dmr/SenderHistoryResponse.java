
package in.msmartpay.agent.network.model.dmr;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SenderHistoryResponse {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("Statement")
    private List<Statement> mStatement;
    @SerializedName("Status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<Statement> getStatement() {
        return mStatement;
    }

    public void setStatement(List<Statement> statement) {
        mStatement = statement;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
