package com.system.model;

import java.sql.Timestamp;

public class Customer extends User {
    private String customerId;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String address;
    private String phoneNumber;
    private String accountNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public Customer(String customerId, String firstName, String lastName, String profileImage, String address, 
            String email, String phoneNumber, String accountNumber, String password, Timestamp createdAt, Timestamp updatedAt) {
    	super(email, password);
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}