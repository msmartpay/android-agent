
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.Expose;

public class FastagOperator {

    private String billFetchResponse;
    @Expose
    private String operator_category;
    @Expose
    private String high_commission_channel;
    @Expose
    private String operator_id;
    @Expose
    private String name;

    public String getBillFetchResponse() {
        return billFetchResponse;
    }

    public void setBillFetchResponse(String billFetchResponse) {
        this.billFetchResponse = billFetchResponse;
    }

    public String getOperator_category() {
        return operator_category;
    }

    public void setOperator_category(String operator_category) {
        this.operator_category = operator_category;
    }

    public String getHigh_commission_channel() {
        return high_commission_channel;
    }

    public void setHigh_commission_channel(String high_commission_channel) {
        this.high_commission_channel = high_commission_channel;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
