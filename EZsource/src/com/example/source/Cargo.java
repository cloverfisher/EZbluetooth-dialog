package com.example.source;

public class Cargo {
	String qty;
	String UOM;
	String price;
	String CustPart;
	String item;
	String description;
	String enterdate;
	String entertime;
	String onOrder;
	boolean weatherTeturn = false;
	
	
	public boolean isWeatherTeturn() {
		return weatherTeturn;
	}
	public void setWeatherTeturn(boolean weatherTeturn) {
		this.weatherTeturn = weatherTeturn;
	}
	
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCustPart() {
		return CustPart;
	}
	public void setCustPart(String custPart) {
		CustPart = custPart;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEnterdate() {
		return enterdate;
	}
	public void setEnterdate(String enterdate) {
		this.enterdate = enterdate;
	}
	public String getEntertime() {
		return entertime;
	}
	public void setEntertime(String entertime) {
		this.entertime = entertime;
	}
	public String getOnOrder() {
		return onOrder;
	}
	public void setOnOrder(String onorder) {
		this.onOrder = onorder;
	}
	
}
