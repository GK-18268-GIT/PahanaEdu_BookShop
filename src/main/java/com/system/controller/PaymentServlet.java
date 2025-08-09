package com.system.controller;

import com.system.db.ManageItemDao;
import com.system.db.PaymentDao;
import com.system.model.Invoice;
import com.system.model.Payment;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.system.model.*;

public class PaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PaymentDao paymentDao;
	private ManageItemDao manageItemDao;
	
	@Override
	public void init() {
		paymentDao = new PaymentDao();
		manageItemDao = new ManageItemDao();
		
		if(paymentDao != null) {
        	System.out.println("[DEBUG] PaymentDao initialized: " + (paymentDao !=null));
        } else {
        	System.out.println("[DEBUG] PaymentDao initialized failed!");
        }
		
		if(manageItemDao != null) {
        	System.out.println("[DEBUG] ManageItemDao initialized: " + (manageItemDao !=null));
        } else {
        	System.out.println("[DEBUG] ManageItemDao initialized failed!");
        }
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if(action == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
			return;
		}
		
		try {
			switch(action) {
			case "payment" :
				paymentProcess(request, response);
				break;
			default :
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action!");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("[Error] An error occured while processing. please try again!");
			request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void paymentProcess (HttpServletRequest request, HttpServletResponse response) throws Exception {
		String paymentMethod = request.getParameter("paymentMethod");
		if(paymentMethod == null || paymentMethod.isEmpty()) {
			handleError(request, response, "Missing payment method!");
			return;
		}
		
		HttpSession session = request.getSession();

		Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
		if(cart == null|| cart.isEmpty()) {
			session.setAttribute("error", "Your cart is empty!");
			response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
			return;
		}
		
		List<CartItem> cartItems = List.copyOf(cart.values());
		double totalAmount = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
		
		try {
			String invoiceId = paymentDao.generateInvoiceId();
			String paymentId = paymentDao.generatePaymentId();
			
			Invoice invoice = new Invoice();
			invoice.setTotalAmount(totalAmount);
			invoice.setInvoiceId(invoiceId);
			invoice.setInvoiceDate(new Timestamp(System.currentTimeMillis()));
			
			Payment payment = new Payment();
			payment.setPaymentMethod(paymentMethod);
			payment.setAmount(totalAmount);
			payment.setPaymentId(paymentId);
			payment.setInvoiceId(invoiceId);
			payment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
			
			paymentDao.addInvoice(invoice);
			paymentDao.addPayment(payment);
			
			updateStockQuantities(cartItems);
			
			session.setAttribute("invoice", invoice);
			session.setAttribute("cartItems", cartItems);
			session.setAttribute("payment", payment);
			
			session.removeAttribute("cart");
			
			request.getRequestDispatcher("/WEB-INF/checkout/invoice.jsp").forward(request, response);
			
		} catch(SQLException e) {
			e.printStackTrace();
			handleError(request, response, "Payment process failed!");
			response.sendRedirect(request.getContextPath() + "CartServlet?action=viewMyCart");
		}
		
	}
	
	private void updateStockQuantities(List<CartItem> cartItems) throws Exception {
		for(CartItem cartItem : cartItems) {
			String itemId = cartItem.getItem().getItemId();
			int buyQuantity = cartItem.getQuantity();
			manageItemDao.updateStockQuantities(itemId, buyQuantity);
		}
	}
	 
	 private void handleError(HttpServletRequest request, HttpServletResponse response, String message) 
			 throws ServletException, IOException {
		 request.setAttribute("errorMessage", message);
		 request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
		 return;
	 }

	
}
