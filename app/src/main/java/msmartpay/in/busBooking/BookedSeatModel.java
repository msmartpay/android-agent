package msmartpay.in.busBooking;

import java.io.Serializable;

/**
 * Created by Smartkinda on 6/18/2017.
 */

public class BookedSeatModel implements Serializable {


   private String PassengerName;
    private String Gender;
    private String Age;
    private String SeatNo;
    private String SeatTypeId;
    private String Fare;
    private String BoardingId;
    private String DroppingId;
    private String Time;
    private String Address;
    private String LandMark;
    private String ContactNumber;

    private int GenderPosition;
    private int BoardingPosition;
    private int DroppingPosition;


    public int getGenderPosition() {
        return GenderPosition;
    }

    public int getBoardingPosition() {
        return BoardingPosition;
    }

    public int getDroppingPosition() {
        return DroppingPosition;
    }

    //==========================================================================

    public BookedSeatModel(){}

    public BookedSeatModel(int GenderPosition, int BoardingPosition, int DroppingPosition, String passengerName, String gender, String age, String seatNo, String seatTypeId, String fare, String boardingId, String droppingId, String time, String address, String landMark, String contactNumber) {

        this.GenderPosition = GenderPosition;
        this.BoardingPosition = BoardingPosition;
        this.DroppingPosition = DroppingPosition;
        PassengerName = passengerName;
        Gender = gender;
        Age = age;
        SeatNo = seatNo;
        SeatTypeId = seatTypeId;
        Fare = fare;
        BoardingId = boardingId;
        DroppingId = droppingId;
        Time = time;
        Address = address;
        LandMark = landMark;
        ContactNumber = contactNumber;
    }

    public BookedSeatModel(String passengerName, String gender, String age, String seatNo, String seatTypeId, String fare, String boardingId, String droppingId, String time, String address, String landMark, String contactNumber) {

        PassengerName = passengerName;
        Gender = gender;
        Age = age;
        SeatNo = seatNo;
        SeatTypeId = seatTypeId;
        Fare = fare;
        BoardingId = boardingId;
        DroppingId = droppingId;
        Time = time;
        Address = address;
        LandMark = landMark;
        ContactNumber = contactNumber;
    }

    public String getPassengerName() {
        return PassengerName;
    }

    public void setPassengerName(String passengerName) {
        PassengerName = passengerName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getSeatNo() {
        return SeatNo;
    }

    public void setSeatNo(String seatNo) {
        SeatNo = seatNo;
    }

    public String getSeatTypeId() {
        return SeatTypeId;
    }

    public void setSeatTypeId(String seatTypeId) {
        SeatTypeId = seatTypeId;
    }

    public String getFare() {
        return Fare;
    }

    public void setFare(String fare) {
        Fare = fare;
    }

    public String getBoardingId() {
        return BoardingId;
    }

    public void setBoardingId(String boardingId) {
        BoardingId = boardingId;
    }

    public String getDroppingId() {
        return DroppingId;
    }

    public void setDroppingId(String droppingId) {
        DroppingId = droppingId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getLandMark() {
        return LandMark;
    }

    public void setLandMark(String landMark) {
        LandMark = landMark;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

}
