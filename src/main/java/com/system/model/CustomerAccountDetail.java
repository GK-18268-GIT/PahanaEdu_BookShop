package com.system.model;

public class CustomerAccountDetail {
    private String customerName;
    private String accountNumber;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private double totalAmount;
    private String invoiceId;
    private String paymentId;
    private String paymentMethod;
    
    public CustomerAccountDetail() {
    	
    }
    
    public CustomerAccountDetail(String customerName, String accountNumber, String itemName, 
                               int quantity, double unitPrice, double totalPrice, double totalAmount,
                               String invoiceId, String paymentId, String paymentMethod) {
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.totalAmount = totalAmount;
        this.invoiceId = invoiceId;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
    }

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
    
    
}