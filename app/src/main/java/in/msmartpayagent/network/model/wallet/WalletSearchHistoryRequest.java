package in.msmartpayagent.network.model.wallet;

public class WalletSearchHistoryRequest {
    String agent_id;
    String txn_key;
    String connection_no;


    public String getConnection_no() {
        return connection_no;
    }

    public void setConnection_no(String connection_no) {
        this.connection_no = connection_no;
    }

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
}
