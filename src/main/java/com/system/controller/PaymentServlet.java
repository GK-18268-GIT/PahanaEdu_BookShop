package com.system.controller;

import com.system.db.CartDao;
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
    
    PaymentDao paymentDao;
    ManageItemDao manageItemDao;
    CartDao cartDao;
    
    @Override
    public void init() {
        if (this.paymentDao == null) {
            this.paymentDao = new PaymentDao();
            System.out.println("[DEBUG] PaymentDao initialized by servlet.");
        }
        if (this.manageItemDao == null) {
            this.manageItemDao = new ManageItemDao();
            System.out.println("[DEBUG] ManageItemDao initialized by servlet.");
        }
        if (this.cartDao == null) {
            this.cartDao = new CartDao();
            System.out.println("[DEBUG] CartDao initialized by servlet.");
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("action");
        if(action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
            return;
        }
        try {
        	switch(action) {
        		case "paymentOption" :
        			showPaymentForm(request, response);
        	}
        } catch(Exception e) {
        	handleError(request, response, "[Error]: An error occurred while processing. Please try again!");
        }
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if(action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
            return;
        }
        try {
            switch(action) {
                case "payment":
                    paymentProcess(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action!");
            }
        } catch(Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            if(session.getAttribute("error") == null) {
            	session.setAttribute("error", "An error occurred while processing your payment. Please try again.");
            }
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
        }
    }
    
    @SuppressWarnings("unchecked")
    private void paymentProcess(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("customer") == null) {
            HttpSession newSession = request.getSession();
            newSession.setAttribute("error", "Please login to complete your purchase");
            response.sendRedirect(request.getContextPath() + "/AdminServlet?action=login");
            return;
        }

        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            session.setAttribute("error", "Your cart is empty!");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            session.setAttribute("error", "Please select a payment method");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
            return;
        }

        Customer customer = (Customer) session.getAttribute("customer");
        List<CartItem> cartItems = List.copyOf(cart.values());
        double totalAmount = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        
        try {
            String invoiceId = paymentDao.generateInvoiceId();
            String paymentId = paymentDao.generatePaymentId();
            
            Invoice invoice = new Invoice();
            invoice.setTotalAmount(totalAmount);
            invoice.setInvoiceId(invoiceId);
            invoice.setInvoiceDate(new Timestamp(System.currentTimeMillis()));
            invoice.setCustomerId(customer.getCustomerId());
            invoice.setAccountNumber(customer.getAccountNumber());
            invoice.setItems(cartItems);
            
            Payment payment = new Payment();
            payment.setPaymentMethod(paymentMethod);
            payment.setAmount(totalAmount);
            payment.setPaymentId(paymentId);
            payment.setInvoiceId(invoiceId);
            payment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
            
            if ("Cash".equals(paymentMethod)) {
                payment.setCardNumber("CASH_PAYMENT");
                payment.setCardName("CASH_PAYMENT");
                payment.setExpiryDate("00/00");
                payment.setCvv("000");
            } else {
                payment.setCardNumber(request.getParameter("cardNumber"));
                payment.setCardName(request.getParameter("cardName"));
                payment.setExpiryDate(request.getParameter("expiryDate"));
                payment.setCvv(request.getParameter("cvv"));
            }
            
            paymentDao.addInvoice(invoice);
            paymentDao.addPayment(payment);
            cartDao.addCartItems(invoice);
            
            updateStockQuantities(cartItems);
            
            session.removeAttribute("cart");
            session.setAttribute("invoice", invoice);
            session.setAttribute("cartItems", cartItems);
            session.setAttribute("payment", payment);
            
            request.getRequestDispatcher("/WEB-INF/checkout/invoice.jsp").forward(request, response);
            
        } catch(SQLException e) {
            e.printStackTrace();
            session.setAttribute("error", "Payment processing failed. Please try again.");
            response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
        }
    }
    
    private void showPaymentForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
    	request.getRequestDispatcher("/WEB-INF/checkout/payment.jsp").forward(request, response);
    }
    
    public void updateStockQuantities(List<CartItem> cartItems) throws Exception {
        for(CartItem cartItem : cartItems) {
            String itemId = cartItem.getItem().getItemId();
            int buyQuantity = cartItem.getQuantity();
            manageItemDao.updateStockQuantities(itemId, buyQuantity);
        }
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
    }
}