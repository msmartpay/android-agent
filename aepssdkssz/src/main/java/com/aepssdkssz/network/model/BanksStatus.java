package com.aepssdkssz.network.model;

public class BanksStatus {
    private String bank1;
    private String bank2;
    private String bank3;
    
	public BanksStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BanksStatus(String bank1, String bank2, String bank3) {
		super();
		this.bank1 = bank1;
		this.bank2 = bank2;
		this.bank3 = bank3;
	}
	public String getBank1() {
		return bank1;
	}
	public void setBank1(String bank1) {
		this.bank1 = bank1;
	}
	public String getBank2() {
		return bank2;
	}
	public void setBank2(String bank2) {
		this.bank2 = bank2;
	}
	public String getBank3() {
		return bank3;
	}
	public void setBank3(String bank3) {
		this.bank3 = bank3;
	}
	@Override
	public String toString() {
		return "BanksStatus [bank1=" + bank1 + ", bank2=" + bank2 + ", bank3=" + bank3 + "]";
	}

    

}