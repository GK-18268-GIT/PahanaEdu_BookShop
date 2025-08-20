package com.system.db;

import com.system.model.ManageItem;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ManageItemDao {
    public boolean addNewItem(ManageItem manageItem) throws Exception {
    	
    	if(manageItem.getStockQty() > 0) {
    		manageItem.setStatus("inStock");
    	} else {
    		manageItem.setStatus("outStock");
    	}
    	
        String query = "INSERT INTO items(itemId, itemName, itemDescription, category, unitPrice, stockQty, itemImage, status, "
                + "createdAt)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
        
        String itemId = generateItemId();
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, itemId);
            ps.setString(2, manageItem.getItemName());
            ps.setString(3, manageItem.getItemDescription());
            ps.setString(4, manageItem.getCategory());
            ps.setDouble(5, manageItem.getUnitPrice());
            ps.setInt(6, manageItem.getStockQty());
            ps.setString(7, manageItem.getItemImage());
            ps.setString(8, manageItem.getStatus());
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[Error]: Failed to add item!");
        }
        return false;
    }
    
    public boolean updateItem(ManageItem manageItem) throws Exception {
        if(manageItem.getStockQty() > 0) {
            manageItem.setStatus("inStock");
        } else {
            manageItem.setStatus("outStock");
        }
        
        String query = "UPDATE items SET itemName = ?, itemDescription = ?, category = ?, unitPrice = ?, stockQty = ?, itemImage = ?, "
                + "status = ?, updatedAt = ? WHERE itemId = ?";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1,  manageItem.getItemName());
            ps.setString(2, manageItem.getItemDescription());
            ps.setString(3, manageItem.getCategory());
            ps.setDouble(4, manageItem.getUnitPrice());
            ps.setInt(5, manageItem.getStockQty());
            ps.setString(6, manageItem.getItemImage());
            ps.setString(7, manageItem.getStatus());
            ps.setTimestamp(8, manageItem.getUpdatedAt());
            ps.setString(9, manageItem.getItemId());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[Error]: Failed to update item!");
        }
        return false;
    }
    
    public boolean deleteItem(String itemId) throws Exception {
        String query = "DELETE FROM items WHERE itemId = ?";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, itemId);
            int i = ps.executeUpdate();
            return i > 0; 
            
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[Error]: Failed to delete item!");
        }
        
        return false;
    }
    
    public List<ManageItem> getAllItems() throws Exception {
        List<ManageItem> itemList = new ArrayList<>();
        String query = "SELECT * FROM items";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()) {
                ManageItem manageItem = new ManageItem(
                        rs.getString("itemId"),
                        rs.getString("itemName"),
                        rs.getString("itemDescription"),
                        rs.getString("category"),
                        rs.getDouble("unitPrice"),
                        rs.getInt("stockQty"),
                        rs.getString("itemImage"),
                        rs.getString("status"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt")
                );
                
                itemList.add(manageItem);
            }
            
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[Error]: Failed to retrieve items!");
        }
        return itemList;
    }
    
    public ManageItem getItemById(String itemId) throws Exception {
        String query = "SELECT * FROM items WHERE itemId = ?";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, itemId);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()) {
                return new ManageItem(
                    rs.getString("itemId"),
                    rs.getString("itemName"),
                    rs.getString("itemDescription"),
                    rs.getString("category"),
                    rs.getDouble("unitPrice"),
                    rs.getInt("stockQty"),
                    rs.getString("itemImage"),
                    rs.getString("status"),
                    rs.getTimestamp("createdAt"),
                    rs.getTimestamp("updatedAt")
                );
            }
            
        }catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[Error]: Failed to retrieve item!");
        }
        return null;
    }
    
    public List<ManageItem> getInStockItems() throws Exception {
        List<ManageItem> itemList = new ArrayList<>();
        String query = "SELECT * FROM items WHERE status = 'inStock'";
        
        try(Connection conn = DBConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {
            
            while(rs.next()) {
                ManageItem item = new ManageItem(
                        rs.getString("itemId"),
                        rs.getString("itemName"),
                        rs.getString("itemDescription"),
                        rs.getString("category"),
                        rs.getDouble("unitPrice"),
                        rs.getInt("stockQty"),
                        rs.getString("itemImage"),
                        rs.getString("status"),
                        rs.getTimestamp("createdAt"),
                        rs.getTimestamp("updatedAt")
                    );
                itemList.add(item);
            }
            
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("[Error]: Failed to retrieve item!");
            throw e;
        }
        return itemList;
    }
    
    public boolean updateStockQuantities(String itemId, int buyQuantity) throws Exception {
    	ManageItem item = getItemById(itemId);
    	if(item == null) {
    		throw new Exception("Item not found: " + itemId);
    	}
    	
    	int currentStock = item.getStockQty();
    	int newStock = currentStock - buyQuantity;
    	
    	if(newStock < 0) {
    		throw new Exception("Not enough for purchase!" + itemId);
    	}
    	
    	String query = "UPDATE items SET stockQty = ?, updatedAt = ? WHERE itemId = ?";
    	
    	try(Connection conn = DBConnectionFactory.getConnection();
    		PreparedStatement ps = conn.prepareStatement(query)) {
    		
    		ps.setInt(1, newStock);
    		ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
    		ps.setString(3, itemId);
    		
    		int rowsAffected = ps.executeUpdate();
    		return rowsAffected > 0;
    		
    	} catch(SQLException e) {
    		e.printStackTrace();
    		System.out.println("Failed to update stock!");
    	}
		return false;
    	
    }
    
    public String generateItemId() throws Exception {
        String query = "SELECT itemId FROM items ORDER BY createdAt DESC LIMIT 1";
        
        try (Connection conn = DBConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                String lastId = rs.getString("itemId");
                
                if(lastId.startsWith("Item-")) {
                	try {
                		int lastNum = Integer.parseInt(lastId.substring(5));
                		return "Item-" + (lastNum + 1);
                	} catch(NumberFormatException e) {
                		
                	}
                }
               
            }
            return "Item-" + (System.currentTimeMillis() % 10000);
            
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return "Item-" + (System.currentTimeMillis() % 10000);
        }
    }
}