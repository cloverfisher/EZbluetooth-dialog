package com.example.source;

import jxl.Cell;

public class AphaseItemTemplate {
	String customer;
	String itemNumber;
	String custPart;
	String description;
	String srm;
	String price;
	String uom;
	String onOrder;
	String returnable;
	public AphaseItemTemplate()
	{
		
	}
	public AphaseItemTemplate(Cell[] cc)
	{
		String s[];
		s= new String[9];
		for(int i=0;i<9;i++)
		{
			try {
				//if(cc[i].getContents()!=null && !cc[i].getContents().isEmpty())
					s[i] = cc[i].getContents();
			} catch (Exception e) {
				s[i]=null;
				// TODO: handle exception
			}
		}
		this.customer = s[0];
		this.itemNumber =  s[1];
		this.custPart =  s[2];
		this.description =  s[3];
		this.srm =  s[4];
		this.price =  s[5];
		this.uom =  s[6];
		this.onOrder =  s[7];
		this.returnable =  s[8];
		
//		cc[0].getContents(),
//		cc[1].getContents(),
//		cc[2].getContents(),
//		cc[3].getContents(),
//		cc[4].getContents(),
//		cc[5].getContents(),
//		cc[6].getContents(),
//		cc[7].getContents(),
//		cc[8].getContents()
	}
	
	public AphaseItemTemplate(	String customer,
	String itemNumber,
	String custPart,
	String description,
	String srm,
	String price,
	String uom,
	String onOrder,
	String returnable)
	{
		this.customer = customer;
		this.itemNumber = itemNumber;
		this.custPart = custPart;
		this.description = description;
		this.srm = srm;
		this.price = price;
		this.uom = uom;
		this.onOrder = onOrder;
		this.returnable = returnable;
	}
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
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
	public String getSrm() {
		return srm;
	}
	public void setSrm(String srm) {
		this.srm = srm;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getOnOrder() {
		return onOrder;
	}
	public void setOnOrder(String onOrder) {
		this.onOrder = onOrder;
	}
	public String getReturnable() {
		return returnable;
	}
	public void setReturnable(String returnable) {
		this.returnable = returnable;
	}
	
	
}
