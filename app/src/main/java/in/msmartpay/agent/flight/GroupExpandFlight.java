package in.msmartpay.agent.flight;

import java.util.ArrayList;

/**
 * Created by Smartkinda on 8/18/2017.
 */

public class GroupExpandFlight {

    public String passengerName,tiketNo;
    public ArrayList<ConfirmFlightModel> children = new ArrayList<>();

    public GroupExpandFlight(String passengerName, String tiketNo, ArrayList<ConfirmFlightModel> children) {

        this.passengerName = passengerName;
        this.tiketNo = tiketNo;
        this.children = children;
    }


}
