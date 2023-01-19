
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FastagFetchResponse {

    @SerializedName("Status")
    private String status;
    private String message;

    FastagCustomerDetails data;

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

    public FastagCustomerDetails getData() {
        return data;
    }

    public void setData(FastagCustomerDetails data) {
        this.data = data;
    }

    public  class FastagCustomerDetails{
        @Expose
        private String amount;
        @SerializedName("bill_fetch")
        private BillFetch billFetch;
        @Expose
        private String duedate;
        @Expose
        private String message;
        @Expose
        private String name;
        @SerializedName("response_code")
        private Long responseCode;
        @Expose
        private Boolean status;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public BillFetch getBillFetch() {
            return billFetch;
        }

        public void setBillFetch(BillFetch billFetch) {
            this.billFetch = billFetch;
        }

        public String getDuedate() {
            return duedate;
        }

        public void setDuedate(String duedate) {
            this.duedate = duedate;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(Long responseCode) {
            this.responseCode = responseCode;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

    }
}
