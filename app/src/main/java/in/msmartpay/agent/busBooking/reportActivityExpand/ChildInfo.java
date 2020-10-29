package in.msmartpay.agent.busBooking.reportActivityExpand;

/**
 * Created by Smartkinda on 7/28/2017.
 */

public class ChildInfo {


    String TransportName, Origin, Destination;
    String TravelDate;
    String TotalTickets, HistoryAmount, PenaltyAmount, TransportId;

    //===================================================


    public String getTransportName() {
        return TransportName;
    }

    public void setTransportName(String transportName) {
        TransportName = transportName;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getTravelDate() {
        return TravelDate;
    }

    public void setTravelDate(String travelDate) {
        TravelDate = travelDate;
    }

    public String getTotalTickets() {
        return TotalTickets;
    }

    public void setTotalTickets(String totalTickets) {
        TotalTickets = totalTickets;
    }

    public String getHistoryAmount() {
        return HistoryAmount;
    }

    public void setHistoryAmount(String historyAmount) {
        HistoryAmount = historyAmount;
    }

    public String getPenaltyAmount() {
        return PenaltyAmount;
    }

    public void setPenaltyAmount(String penaltyAmount) {
        PenaltyAmount = penaltyAmount;
    }

    public String getTransportId() {
        return TransportId;
    }

    public void setTransportId(String transportId) {
        TransportId = transportId;
    }
}
