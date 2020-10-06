package msmartpay.in.busBooking;

import java.io.Serializable;

/**
 * Created by Smartkinda on 6/18/2017.
 */

public class BusDetailsModel implements Serializable {

    private String AvailableSeatCount;
    private String Fares;
    private String DepartureTime;
    private String ArrivalTime;
    private String BusName;
    private String TransportName;
    private String SeatType;

    private String BoardingPlace;
    private String BoardingContactNumber;
    private String BoardingTime;
    private String BoardingAddress;
    private String BoardingId;
    private String BoardingLandMark;

    private String DroppingId;
    private String DroppingPlace;
    private String DroppingContactNumber;
    private String DroppingTime;
    private String DroppingAddress;
    private String DroppingLandMark;

    private String FaresSingle;
    private String FaresSeatType;
    private String FaresConvenienceFee;
    private String FaresSeatTypeId;
    private String FaresServiceTax;

    public String getDroppingId() {
        return DroppingId;
    }

    public void setDroppingId(String droppingId) {
        DroppingId = droppingId;
    }

    public String getDroppingPlace() {
        return DroppingPlace;
    }

    public void setDroppingPlace(String droppingPlace) {
        DroppingPlace = droppingPlace;
    }

    public String getDroppingContactNumber() {
        return DroppingContactNumber;
    }

    public void setDroppingContactNumber(String droppingContactNumber) {
        DroppingContactNumber = droppingContactNumber;
    }

    public String getDroppingTime() {
        return DroppingTime;
    }

    public void setDroppingTime(String droppingTime) {
        DroppingTime = droppingTime;
    }

    public String getDroppingAddress() {
        return DroppingAddress;
    }

    public void setDroppingAddress(String droppingAddress) {
        DroppingAddress = droppingAddress;
    }

    public String getDroppingLandMark() {
        return DroppingLandMark;
    }

    public void setDroppingLandMark(String droppingLandMark) {
        DroppingLandMark = droppingLandMark;
    }

    //===================================================================

    public String getFaresSingle() {
        return FaresSingle;
    }

    public void setFaresSingle(String faresSingle) {
        FaresSingle = faresSingle;
    }

    public String getFaresSeatType() {
        return FaresSeatType;
    }

    public void setFaresSeatType(String faresSeatType) {
        FaresSeatType = faresSeatType;
    }

    public String getFaresConvenienceFee() {
        return FaresConvenienceFee;
    }

    public void setFaresConvenienceFee(String faresConvenienceFee) {
        FaresConvenienceFee = faresConvenienceFee;
    }

    public String getFaresSeatTypeId() {
        return FaresSeatTypeId;
    }

    public void setFaresSeatTypeId(String faresSeatTypeId) {
        FaresSeatTypeId = faresSeatTypeId;
    }

    public String getFaresServiceTax() {
        return FaresServiceTax;
    }

    public void setFaresServiceTax(String faresServiceTax) {
        FaresServiceTax = faresServiceTax;
    }


    public String getBoardingPlace() {
        return BoardingPlace;
    }

    public void setBoardingPlace(String boardingPlace) {
        BoardingPlace = boardingPlace;
    }

    public String getBoardingContactNumber() {
        return BoardingContactNumber;
    }

    public void setBoardingContactNumber(String boardingContactNumber) {
        BoardingContactNumber = boardingContactNumber;
    }

    public String getBoardingTime() {
        return BoardingTime;
    }

    public void setBoardingTime(String boardingTime) {
        BoardingTime = boardingTime;
    }

    public String getBoardingAddress() {
        return BoardingAddress;
    }

    public void setBoardingAddress(String boardingAddress) {
        BoardingAddress = boardingAddress;
    }

    public String getBoardingId() {
        return BoardingId;
    }

    public void setBoardingId(String boardingId) {
        BoardingId = boardingId;
    }

    public String getBoardingLandMark() {
        return BoardingLandMark;
    }

    public void setBoardingLandMark(String boardingLandMark) {
        BoardingLandMark = boardingLandMark;
    }


    public String getAvailableSeatCount() {
        return AvailableSeatCount;
    }

    public void setAvailableSeatCount(String availableSeatCount) {
        AvailableSeatCount = availableSeatCount;
    }

    public String getFares() {
        return Fares;
    }

    public void setFares(String fares) {
        Fares = fares;
    }

    public String getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        DepartureTime = departureTime;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public String getBusName() {
        return BusName;
    }

    public void setBusName(String busName) {
        BusName = busName;
    }

    public String getTransportName() {
        return TransportName;
    }

    public void setTransportName(String transportName) {
        TransportName = transportName;
    }

    public String getSeatType() {
        return SeatType;
    }

    public void setSeatType(String seatType) {
        SeatType = seatType;
    }
}
