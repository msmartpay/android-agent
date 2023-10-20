package com.scinfotech.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class FetchBillRequest{

	@SerializedName("agent_id")
	private String agentId;

	@SerializedName("txn_key")
	private String txnKey;

	@SerializedName("data")
	private HashMap<String,Object> data;

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}


	public void setAgentId(String agentId){
		this.agentId = agentId;
	}

	public String getAgentId(){
		return agentId;
	}

	public void setTxnKey(String txnKey){
		this.txnKey = txnKey;
	}

	public String getTxnKey(){
		return txnKey;
	}
}