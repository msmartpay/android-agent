package in.msmartpayagent.network.model.user;

import java.util.List;

public class StateResponse {
    String status;
    String message;
    List<StateData> StateList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StateData> getStateList() {
        return StateList;
    }

    public void setStateList(List<StateData> stateList) {
        StateList = stateList;
    }
}
