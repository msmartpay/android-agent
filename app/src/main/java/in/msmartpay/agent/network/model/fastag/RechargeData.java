
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RechargeData {

    @Expose
    private String amount;
    @SerializedName("bill_fetch")
    private BillFetch billFetch;
    @Expose
    private String canumber;
    @Expose
    private Double latitude;
    @Expose
    private Double longitude;
    @Expose
    private String operator;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public BillFetch getBillFetch() {
        return billFetch;
    }

    public void setBillFetch(BillFetch billFetch) {
        this.billFetch = billFetch;
    }

    public String getCanumber() {
        return canumber;
    }

    public void setCanumber(String canumber) {
        this.canumber = canumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
