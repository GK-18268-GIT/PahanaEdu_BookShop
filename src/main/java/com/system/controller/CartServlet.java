package com.system.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.system.model.ManageItem;
import com.system.model.CartItem;
import com.system.db.ManageItemDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ManageItemDao manageItemDao;

    @Override
    public void init() {
        manageItemDao = new ManageItemDao();
        System.out.println("[DEBUG] ManageItemDao initialized: " + (manageItemDao != null));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if(action == null) {
            handleError(request, response, "Missing action parameter");
            return;
        }
        
        try {
            switch(action) {
                case "viewMyCart" :
                    viewMyCart(request, response);
                    break;
                case "remove" :
                    removeItemFromCart(request, response);
                    break;
                default:
                    handleError(request, response, "Invalid action!");
            }
        } catch(Exception e) {
            e.printStackTrace();
            handleError(request, response, "An error occurred: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if(action == null) {
            handleError(request, response, "Missing action parameter");
            return;
        }
        
        try {
            switch(action) {
                case "addItem" :
                    addItemToCart(request, response);
                    break;
                case "updateItem" :
                    updateCartItem(request, response);
                    break;
                default:
                    handleError(request, response, "Invalid action!");
            }
        } catch(Exception e) {
            e.printStackTrace();
            handleError(request, response, "An error occurred: " + e.getMessage());
        }
    }

    private void addItemToCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String itemId = request.getParameter("itemId");
        int quantity = 1;
        
        ManageItem item = manageItemDao.getItemById(itemId);
        if(item == null) {
            handleError(request, response, "Item not found!");
            return;
        }
        
        if(item.getStockQty() <= 0) {
        	request.getSession().setAttribute("error", "Item is out of stocl!");
        	response.sendRedirect(request.getContextPath() + "/ManageItemServlet?action=inStock");
        	return;
        }
        
        HttpSession session = request.getSession();
        Map<String, CartItem> cart = getCartFromSession(session);
        
        int itemRequest = quantity;
        if(cart.containsKey(itemId)) {
        	CartItem cartItem = cart.get(itemId);
        	itemRequest += cartItem.getQuantity();
        }
        
        if(itemRequest  > item.getStockQty()) {
        	request.getSession().setAttribute("error", "Can not add items more than available stock!");
        }
        
        if(cart.containsKey(itemId)) {
        	CartItem cartItem = cart.get(itemId);
        	cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
        	cart.put(itemId, new CartItem(item, quantity));
        }
        
        session.setAttribute("cart", cart);
        session.setAttribute("successMessage", "Item added successfully!");
        response.sendRedirect(request.getContextPath() + "/ManageItemServlet?action=inStock");
    }

    private void updateCartItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String itemId = request.getParameter("itemId");
        int quantity;
        
        try {
            quantity = Integer.parseInt(request.getParameter("quantity"));
            if(quantity < 1) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            handleError(request, response, "Invalid quantity value!");
            return;
        }
        
        ManageItem item = manageItemDao.getItemById(itemId);
        if(itemId == null) {
        	request.getSession().setAttribute("error", "Item Id not found!");
        	response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
        	return;
        }
        
        if(quantity  > item.getStockQty()) {
        	request.getSession().setAttribute("error", "Can not set quantity higher than available stock! Available item: " + item.getStockQty());
        	response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
        	return;
        }
        
        HttpSession session = request.getSession();
        Map<String, CartItem> cart = getCartFromSession(session);
        
        if(cart.containsKey(itemId)) {
            cart.get(itemId).setQuantity(quantity);
            session.setAttribute("cart", cart);
        }
        
        response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
    }

    private void viewMyCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, CartItem> cart = getCartFromSession(session);
        List<CartItem> cartItems = new ArrayList<>(cart.values());
        
        double grandTotal = 0;
        for(CartItem item : cartItems) {
            grandTotal += item.getTotalPrice();
        }
        
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("grandTotal", grandTotal);
        request.getRequestDispatcher("/WEB-INF/items/cartItems.jsp").forward(request, response);
    }

    private void removeItemFromCart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String itemId = request.getParameter("itemId");
        
        if(itemId == null || itemId.isEmpty()) {
            handleError(request, response, "Missing item ID!");
            return;
        }
        
        HttpSession session = request.getSession();
        Map<String, CartItem> cart = getCartFromSession(session);
        
        cart.remove(itemId);
        session.setAttribute("cart", cart);
        response.sendRedirect(request.getContextPath() + "/CartServlet?action=viewMyCart");
    }

    @SuppressWarnings("unchecked")
    private Map<String, CartItem> getCartFromSession(HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        return cart == null ? new HashMap<>() : cart;
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message) 
            throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
    }
}