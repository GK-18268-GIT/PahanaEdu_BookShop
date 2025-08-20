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
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        
        String query = "INSERT INTO invoice (invoiceId, invoiceDate, totalAmount, customerId, accountNumber) " +
                      "VALUES (?, ?, ?, ?, ?)";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
                
            ps.setString(1, invoice.getInvoiceId());
            ps.setTimestamp(2, invoice.getInvoiceDate());
            ps.setDouble(3, invoice.getTotalAmount());
            ps.setString(4, invoice.getCustomerId());
            ps.setString(5, invoice.getAccountNumber());
            
            ps.executeUpdate();
            
        } catch(SQLException e) {
            System.err.println("Failed to add invoice: " + e.getMessage());
            throw new SQLException("Failed to add invoice", e);
        }
    }
    
    public void addPayment(Payment payment) throws SQLException {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        
        String query = "INSERT INTO payment (paymentId, invoiceId, paymentMethod, amount, paymentDate) VALUES(?, ?, ?, ?, ?)";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, payment.getPaymentId());
            ps.setString(2, payment.getInvoiceId());
            ps.setString(3, payment.getPaymentMethod());
            ps.setDouble(4, payment.getAmount());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            
            ps.executeUpdate();
            
        } catch(SQLException e) {
            System.err.println("Failed to add payment: " + e.getMessage());
            throw new SQLException("Failed to add payment", e);
        }
    }
    
    public String generateInvoiceId() throws SQLException {
        String query = "SELECT invoiceId FROM invoice ORDER BY invoiceId DESC LIMIT 1";
        
        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String lastInvoiceId = rs.getString("invoiceId");
                if(lastInvoiceId != null && lastInvoiceId.startsWith("Inv-")) {
                    try {
                        String numberStr = lastInvoiceId.substring(4);
                        long lastNumber = Long.parseLong(numberStr);
                        return "Inv-" + String.format("%04d", lastNumber + 1);
                    } catch(NumberFormatException e) {
                        return "Inv-" + (System.currentTimeMillis() % 10000);
                    }
                }
            }
            return "Inv-" + String.format("%04d", 1);
            
        } catch (SQLException e) {
            System.err.println("Failed to generate invoice ID: " + e.getMessage());
            return "Inv-" + (System.currentTimeMillis() % 10000);
        }
    }
    
    public String generatePaymentId() throws SQLException {
        String query = "SELECT paymentId FROM payment ORDER BY paymentId DESC LIMIT 1";
        
        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String lastPaymentId = rs.getString("paymentId");
                if(lastPaymentId != null && lastPaymentId.startsWith("Pay-")) {
                    try {
                        String numberStr = lastPaymentId.substring(4);
                        long lastNumber = Long.parseLong(numberStr);
                        return "Pay-" + String.format("%04d", lastNumber + 1);
                    } catch(NumberFormatException e) {
                        return "Pay-" + (System.currentTimeMillis() % 10000);
                    }
                }
            }
            return "Pay-" + String.format("%04d", 1);
            
        } catch (SQLException e) {
            System.err.println("Failed to generate payment ID: " + e.getMessage());
            return "Pay-" + (System.currentTimeMillis() % 10000);
        }
    }
}