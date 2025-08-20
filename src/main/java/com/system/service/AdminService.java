package com.system.service;

import java.util.List;

import com.system.db.AdminDao;
import com.system.model.AccountDetails;
import com.system.model.Admin;

public class AdminService {
	private static AdminService instance;
	private AdminDao adminDao;
	
	private AdminService() {
		this.adminDao = new AdminDao();
	}
	
	public static AdminService getInstance() {
		if(instance == null) {
			synchronized (AdminService.class) {
				if(instance == null) {
					instance = new AdminService();
				}
			}
		}
		return instance;
	}
	
	public boolean addNewCustomer() throws Exception {
		return adminDao.addNewCustomer(null);
	}
	
	public Admin getAdminByEmail(String email) throws Exception {
		return adminDao.getAdminByEmail(email);
	}
	
	public List<AccountDetails> getCustomerAccountDetails() throws Exception {
		return adminDao.getCustomerAccountDetails();
	}
	
	public boolean addNewAdmin() throws Exception {
		return adminDao.addNewAdmin(null);
	}
	
	public String generateCustomerId() throws Exception {
		return adminDao.generateCustomerId(null);
	}
	
	public String generateAccountNumber() throws Exception {
		return adminDao.generateAccountNumber(null, null, null);
	}
	
	public String generateAdminId() throws Exception {
		return adminDao.generateAdminId(null);
	}

}
