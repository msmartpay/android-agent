
package in.msmartpay.agent.network.model.fastag;

import com.google.gson.annotations.SerializedName;


public class FastagRechargeResponse {

    private String status;
    private String message;
    private FastagTransactionData data;

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

    public FastagTransactionData getData() {
        return data;
    }

    public void setData(FastagTransactionData data) {
        this.data = data;
    }

    public  class FastagTransactionData{

        private String amount;
        @SerializedName("txn_status")
        private String txnStatus;
        @SerializedName("date_time")
        private String dateTime;

        @SerializedName("api_txn_id")
        private String txnId;

        @SerializedName("tid")
        private String tid;

        @SerializedName("operator_id")
        private String operatorId;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTxnStatus() {
            return txnStatus;
        }

        public void setTxnStatus(String txnStatus) {
            this.txnStatus = txnStatus;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getTxnId() {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }
    }
}
