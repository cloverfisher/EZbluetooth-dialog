package com.example.source;

import jxl.Cell;

public class CustomerMasterfile {
	String customer;
	String custName;
	String branch;
	String warehouse;
	String shiptoNumber;
	String shiptoName;
	String SRM;
	String Autocrib;
	String workorder;
	String price;
	
	String shipToAddress;
	String shipToCity;
	String shipToState;
	String shipToZip;
	String shipToContactName;
	String shipToContactEmail;
	
	public CustomerMasterfile(Cell cc[])
	{
		String s[];
		s = new String[16];
		for(int i=0;i<16;i++)
		{
			try {
				s[i] = cc[i].getContents() ;
			} catch (Exception e) {
				// TODO: handle exception
				s[i] = null;
			} 
		}
		this.customer = s[0];
		this.custName = s[1];
		this.branch = s[2];
		this.warehouse = s[3];
		this.shiptoNumber = s[4];
		this.shiptoName = s[5];
		this.SRM = s[6];
		this.Autocrib = s[7];
		this.workorder = s[8];
		this.price = s[9];
		this.shipToAddress = s[10];
		this.shipToCity = s[11];
		this.shipToState = s[12];
		this.shipToZip = s[13];
		this.shipToContactName = s[14];
		this.shipToContactEmail = s[15];
	}
	
	public CustomerMasterfile(String customer,String custName,
	String branch,
	String warehouse,
	String shiptoNumber,
	String shiptoName,
	String SRM,
	String Autocrib,
	String workorder,
	String price,
	String shipToAddress,
	String shipToCity,
	String shipToState,
	String shipToZip,
	String shipToContactName,
	String shipToContactEmail){
		this.customer = customer;
		this.custName = custName;
		this.branch = branch;
		this.warehouse = warehouse;
		this.shiptoNumber = shiptoNumber;
		this.shiptoName = shiptoName;
		this.SRM = SRM;
		this.Autocrib = Autocrib;
		this.workorder = workorder;
		this.price = price;
		this.shipToAddress = shipToAddress;
		this.shipToCity = shipToCity;
		this.shipToState = shipToState;
		this.shipToZip = shipToZip;
		this.shipToContactName = shipToContactName;
		this.shipToContactEmail = shipToContactEmail;
		
	}
	
	public String getShipToState() {
		return shipToState;
	}
	public void setShipToState(String shipToState) {
		this.shipToState = shipToState;
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
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public String getShiptoNumber() {
		return shiptoNumber;
	}
	public void setShiptoNumber(String shiptoNumber) {
		this.shiptoNumber = shiptoNumber;
	}
	public String getShiptoName() {
		return shiptoName;
	}
	public void setShiptoName(String shiptoName) {
		this.shiptoName = shiptoName;
	}
	public String getSRM() {
		return SRM;
	}
	public void setSRM(String sRM) {
		SRM = sRM;
	}
	public String getAutocrib() {
		return Autocrib;
	}
	public void setAutocrib(String autocrib) {
		Autocrib = autocrib;
	}
	public String getWorkorder() {
		return workorder;
	}
	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getShipToAddress() {
		return shipToAddress;
	}
	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}
	public String getShipToCity() {
		return shipToCity;
	}
	public void setShipToCity(String shipToCity) {
		this.shipToCity = shipToCity;
	}
	public String getShipToZip() {
		return shipToZip;
	}
	public void setShipToZip(String shipToZip) {
		this.shipToZip = shipToZip;
	}
	public String getShipToContactName() {
		return shipToContactName;
	}
	public void setShipToContactName(String shipToContactName) {
		this.shipToContactName = shipToContactName;
	}
	public String getShipToContactEmail() {
		return shipToContactEmail;
	}
	public void setShipToContactEmail(String shipToContactEmail) {
		this.shipToContactEmail = shipToContactEmail;
	}
	
	
}
