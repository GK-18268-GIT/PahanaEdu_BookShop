package com.system.db;

import com.system.model.Invoice;
import com.system.model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PaymentDao {
	public void addInvoice(Invoice invoice) throws SQLException {
		String query = "INSERT INTO invoice (invoiceId, invoiceDate, totalAmount) VALUES (?, ?, ?)";
		
		try(Connection conn = DBConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(query)) {
				ps.setString(1, invoice.getInvoiceId());
				ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				ps.setDouble(3, invoice.getTotalAmount());
				
				ps.executeUpdate();
				
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to add invoice!");
		}
			
	}
	
	public void addPayment(Payment payment) throws SQLException {
		String query = "INSERT INTO payment (paymentId, invoiceId, paymentMethod, amount, paymentDate) VALUES(?, ?, ?, ?, ?)";
		
		try(Connection conn = DBConnectionFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setString(1, payment.getPaymentId());
			ps.setString(2, payment.getInvoiceId());
			ps.setString(3, payment.getPaymentMethod());
			ps.setDouble(4, payment.getAmount());
			ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			
			ps.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to add payment!");
		}
		
	}
	
	
	public String generateInvoiceId() throws Exception {
        String query = "SELECT invoiceId FROM invoice ORDER BY invoiceDate DESC LIMIT 1";
        String defaultId = "Inv-" + System.currentTimeMillis(); 
        
        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String lastInvoiceId = rs.getString("invoiceId");
                if(lastInvoiceId != null && lastInvoiceId.startsWith("Inv-")) {
                	try {
                		String numberStr = lastInvoiceId.substring(lastInvoiceId.indexOf("-") + 1);
                		long number = Long.parseLong(numberStr);
                		return "Inv-" + (number + 1);
                	} catch(NumberFormatException e) {
                		e.printStackTrace();
                		return defaultId;
                	}
                }
                
            }
            return defaultId; 
            
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return defaultId;
        }
    }
	
	public String generatePaymentId() throws Exception {
        String query = "SELECT paymentId FROM payment ORDER BY paymentDate DESC LIMIT 1";
        String defaultId = "Pay-" + System.currentTimeMillis(); 
        
        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String lastPaymentId = rs.getString("paymentId");
                if(lastPaymentId != null && lastPaymentId.startsWith("Pay-")) {
                	
                	try {
                		String numberStr = lastPaymentId.substring(lastPaymentId.indexOf("-") + 1);
                		long number = Long.parseLong(numberStr);
                		return "Pay-" + (number + 1);
                	} catch(NumberFormatException e) {
                		e.printStackTrace();
                		return defaultId;
                	}
                }
                
            }
            return defaultId; 
            
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return defaultId;
        }
    }
}
