package com.system.service;

import java.sql.SQLException;

import com.system.db.CartDao;
import com.system.model.CartItem;
import com.system.model.Invoice;

public class CartService {
	private static CartService instance;
	private CartDao cartDao;
	
	private CartService() {
		this.cartDao = new CartDao();
	}
	
	public static CartService getInstance() {
		if(instance == null) {
			synchronized (CartService.class) {
				if(instance == null) {
					instance = new CartService();
				}
			}
		}
		return instance;
	}
	
	public void addCartItems(Invoice invoice) throws SQLException {
		if(invoice == null) {
			throw new IllegalArgumentException("Invoice cannot be null");
		}
		cartDao.addCartItems(invoice);
	}
	
	public void deleteCartItemsByInvoice(String invoiceId) throws SQLException {
		if(invoiceId == null) {
			throw new IllegalArgumentException("Invoice Id cannot be null");
		}
		cartDao.deleteCartItemsByInvoice(invoiceId);
	}

}
