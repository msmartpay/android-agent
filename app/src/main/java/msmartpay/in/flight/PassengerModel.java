package msmartpay.in.flight;

import java.io.Serializable;

/**
 * Created by Harendra on 10/7/2017.
 */

public class PassengerModel implements Serializable {

    private String type;
    private String firstName;
    private String lastName;
    private String DOB;
    private int rbId;


    public int getRbId() {
        return rbId;
    }

    public void setRbId(int rbId) {
        this.rbId = rbId;
    }
   public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

}
