package com.example.source;

import android.annotation.SuppressLint;
import jxl.Cell;

public class ReturnableItem {

	String itemNumber;
	String custPart;
	String description;
	String date;
	String time;
	String status;
	String uom;
	String userName;
	String shiptoNumber;
	String shiptoName;
	String wortOrder;
	String customer;
	String custName;
	@SuppressLint("NewApi")
	public ReturnableItem(Cell[] cc)
	{
		String s[] ;
		s= new String[13] ;
		for(int i = 0; i< 13; i++)
		{
			try{
			if(cc[i].getContents() != null && !cc[i].getContents().equals(""))
				s[i] = cc[i].getContents();
			else
				s[i] = null;
			}
			catch(Exception e)
			{
				s[i] = null;
			}
	//		s[i]  = cc[i].getContents();
		}

		this.itemNumber = s[0];//cc[0].getContents();
		this.custPart = s[1];//cc[1].getContents();
//		if (this.custPart == null || this.custPart.isEmpty()) {

		this.description =s[2];// cc[2].getContents();
		this.date = s[3];//cc[3].getContents();
		this.time = s[4];//cc[4].getContents();
		this.status = s[5];//cc[5].getContents();
		this.uom = s[6];//cc[6].getContents();
		this.userName = s[7];//cc[7].getContents();
		this.shiptoNumber = s[8];//cc[8].getContents();
		this.shiptoName = s[9];//cc[9].getContents();
		this.wortOrder = s[10];//cc[10].getContents();
		this.customer = s[11];//cc[11].getContents();
		this.custName =s[12];// cc[12].getContents();
	}
		
	@SuppressLint("NewApi")
	public String checknull(String astring)
	{
		if((astring) == null || (astring.isEmpty()))
			return null;
		else 
			return astring;
	}
	
	public ReturnableItem(String itemNumber,
			String custPart,
			String description,
			String date,
			String time,
			String status,
			String uom,
			String userName,
			String shiptoNumber,
			String shiptoName,
			String wortOrder,
			String customer,
			String custName
			)
	{
		super();
		this.itemNumber = itemNumber;
		this.custPart = custPart;
		this.description = description;
		this.date = date;
		this.time = time;
		this.status = status;
		this.uom = uom;
		this.userName = userName;
		this.shiptoNumber = shiptoNumber;
		this.shiptoName = shiptoName;
		this.wortOrder = wortOrder;
		this.customer = customer;
		this.custName = custName;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getCustPart() {
		return custPart;
	}
	public void setCustPart(String custPart) {
		this.custPart = custPart;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getWortOrder() {
		return wortOrder;
	}
	public void setWortOrder(String wortOrder) {
		this.wortOrder = wortOrder;
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
	

}
