package agent.msmartpay.in.flight;

/**
 * Created by Smartkinda on 7/28/2017.
 */

public class ChildInfoFlight {


    String Amount;
    String currentBal;
    String Service, TransactionAmount, charge, ActionOnBalanceAmount, Remarks;

    //===================================================

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getTransactionAmount() {
        return TransactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        TransactionAmount = transactionAmount;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getActionOnBalanceAmount() {
        return ActionOnBalanceAmount;
    }

    public void setActionOnBalanceAmount(String actionOnBalanceAmount) {
        ActionOnBalanceAmount = actionOnBalanceAmount;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCurrentBal() {
        return currentBal;
    }

    public void setCurrentBal(String currentBal) {
        this.currentBal = currentBal;
    }


}
