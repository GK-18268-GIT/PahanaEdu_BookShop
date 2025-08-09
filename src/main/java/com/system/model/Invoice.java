package com.system.model;

import java.sql.Timestamp;
import java.util.List;

public class Invoice {
	private String invoiceId;
	private Timestamp invoiceDate;
	private double totalAmount;
	private List<CartItem> items;
	
	public Invoice() {
		
	}
	
	public Invoice(String invoiceId, Timestamp invoiceDate, double totalAmount, List<CartItem> items) {
		this.invoiceId = invoiceId;
		this.invoiceDate = invoiceDate;
		this.totalAmount = totalAmount;
		this.items = items;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}

}
