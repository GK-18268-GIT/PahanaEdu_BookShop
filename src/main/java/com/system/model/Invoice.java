package com.system.model;

import java.sql.Timestamp;
import java.util.List;

public class Invoice {
	private String invoiceId;
	private Timestamp invoiceDate;
	private double totalAmount;
	private String customerId;
	private String accountNumber;
	private List<CartItem> items;
	
	public Invoice() {
		
	}
	
	public Invoice(String invoiceId, Timestamp invoiceDate, double totalAmount, String customerId, String accountNumber, List<CartItem> items) {
		this.invoiceId = invoiceId;
		this.invoiceDate = invoiceDate;
		this.totalAmount = totalAmount;
		this.customerId = customerId;
		this.accountNumber = accountNumber;
		this.items = items;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
