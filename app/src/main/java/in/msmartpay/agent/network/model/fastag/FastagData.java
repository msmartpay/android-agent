
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.Expose;


public class FastagData {

    @Expose
    private String canumber;
    @Expose
    private String operator;

    public String getCanumber() {
        return canumber;
    }

    public void setCanumber(String canumber) {
        this.canumber = canumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

}
