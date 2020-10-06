package msmartpay.in.busBooking.reportActivityExpand;

/**
 * Created by Irfan on 6/18/2017.
 */

public class HistoryModel {

    public String TransactionId;
    public String AmountRs;
    public String BookedDateTime;
    public String TicketStatus;
    public String Origin;
    public String TransportId;
    public String TotalTickets;
    public String PenaltyAmount;
    public String Destination;
    public String TravelDate;
    public String TransportName;


    public HistoryModel(String transactionId, String amountRs, String bookedDateTime, String ticketStatus, String origin, String transportId, String totalTickets, String penaltyAmount, String destination, String travelDate, String transportName) {
        TransactionId = transactionId;
        AmountRs = amountRs;
        BookedDateTime = bookedDateTime;
        TicketStatus = ticketStatus;
        Origin = origin;
        TransportId = transportId;
        TotalTickets = totalTickets;
        PenaltyAmount = penaltyAmount;
        Destination = destination;
        TravelDate = travelDate;
        TransportName = transportName;
    }


}
