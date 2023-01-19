package in.msmartpay.agent.network.model.user;

import java.util.List;

public class DistrictResponse {
    String status;
    String message;
    List<DistrictData> districtList;

    public List<DistrictData> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictData> districtList) {
        this.districtList = districtList;
    }


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

}
