
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.Expose;


public class BillFetch {

    @Expose
    private Boolean acceptPartPay;
    @Expose
    private Boolean acceptPayment;
    @Expose
    private String billAmount;
    @Expose
    private String billnetamount;
    @Expose
    private String cellNumber;
    @Expose
    private String dueDate;
    @Expose
    private Long maxBillAmount;
    @Expose
    private String userName;

    public Boolean getAcceptPartPay() {
        return acceptPartPay;
    }

    public void setAcceptPartPay(Boolean acceptPartPay) {
        this.acceptPartPay = acceptPartPay;
    }

    public Boolean getAcceptPayment() {
        return acceptPayment;
    }

    public void setAcceptPayment(Boolean acceptPayment) {
        this.acceptPayment = acceptPayment;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillnetamount() {
        return billnetamount;
    }

    public void setBillnetamount(String billnetamount) {
        this.billnetamount = billnetamount;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Long getMaxBillAmount() {
        return maxBillAmount;
    }

    public void setMaxBillAmount(Long maxBillAmount) {
        this.maxBillAmount = maxBillAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
