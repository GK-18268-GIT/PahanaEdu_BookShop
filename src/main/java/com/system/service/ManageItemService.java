package com.system.service;

import com.system.db.AdminDao;
import com.system.db.ManageItemDao;
import com.system.model.ManageItem;
import java.util.ArrayList;
import java.util.List;

public class ManageItemService {
	private static ManageItemDao instance;
	private ManageItemDao manageItemDao;
	
	private ManageItemService() {
		this.manageItemDao = new ManageItemDao();
	}
	
	public static ManageItemDao getInstance() {
		if(instance == null) {
			synchronized (ManageItemDao.class) {
				if(instance == null) {
					instance = new ManageItemDao();
				}
			}
		}
		return instance;
	}
	
	public boolean addNewItem(ManageItem manageItem) throws Exception {
		return manageItemDao.addNewItem(manageItem);
	}
	
	public boolean updateItem(ManageItem manageItem) throws Exception {
		return manageItemDao.updateItem(manageItem);
	}
	
	public boolean deleteItem(String itemId) throws Exception {
		return manageItemDao.deleteItem(itemId);
	}
	
	 public List<ManageItem> getAllItems() throws Exception {
		 return manageItemDao.getAllItems();
	 }
	 
	 public ManageItem getItemById(String itemId) throws Exception {
		 return manageItemDao.getItemById(itemId);
	 }
	 
	 public List<ManageItem> getInStockItems() throws Exception {
		 return manageItemDao.getInStockItems();
				 
	 }
	 
	 public boolean updateStockQuantities(String itemId, int buyQuantity) throws Exception {
		 return manageItemDao.updateStockQuantities(itemId, buyQuantity);
	 }
	 
	 public String generateItemId() throws Exception {
		 return manageItemDao.generateItemId();
	 }
}
