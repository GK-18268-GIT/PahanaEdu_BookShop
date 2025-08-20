package com.system.db;

import com.system.model.Customer;
import com.system.model.AccountDetails;
import com.system.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
	
	
	public Admin getAdminByEmail(String email) throws SQLException {
		Admin admin = null;
        String sql = "SELECT * FROM admins WHERE email = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
        	PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    admin = new Admin(
                        rs.getString("adminId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("profileImage"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("createdAt"),
                        rs.getString("password")
                    );
                }
            }
        }
        return admin;
    }
	
	public List<AccountDetails> getCustomerAccountDetails() throws SQLException {
	    List<AccountDetails> accountDetailsList = new ArrayList<>();
	    
	    String query = "SELECT " +
	                  "c.customerId, " +
	                  "CONCAT(c.firstName, ' ', c.lastName) AS customerName, " +
	                  "c.accountNumber, " +
	                  "c.email, " +
	                  "c.phoneNumber, " +
	                  "c.address, " +
	                  "p.paymentId, " +
	                  "p.paymentDate, " +
	                  "p.paymentMethod, " +
	                  "inv.invoiceId, " +
	                  "inv.totalAmount, " +
	                  "i.itemId, " +
	                  "i.itemName, " +
	                  "ct.quantity, " +
	                  "ct.unitPrice, " +
	                  "ct.totalPrice AS itemTotal " +
	                  "FROM customers c " +
	                  "JOIN invoice inv ON c.customerId = inv.customerId " +
	                  "JOIN payment p ON inv.invoiceId = p.invoiceId " +
	                  "JOIN cart ct ON inv.invoiceId = ct.invoiceId " +
	                  "JOIN items i ON ct.itemId = i.itemId " +
	                  "ORDER BY p.paymentDate DESC, inv.invoiceId";

	    try (Connection conn = DBConnectionFactory.getConnection();
	         PreparedStatement ps = conn.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {

	        AccountDetails currentDetails = null;
	        String currentInvoiceId = null;

	        while (rs.next()) {
	            String invoiceId = rs.getString("invoiceId");
	            
	            if (currentDetails == null || !invoiceId.equals(currentInvoiceId)) {
	                if (currentDetails != null) {
	                    accountDetailsList.add(currentDetails);
	                }
	                
	                currentDetails = new AccountDetails();
	                currentDetails.setCustomerId(rs.getString("customerId"));
	                currentDetails.setCustomerName(rs.getString("customerName"));
	                currentDetails.setAccountNumber(rs.getString("accountNumber"));
	                currentDetails.setEmail(rs.getString("email"));
	                currentDetails.setPhoneNumber(rs.getString("phoneNumber"));
	                currentDetails.setAddress(rs.getString("address"));
	                currentDetails.setPaymentId(rs.getString("paymentId"));
	                currentDetails.setPaymentDate(rs.getTimestamp("paymentDate"));
	                currentDetails.setPaymentMethod(rs.getString("paymentMethod"));
	                currentDetails.setInvoiceId(invoiceId);
	                currentDetails.setTotalAmount(rs.getDouble("totalAmount"));
	                
	                currentInvoiceId = invoiceId;
	            }
	            
	            currentDetails.addPurchasedItems(
	                rs.getString("itemId"),
	                rs.getString("itemName"),
	                rs.getDouble("unitPrice"),
	                rs.getInt("quantity"),
	                rs.getDouble("itemTotal")
	            );
	        }
	        
	        if (currentDetails != null) {
	            accountDetailsList.add(currentDetails);
	        }
	    }
	    return accountDetailsList;
	}
	
	public boolean addNewAdmin(Admin admin) throws SQLException {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    
	    try {
	        conn = DBConnectionFactory.getConnection();
	        String adminId = generateAdminId(conn);
	        
	        String query = "INSERT INTO admins(adminId, firstName, lastName, profileImage, address, email, phoneNumber, "
	                     + "password, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        
	        ps = conn.prepareStatement(query);
	        ps.setString(1, adminId);
	        ps.setString(2, admin.getFirstName());
	        ps.setString(3, admin.getLastName());
	        ps.setString(4, admin.getProfileImage());
	        ps.setString(5, admin.getAddress());
	        ps.setString(6, admin.getEmail());
	        ps.setString(7, admin.getPhoneNumber());
	        ps.setString(8, admin.getPassword());
	        ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
	        
	        int rowsAffected = ps.executeUpdate();
	        return rowsAffected > 0;
	        
	    } catch(SQLException e) {
	        e.printStackTrace();
	        System.out.println("[Error]: Admin registration failed! " + e.getMessage());
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
	
	
	public String generateCustomerId(Connection conn) throws SQLException {
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
    
	public String generateAccountNumber(Connection conn, String firstname, String lastname) throws SQLException {
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
	
	public String generateAdminId(Connection conn) throws SQLException{
String query = "SELECT adminId FROM admins ORDER BY adminId DESC LIMIT 1";
        
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String lastId = rs.getString("adminId");
                if (lastId != null && lastId.startsWith("Adm-")) {
                    try {
                        int lastNum = Integer.parseInt(lastId.substring(4));
                        return "Adm-" + String.format("%04d", lastNum + 1);
                    } catch (NumberFormatException e) {
                    		e.printStackTrace();                    }
                }
            }
            return "Adm-0001";
        }
	}
	
	
}
