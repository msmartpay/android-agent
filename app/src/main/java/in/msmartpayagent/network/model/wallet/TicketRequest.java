package in.msmartpayagent.network.model.wallet;

public class TicketRequest {
    String agent_id;
    String txn_key;
    String transactionId;
    String ticketmessage;

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getTxn_key() {
        return txn_key;
    }

    public void setTxn_key(String txn_key) {
        this.txn_key = txn_key;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTicketmessage() {
        return ticketmessage;
    }

    public void setTicketmessage(String ticketmessage) {
        this.ticketmessage = ticketmessage;
    }
}
