package com.system.model;

import java.sql.Timestamp;

public class Payment {
	private String paymentId;
	private String invoiceId;
	private String paymentMethod;
	private double amount;
	private Timestamp paymentDate;
	
	public Payment() {
		
	}
	
	public Payment(String paymentId, String invoiceId, String paymentMethod, double amount, Timestamp paymentDate) {
		this.paymentId = paymentId;
		this.invoiceId = invoiceId;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.paymentDate = paymentDate;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Timestamp paymentDate) {
		this.paymentDate = paymentDate;
	}
	
}
