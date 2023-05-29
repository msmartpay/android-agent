
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.Expose;


public class FastagData {

    @Expose
    private String utility_acc_no;
    @Expose
    private String operator_id;

    private String latlong;
    private String source_ip;

    private String amount;
    private String bill_fetch;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBill_fetch() {
        return bill_fetch;
    }

    public void setBill_fetch(String bill_fetch) {
        this.bill_fetch = bill_fetch;
    }

    public String getUtility_acc_no() {
        return utility_acc_no;
    }

    public void setUtility_acc_no(String utility_acc_no) {
        this.utility_acc_no = utility_acc_no;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getSource_ip() {
        return source_ip;
    }

    public void setSource_ip(String source_ip) {
        this.source_ip = source_ip;
    }
}
