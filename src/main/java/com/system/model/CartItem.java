package com.system.model;

public class CartItem {
	private ManageItem item;
	private int quantity;
	
	public CartItem(ManageItem item, int quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	public ManageItem getItem() {
		return item;
	}

	public void setItem(ManageItem item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return item.getUnitPrice() * quantity;
	}
	
	

}
