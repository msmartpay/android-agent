package in.msmartpayagent.network.model.aeps;

import com.google.gson.annotations.SerializedName;

public class FundSettlementDetailsResponse {
    private String Status;
    private String message;
    @SerializedName("unsettled_fund")
    private String unsettledFund;
    @SerializedName("settled_amount")
    private String settledAamount;
    @SerializedName("total_transaction")
    private String totalTransaction;

    @SerializedName("data")
    private AepsSettlementDetails data;

    public AepsSettlementDetails getData() {
        return data;
    }

    public void setData(AepsSettlementDetails data) {
        this.data = data;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUnsettledFund() {
        return unsettledFund;
    }

    public void setUnsettledFund(String unsettledFund) {
        this.unsettledFund = unsettledFund;
    }

    public String getSettledAamount() {
        return settledAamount;
    }

    public void setSettledAamount(String settledAamount) {
        this.settledAamount = settledAamount;
    }

    public String getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(String totalTransaction) {
        this.totalTransaction = totalTransaction;
    }





}
