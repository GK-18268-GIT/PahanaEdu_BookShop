package com.system.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.system.model.CartItem;
import com.system.model.Invoice;

public class CartDao {

    public void addCartItems(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO cart (invoiceId, itemId, quantity, unitPrice, totalPrice, customerId, accountNumber) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            for (CartItem cartItem : invoice.getItems()) {
                ps.setString(1, invoice.getInvoiceId());
                ps.setString(2, cartItem.getItem().getItemId());
                ps.setInt(3, cartItem.getQuantity());
                ps.setDouble(4, cartItem.getItem().getUnitPrice());
                ps.setDouble(5, cartItem.getTotalPrice());
                ps.setString(6, invoice.getCustomerId());
                ps.setString(7, invoice.getAccountNumber());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void deleteCartItemsByInvoice(String invoiceId) throws SQLException {
        String sql = "DELETE FROM cart WHERE invoiceId = ?";

        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoiceId);
            ps.executeUpdate();
        }
    }
}