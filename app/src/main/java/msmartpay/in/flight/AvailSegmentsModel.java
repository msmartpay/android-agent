package msmartpay.in.flight;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Harendra on 9/22/2017.
 */

public class AvailSegmentsModel implements Serializable {

    private String AirCraftType;
    private String NumberofStops;
    private String OriginAirportTerminal;
    private String ArrivalDateTime;
    private String DepartureDateTime;
    private String Duration;
    private String FlightNumber;
    private String Currency_Conversion_Rate;
    private String Via;
    private String FlightId;
    private String Origin;
    private String SupplierId;
    private String CurrencyCode;
    private String Destination;
    private String AirlineCode;
    private String DestinationAirportTerminal;

    private ArrayList<AvailPaxFareDetailsModel> availPaxFareDetailsModels = new ArrayList<>();
    public ArrayList<AvailPaxFareDetailsModel> getAvailPaxFareDetailsModels() {
        return availPaxFareDetailsModels;
    }
    public void setAvailPaxFareDetailsModels(ArrayList<AvailPaxFareDetailsModel> availPaxFareDetailsModels) {
        this.availPaxFareDetailsModels = availPaxFareDetailsModels;
    }
    public String getAirCraftType() {
        return AirCraftType;
    }
    public void setAirCraftType(String airCraftType) {
        AirCraftType = airCraftType;
    }
    public String getNumberofStops() {
        return NumberofStops;
    }

    public void setNumberofStops(String numberofStops) {
        NumberofStops = numberofStops;
    }

    public String getOriginAirportTerminal() {
        return OriginAirportTerminal;
    }

    public void setOriginAirportTerminal(String originAirportTerminal) {
        OriginAirportTerminal = originAirportTerminal;
    }
    public String getDepartureDateTime() {
        return DepartureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        DepartureDateTime = departureDateTime;
    }

    public String getArrivalDateTime() {
        return ArrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        ArrivalDateTime = arrivalDateTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getFlightNumber() {
        return FlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        FlightNumber = flightNumber;
    }

    public String getCurrency_Conversion_Rate() {
        return Currency_Conversion_Rate;
    }

    public void setCurrency_Conversion_Rate(String currency_Conversion_Rate) {
        Currency_Conversion_Rate = currency_Conversion_Rate;
    }

    public String getVia() {
        return Via;
    }

    public void setVia(String via) {
        Via = via;
    }

    public String getFlightId() {
        return FlightId;
    }

    public void setFlightId(String flightId) {
        FlightId = flightId;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(String supplierId) {
        SupplierId = supplierId;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getAirlineCode() {
        return AirlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        AirlineCode = airlineCode;
    }
    public String getDestinationAirportTerminal() {
        return DestinationAirportTerminal;
    }

    public void setDestinationAirportTerminal(String destinationAirportTerminal) {
        DestinationAirportTerminal = destinationAirportTerminal;
    }
}
