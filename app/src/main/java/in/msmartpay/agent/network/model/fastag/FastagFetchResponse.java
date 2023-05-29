
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FastagFetchResponse {

    private String status;
    private String message;
    private String response_type_id;
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

    public String getResponse_type_id() {
        return response_type_id;
    }

    public void setResponse_type_id(String response_type_id) {
        this.response_type_id = response_type_id;
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
        @SerializedName("billfetchresponse")
        private String billFetch;
        @Expose
        private String utilitycustomername;
        private String bbpstrxnrefid;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getBillFetch() {
            return billFetch;
        }

        public void setBillFetch(String billFetch) {
            this.billFetch = billFetch;
        }

        public String getUtilitycustomername() {
            return utilitycustomername;
        }

        public void setUtilitycustomername(String utilitycustomername) {
            this.utilitycustomername = utilitycustomername;
        }

        public String getBbpstrxnrefid() {
            return bbpstrxnrefid;
        }

        public void setBbpstrxnrefid(String bbpstrxnrefid) {
            this.bbpstrxnrefid = bbpstrxnrefid;
        }
    }
}
