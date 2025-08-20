package com.system.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AccountDetails {
    private String customerId;
    private String customerName;
    private String accountNumber;
    private String email;
    private String phoneNumber;
    private String address;
    private double totalAmount;
    private String invoiceId;
    private String paymentId;
    private String paymentMethod;
    private Timestamp paymentDate;
    private List<PurchaseItems> purchaseItems;
    
    public static class PurchaseItems {
    	private String itemId;
    	private String itemName;
    	private double unitPrice;
    	private int quantity;
    	private double itemTotal;
    	
    	public PurchaseItems() {
    		
    	}
    	
    	public PurchaseItems(String itemId, String itemName, double unitPrice, int quantity, double itemTotal) {
    		this.itemId = itemId;
    		this.itemName = itemName;
    		this.unitPrice = unitPrice;
    		this.quantity = quantity;
    		this.itemTotal = itemTotal;
    	}

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public String getItemName() {
			return itemName;
		}

		public void setItemName(String itemName) {
			this.itemName = itemName;
		}

		public double getUnitPrice() {
			return unitPrice;
		}

		public void setUnitPrice(double unitPrice) {
			this.unitPrice = unitPrice;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public double getItemTotal() {
			return itemTotal;
		}

		public void setItemTotal(double itemTotal) {
			this.itemTotal = itemTotal;
		}
    	
    	
    	
    }
    
    // Constructors
    public AccountDetails() {
    	this.purchaseItems = new ArrayList<>();
    }
    
    public AccountDetails(String customerId, String customerName, String accountNumber, String email, String phoneNumber, String address,  
                         double totalAmount, String invoiceId, String paymentId, String paymentMethod, Timestamp paymentDate) {
    	this();
        this.customerId = customerId;
        this.customerName = customerName;
        this.accountNumber = accountNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.totalAmount = totalAmount;
        this.invoiceId = invoiceId;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }
    
    public void addPurchasedItems(String itemId, String itemName, double unitPrice, int quantity, double itemTotal) {
    	PurchaseItems item = new PurchaseItems(itemId, itemName, unitPrice, quantity, itemTotal);
    	
    	this.purchaseItems.add(item);
    }
    
    // Getters and Setters
    public String getCustomerId() { 
    	return customerId; 
    }
    public void setCustomerId(String customerId) { 
    	this.customerId = customerId; 
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
    
    public String getEmail() { 
    	return email; 
    }
    public void setEmail(String email) { 
    	this.email = email; 
    }
    
    public String getPhoneNumber() { 
    	return phoneNumber; 
    }
    public void setPhoneNumber(String phoneNumber) { 
    	this.phoneNumber = phoneNumber; 
    }
    
    public String getAddress() { 
    	return address; 
    }
    public void setAddress(String address) { 
    	this.address = address; 
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
    
    public Timestamp getPaymentDate() { 
    	return paymentDate; 
    }
    public void setPaymentDate(Timestamp paymentDate) { 
    	this.paymentDate = paymentDate; 
    }

	public List<PurchaseItems> getPurchaseItems() {
		return purchaseItems;
	}

	public void setPurchaseItems(List<PurchaseItems> purchaseItems) {
		this.purchaseItems = purchaseItems;
	}
    
}