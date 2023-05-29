package in.msmartpay.agent.network.model;

public class BillPayRequest {
    String agent_id;
    String txn_key;
    String ST;
    String service;
    String OP;
    String OPName;
    String reqType;
    String CN;
    String AD1;
    String AD2;
    String AD3;
    String AD4;
    String REQUEST_ID;
    String reference_id;
    String latitude;
    String longitude;
    String ip;
    String AMT;

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOP() {
        return OP;
    }

    public void setOP(String OP) {
        this.OP = OP;
    }

    public String getOPName() {
        return OPName;
    }

    public void setOPName(String OPName) {
        this.OPName = OPName;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public String getAD1() {
        return AD1;
    }

    public void setAD1(String AD1) {
        this.AD1 = AD1;
    }

    public String getAD2() {
        return AD2;
    }

    public void setAD2(String AD2) {
        this.AD2 = AD2;
    }

    public String getAD3() {
        return AD3;
    }

    public void setAD3(String AD3) {
        this.AD3 = AD3;
    }

    public String getAD4() {
        return AD4;
    }

    public void setAD4(String AD4) {
        this.AD4 = AD4;
    }

    public String getREQUEST_ID() {
        return REQUEST_ID;
    }

    public void setREQUEST_ID(String REQUEST_ID) {
        this.REQUEST_ID = REQUEST_ID;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
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

    public String getAMT() {
        return AMT;
    }

    public void setAMT(String AMT) {
        this.AMT = AMT;
    }

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

}
