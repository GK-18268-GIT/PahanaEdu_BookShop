package com.system.db;

import com.system.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    public Customer getCustomerByEmail(String email, String password) throws Exception {
        String query = "SELECT * FROM customers WHERE email = ? AND password = ?";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return new Customer(
                        rs.getString("customerId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("profileImage"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("accountNumber"),
                        rs.getString("password"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt")
                    );
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[ERROR]: Failed to fetch customer!");
        }
        return null;
    }
    
    public List<Customer> getAllCustomers() throws Exception {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()) {
                Customer customer = new Customer(
                    rs.getString("customerId"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("profileImage"),
                    rs.getString("address"),
                    rs.getString("email"),
                    rs.getString("phoneNumber"),
                    rs.getString("accountNumber"),
                    rs.getString("password"),
                    rs.getTimestamp("createdAt"),
                    rs.getTimestamp("updatedAt")
                );
                customers.add(customer);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[ERROR]: Failed to retrieve customers!");
        }
        return customers;
    }
    
    public  Customer getCustomerById(String customerId) throws Exception {
    	String query = "SELECT * FROM customers WHERE customerId = ?";
    	
    	try(Connection conn = DBConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(query)) {
    		
    		ps.setString(1, customerId);
    		
    		ResultSet rs = ps.executeQuery();
    		
    		if(rs.next()) {
    			return new Customer(
    				rs.getString("customerId"),
    				rs.getString("firstName"),
    				rs.getString("lastName"),
    				rs.getString("profileImage"),
    				rs.getString("address"),
    				rs.getString("email"),
    				rs.getString("phoneNumber"),
    				rs.getString("accountNumber"),
    				rs.getString("password"),
    				rs.getTimestamp("createdAt"),
    				rs.getTimestamp("updatedAt")

    			);
    		}
    		
    	} catch(SQLException e) {
    		e.printStackTrace();
    		System.out.println("Failed to fetch customer data!");
    	}
		return null;
    }
    
    public boolean updateCustomer(Customer customer) throws Exception {
        String query = "UPDATE customers SET firstName = ?, lastName = ?, email = ?, phoneNumber = ?, address = ?, updatedAt = ? WHERE customerId = ?";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhoneNumber());
            ps.setString(5, customer.getAddress());
            ps.setTimestamp(6, customer.getUpdatedAt());
            ps.setString(7, customer.getCustomerId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[ERROR]: Failed to update customer!");
        }
        return false;
    }
}