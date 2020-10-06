package msmartpay.in.busBooking;

import java.util.ArrayList;

/**
 * Created by Smartkinda on 8/18/2017.
 */

public class GroupExpand {


    public String passengerName,seatNo,boardDropPlace;
    public ArrayList<ConfirmTicketModel> children = new ArrayList<>();

    public GroupExpand(String passengerName, String seatNo, String boardDropPlace, ArrayList<ConfirmTicketModel> children) {

        this.passengerName = passengerName;
        this.seatNo = seatNo;
        this.boardDropPlace = boardDropPlace;
        this.children = children;
    }


}
