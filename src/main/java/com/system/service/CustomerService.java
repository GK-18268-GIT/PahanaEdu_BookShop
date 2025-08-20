package com.system.service;

import java.util.List;

import com.system.db.CustomerDao;
import com.system.model.Customer;
import com.system.model.CustomerAccountDetail;

public class CustomerService {
		private static CustomerService instance;
		private CustomerDao customerDao;
		
		private CustomerService() {
			this.customerDao = new CustomerDao();
		}
		
		public static CustomerService getInstance() {
			if(instance == null) {
				synchronized (CustomerService.class) {
					if(instance == null) {
						instance = new CustomerService();
					}
				}
			}
			return instance;
		}
	
	public Customer getCustomerByEmail(String email, String password) throws Exception {
		return customerDao.getCustomerByEmail(email, password);
	}
	
	public List<Customer> getAllCustomers() throws Exception {
		return customerDao.getAllCustomers();
	}
	
	public  Customer getCustomerById(String customerId) throws Exception {
		return customerDao.getCustomerById(customerId);
	}
	
	public boolean updateCustomer(Customer customer) throws Exception {
		return customerDao.updateCustomer(customer);
	}
	
	public List<CustomerAccountDetail> getCustomerAccountDetails(String customerId) throws Exception {
		return customerDao.getCustomerAccountDetails(customerId);
	}
	
	
}

