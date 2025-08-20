package com.system.model;

public class Admin extends User {
	private String adminId;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String address;
    private String phoneNumber;
    private String createdAt;
	
	public Admin (String email, String password) {
		super(email, password);
	}
	
	public Admin(String adminId, String firstName, String lastName, String profileImage, String address, String email, String phoneNumber, String createdAt, 
            		String password) {
		    super(email, password);
		    this.adminId = adminId;
		    this.firstName = firstName;
		    this.lastName = lastName;
		    this.profileImage = profileImage;
		    this.address = address;
		    this.phoneNumber = phoneNumber;
		    this.createdAt = createdAt;
		}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
}
