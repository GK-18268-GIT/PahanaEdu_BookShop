package com.system.db;

import com.system.model.Customer;
import com.system.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AdminDao {
	
	public boolean addNewCustomer(Customer customer) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnectionFactory.getConnection();
            String customerId = generateCustomerId(conn);
            String accountNumber = generateAccountNumber(conn, customer.getFirstName(), customer.getLastName());
            
            String query = "INSERT INTO customers(customerId, firstName, lastName, profileImage, address, email,"
                         + " phoneNumber, accountNumber, password, createdAt)"
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(query);
            ps.setString(1, customerId);
            ps.setString(2, customer.getFirstName());
            ps.setString(3, customer.getLastName());
            ps.setString(4, customer.getProfileImage());
            ps.setString(5, customer.getAddress());
            ps.setString(6, customer.getEmail());
            ps.setString(7, customer.getPhoneNumber());
            ps.setString(8, accountNumber);
            ps.setString(9, customer.getPassword());
            ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[Error]: Registration failed! " + e.getMessage());
            throw e;
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
	
	public Admin getCustomer(String username, String password) throws Exception {
		Admin users = null;
		String query = "SELECT * FROM customers WHERE username = ? AND password  = ? ";
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBConnectionFactory.getConnection();
			ps = conn.prepareStatement(query);
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				users = new Admin(rs.getString("username"), rs.getString("password"));
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("[Error]: Failed to fatching data!");
		}
		return users;
		
	}
	
	private String generateCustomerId(Connection conn) throws SQLException {
        String query = "SELECT customerId FROM customers ORDER BY customerId DESC LIMIT 1";
        
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String lastId = rs.getString("customerId");
                if (lastId != null && lastId.startsWith("Cus-")) {
                    try {
                        int lastNum = Integer.parseInt(lastId.substring(4));
                        return "Cus-" + String.format("%04d", lastNum + 1);
                    } catch (NumberFormatException e) {
                    		e.printStackTrace();                    }
                }
            }
            return "Cus-0001";
        }
    }
    
	private String generateAccountNumber(Connection conn, String firstname, String lastname) throws SQLException {
        String subName = "PahanaEdu-" + firstname.substring(0, 1).toUpperCase() + 
                         lastname.substring(0, 1).toUpperCase();
        
        String query = "SELECT accountNumber FROM customers WHERE accountNumber LIKE ? " + 
                       "ORDER BY accountNumber DESC LIMIT 1";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, subName + "-%");
            
            try (ResultSet rs = ps.executeQuery()) {
                int lastNumber = 0;
                
                if (rs.next()) {
                    String lastAccount = rs.getString("accountNumber");
                    String numPart = lastAccount.substring(lastAccount.lastIndexOf('-') + 1);
                    try {
                        lastNumber = Integer.parseInt(numPart);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                return subName + "-" + String.format("%04d", lastNumber + 1);
            }
        }
    }
}
