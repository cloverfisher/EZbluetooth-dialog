package com.example.source;

import java.util.ArrayList;
import java.util.List;

public class Output {
	String user;
	String shipdate;
	String customerNumber;
	String shiptocode;
	String branch;
	String disc;
	String discday;
	String netdays;
	String shipToNumber;
	String shipToName;
	String shipToAddress;
	String shipToCity;
	String shipToState;
	String shipToZip;
	int orderTotal;
	String warehouse;
	String workOrder;

	List<Cargo> cargoList = new ArrayList<Cargo>();

	public String getShipToState() {
		return shipToState;
	}

	public void setShipToState(String shipToState) {
		this.shipToState = shipToState;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getShipdate() {
		return shipdate;
	}

	public void setShipdate(String shipdate) {
		this.shipdate = shipdate;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getShiptocode() {
		return shiptocode;
	}

	public void setShiptocode(String shiptocode) {
		this.shiptocode = shiptocode;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getDisc() {
		return disc;
	}

	public void setDisc(String disc) {
		this.disc = disc;
	}

	public String getDiscday() {
		return discday;
	}

	public void setDiscday(String discday) {
		this.discday = discday;
	}

	public String getNetdays() {
		return netdays;
	}

	public void setNetdays(String netdays) {
		this.netdays = netdays;
	}

	public String getShipToNumber() {
		return shipToNumber;
	}

	public void setShipToNumber(String shipToNumber) {
		this.shipToNumber = shipToNumber;
	}

	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
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

	public int getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(int orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public List<Cargo> getCargoList() {
		return cargoList;
	}

	public void setCargoList(List<Cargo> cargoList) {
		this.cargoList = cargoList;
	}
	
}
