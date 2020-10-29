package in.msmartpay.agent.busBooking.reportActivityExpand;

import java.util.ArrayList;

/**
 * Created by Smartkinda on 7/28/2017.
 */

public class GroupInfo {


    String TransactionId;
    String BookedDateTime;
    String TicketStatus;
    private ArrayList<ChildInfo> list = new ArrayList<ChildInfo>();

    //=========================================================================


    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getBookedDateTime() {
        return BookedDateTime;
    }

    public void setBookedDateTime(String bookedDateTime) {
        BookedDateTime = bookedDateTime;
    }

    public String getTicketStatus() {
        return TicketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        TicketStatus = ticketStatus;
    }


    public ArrayList<ChildInfo> getList() {
        return list;
    }

    public void setList(ArrayList<ChildInfo> list) {
        this.list = list;
    }



}
