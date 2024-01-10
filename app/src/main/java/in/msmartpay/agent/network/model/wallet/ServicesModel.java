package in.msmartpay.agent.network.model.wallet;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServicesModel implements Serializable {

    private String flight;
    private String bus;
    private String hotel;
    private String recharge;
    private String pancard;
    private String utility;
    private String pg;
    @SerializedName("axis_account")
    private String axisAccount;
    private String aeps2;
    private String aeps1;
    private String aeps3;//EKO AEPS API
    private String w2W;
    private String cashout;
    private String dmr1;
    private String cms;
    private String fastag;
    private String creditcard;
    private String paytm;
    private String dmr2;
    private String lic;

    private String ekobbps;

    public String getEkobbps() {
        return ekobbps;
    }

    public void setEkobbps(String ekobbps) {
        this.ekobbps = ekobbps;
    }

    public String getPg() {
        return pg;
    }

    public void setPg(String pg) {
        this.pg = pg;
    }

    public String getAeps3() {
        return aeps3;
    }

    public void setAeps3(String aeps3) {
        this.aeps3 = aeps3;
    }

    public String getCms() {
        return cms;
    }

    public void setCms(String cms) {
        this.cms = cms;
    }

    @SerializedName("auto_credit")
    private String autoCredit;

    public String getFlight() { return flight; }
    public void setFlight(String value) { this.flight = value; }

    public String getBus() { return bus; }
    public void setBus(String value) { this.bus = value; }

    public String getHotel() { return hotel; }
    public void setHotel(String value) { this.hotel = value; }

    public String getRecharge() { return recharge; }
    public void setRecharge(String value) { this.recharge = value; }

    public String getPancard() { return pancard; }
    public void setPancard(String value) { this.pancard = value; }

    public String getUtility() { return utility; }
    public void setUtility(String value) { this.utility = value; }

    public String getPG() { return pg; }
    public void setPG(String value) { this.pg = value; }

    public String getAxisAccount() { return axisAccount; }
    public void setAxisAccount(String value) { this.axisAccount = value; }

    public String getAeps2() { return aeps2; }
    public void setAeps2(String value) { this.aeps2 = value; }

    public String getAeps1() { return aeps1; }
    public void setAeps1(String value) { this.aeps1 = value; }

    public String getW2W() { return w2W; }
    public void setW2W(String value) { this.w2W = value; }

    public String getCashout() { return cashout; }
    public void setCashout(String value) { this.cashout = value; }

    public String getDmr1() { return dmr1; }
    public void setDmr1(String value) { this.dmr1 = value; }

    public String getCMS() { return cms; }
    public void setCMS(String value) { this.cms = value; }

    public String getFastag() { return fastag; }
    public void setFastag(String value) { this.fastag = value; }

    public String getCreditcard() { return creditcard; }
    public void setCreditcard(String value) { this.creditcard = value; }

    public String getPaytm() { return paytm; }
    public void setPaytm(String value) { this.paytm = value; }

    public String getDmr2() { return dmr2; }
    public void setDmr2(String value) { this.dmr2 = value; }

    public String getLic() { return lic; }
    public void setLic(String value) { this.lic = value; }

    public String getAutoCredit() { return autoCredit; }
    public void setAutoCredit(String value) { this.autoCredit = value; }
}
