package com.aepssdkssz.network.model.aepstransaction;

import com.aepssdkssz.network.model.MiniStatementModel;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class AepsData {
    private String txn_status;
    private String balance;
    private String comment;
    private String amount;
    private String tid;
    private String customer_name;
    private String user_code;
    private String aadhaar_number;
    private String rrn;
    private String date_time;
    private String merchant_name;
    private String merchant_location;
    private String merchant_id;
    private String transaction_type;
    private String terminal_id;
    private String client_ref_id;
    private boolean miniOffusFlag;
    private String miniStatementText;


    @SerializedName("mini_statement_list")
    ArrayList<MiniStatementModel> miniStatementlist;

    public ArrayList<MiniStatementModel> getMiniStatementlist() {
        return miniStatementlist;
    }

    public void setMiniStatementlist(ArrayList<MiniStatementModel> miniStatementlist) {
        this.miniStatementlist = miniStatementlist;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getClient_ref_id() {
        return client_ref_id;
    }

    public void setClient_ref_id(String client_ref_id) {
        this.client_ref_id = client_ref_id;
    }

    public String getTerminal_id() {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_location() {
        return merchant_location;
    }

    public void setMerchant_location(String merchant_location) {
        this.merchant_location = merchant_location;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAadhaar_number() {
        return aadhaar_number;
    }

    public void setAadhaar_number(String aadhaar_number) {
        this.aadhaar_number = aadhaar_number;
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

    public boolean isMiniOffusFlag() {
        return miniOffusFlag;
    }

    public void setMiniOffusFlag(boolean miniOffusFlag) {
        this.miniOffusFlag = miniOffusFlag;
    }

    public String getMiniStatementText() {
        return miniStatementText;
    }

    public void setMiniStatementText(String miniStatementText) {
        this.miniStatementText = miniStatementText;
    }
}