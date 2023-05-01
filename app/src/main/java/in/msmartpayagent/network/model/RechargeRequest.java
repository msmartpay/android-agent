package in.msmartpayagent.network.model;

public class RechargeRequest {
    String agent_id;
    String txn_key;
    String operator;
    String service;
    String mobile_no;
    String request_id;
    String OpCode;
    String latitude;
    String longitude;
    String ip;

    String transactionPin;
    public String getTransactionPin() {
        return transactionPin;
    }

    public void setTransactionPin(String transactionPin) {
        this.transactionPin = transactionPin;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String amount;

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getOpCode() {
        return OpCode;
    }

    public void setOpCode(String opCode) {
        OpCode = opCode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
