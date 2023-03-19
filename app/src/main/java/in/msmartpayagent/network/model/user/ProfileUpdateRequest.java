package in.msmartpayagent.network.model.user;

public class ProfileUpdateRequest {
    String agent_id;
    String txn_key;

    String firstname;
    String lastname;
    String firmname;
    String DateOfBirth;
    String email;
    String mobileno;
    String gender;
    String address;
    String country;
    String state;
    String District;
    String city;
    String pincode;
    String kycstatus;
    String pannumber;
    String adharnumber;


    public String getAgentID() {
        return agent_id;
    }

    public void setAgentID(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }


    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        this.District = district;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirmname() {
        return firmname;
    }

    public void setFirmname(String firmname) {
        this.firmname = firmname;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getEmailID() {
        return email;
    }

    public void setEmailID(String emailID) {
        this.email = emailID;
    }

    public String getMobile() {
        return mobileno;
    }

    public void setMobile(String mobile) {
        this.mobileno = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getKycstatus() {
        return kycstatus;
    }

    public void setKycstatus(String kycstatus) {
        this.kycstatus = kycstatus;
    }

    public String getPannumber() {
        return pannumber;
    }

    public void setPannumber(String pannumber) {
        this.pannumber = pannumber;
    }

    public String getAdharnumber() {
        return adharnumber;
    }

    public void setAdharnumber(String adharnumber) {
        this.adharnumber = adharnumber;
    }
}
