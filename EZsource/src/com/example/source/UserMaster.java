package com.example.source;

public class UserMaster {	
	String userID;
	String userName;
	String userPin;
	String customer;
	String custName;
	String returnable;
	
	
	public UserMaster(String userID, String userName, String userPin,
			String customer, String custName, String returnable) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.userPin = userPin;
		this.customer = customer;
		this.custName = custName;
		this.returnable = returnable;
	}
	
	public UserMaster(){};
	

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPin() {
		return userPin;
	}
	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getReturnable() {
		return returnable;
	}
	public void setReturnable(String returnable) {
		this.returnable = returnable;
	}
	
	
}
