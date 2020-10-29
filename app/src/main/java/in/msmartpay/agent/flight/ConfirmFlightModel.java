package in.msmartpay.agent.flight;

/**
 * Created by Harendra on 10/23/2017.
 */

public class ConfirmFlightModel {

    String PassengerType;
    String Age;
    String IdentityProofNumber;
    String PersonOrgId;
    String IdentityProofId;
    String TransmissionControlNo;
    String Title;
    String fare;
    String gender;

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassengerType() {
        return PassengerType;
    }

    public void setPassengerType(String passengerType) {
        PassengerType = passengerType;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getIdentityProofNumber() {
        return IdentityProofNumber;
    }

    public void setIdentityProofNumber(String identityProofNumber) {
        IdentityProofNumber = identityProofNumber;
    }

    public String getPersonOrgId() {
        return PersonOrgId;
    }

    public void setPersonOrgId(String personOrgId) {
        PersonOrgId = personOrgId;
    }

    public String getIdentityProofId() {
        return IdentityProofId;
    }

    public void setIdentityProofId(String identityProofId) {
        IdentityProofId = identityProofId;
    }

    public String getTransmissionControlNo() {
        return TransmissionControlNo;
    }

    public void setTransmissionControlNo(String transmissionControlNo) {
        TransmissionControlNo = transmissionControlNo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


}
