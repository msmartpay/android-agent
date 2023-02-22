package com.aepssdkssz.paysprint.model;

import com.aepssdkssz.network.model.MiniStatementModel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class PaysprintAepsData {

    @SerializedName("orderStatus")
    private String txn_status;

    @SerializedName("accountBalance")
    private String balance;

    @SerializedName("ackno")
    private String tid;

    @SerializedName("bankrrn")
    private String rrn;

    @SerializedName("datetime")
    private String date_time;

    @SerializedName("agentId")
    private String merchant_id;

    @SerializedName("mode")
    private String transaction_type;

    @SerializedName("terminalId")
    private String terminal_id;

    @SerializedName("transactionId")
    private String client_ref_id;

    private HashMap<String,String> data;

    @SerializedName("request")
    private PaysprintAepsRequestData reqData;

    @SerializedName("ministatement")
    ArrayList<MiniStatementModel> miniStatementlist;

    public ArrayList<MiniStatementModel> getMiniStatementlist() {
        return miniStatementlist;
    }

    public void setMiniStatementlist(ArrayList<MiniStatementModel> miniStatementlist) {
        this.miniStatementlist = miniStatementlist;
    }

    public PaysprintAepsRequestData getReqData() {
        return reqData;
    }

    public void setReqData(PaysprintAepsRequestData reqData) {
        this.reqData = reqData;
    }

    public String getTxn_status() {
        return txn_status;
    }

    public void setTxn_status(String txn_status) {
        this.txn_status = txn_status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getClient_ref_id() {
        return client_ref_id;
    }

    public void setClient_ref_id(String client_ref_id) {
        this.client_ref_id = client_ref_id;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

}