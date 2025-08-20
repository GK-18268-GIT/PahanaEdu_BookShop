package com.system.model;

import java.sql.Timestamp;

public class Payment {
	private String paymentId;
	private String invoiceId;
	private String paymentMethod;
	private double amount;
	private Timestamp paymentDate;
	private String cardNumber;
	private String cardName;
	private String expiryDate;
	private String cvv;
	
	public Payment() {
		
	}
	
	public Payment(String paymentId, String invoiceId, String paymentMethod, double amount, Timestamp paymentDate,
			String cardNumber, String cardName, String expiryDate, String cvv) {
		this.paymentId = paymentId;
		this.invoiceId = invoiceId;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
		this.paymentDate = paymentDate;
		this.cardNumber = cardNumber;
		this.cardName = cardName;
		this.expiryDate = expiryDate;
		this.cvv = cvv;
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

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	
	
}
