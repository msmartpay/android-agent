package com.scinfotech.network.model.ekobbps;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class PayBillRequest {

	@SerializedName("agent_id")
	private String agentId;

	@SerializedName("data")
	private HashMap<String,Object> data;

	@SerializedName("txn_key")
	private String txnKey;

	@SerializedName("opname")
	private String opname;

	public void setAgentId(String agentId){
		this.agentId = agentId;
	}

	public String getAgentId(){
		return agentId;
	}

	public void setData(HashMap<String,Object> data){
		this.data = data;
	}

	public HashMap<String,Object> getData(){
		return data;
	}

	public void setTxnKey(String txnKey){
		this.txnKey = txnKey;
	}

	public String getTxnKey(){
		return txnKey;
	}

	public void setOpname(String opname){
		this.opname = opname;
	}

	public String getOpname(){
		return opname;
	}
}