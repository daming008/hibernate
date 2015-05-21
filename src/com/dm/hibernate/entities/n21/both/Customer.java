package com.dm.hibernate.entities.n21.both;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	private Integer customerID;
	private String customerName;
	private Set<Order> orders = new HashSet<>();
	
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	public Integer getCustomerID() {
		return customerID;
	}
	public void setCustomerID(Integer customerID) {
		this.customerID = customerID;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	
}
