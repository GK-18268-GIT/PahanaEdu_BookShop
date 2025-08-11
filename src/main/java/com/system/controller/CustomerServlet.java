package com.system.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import java.sql.Timestamp;


import com.system.db.CustomerDao;
import com.system.db.AdminDao;
import com.system.model.Customer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDao customerDao;
    private AdminDao userDao;
    
    @Override
    public void init() {
        customerDao  = new CustomerDao();
        if(customerDao != null) {
            System.out.println("[DEBUG] CustomerDao initialized: " + (customerDao != null));
        } else {
            System.out.println("[DEBUG] CustomerDao initialization failed!");
        }
        
        userDao  = new AdminDao();
        if(userDao != null) {
            System.out.println("[DEBUG] AdminDao initialized: " + (userDao != null));
        } else {
            System.out.println("[DEBUG] AdminDao initialization failed!");
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
                case "login":
                    showLoginForm(request, response);
                    break;
                case "logout":
                    logoutCustomer(request, response);
                    break;
                case "list":
                    listCustomers(request, response);
                    break;
                case "edit":
                	showEditForm(request, response);
                	break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action!");
            }
        } catch(Exception e) {
            handleError(request, response, "[Error]: An error occurred while processing. Please try again!");
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if(action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
            return;
        }
        
        try {
            switch(action) {
                case "login":
                    loginCustomer(request, response);
                    break;
                case "update":
                	updateCustomer(request, response);
                	break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action!");
            }
        } catch(Exception e) {
            handleError(request, response, "[Error]: An error occurred while processing. Please try again!");
        }
    }
    
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/users/customerLogin.jsp").forward(request, response);
    }
    
    private void loginCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        try {
            String hashedPassword = hashPassword(password);
            Customer customer = customerDao.getCustomerByEmail(email, hashedPassword);
            
            if(customer != null) {
                HttpSession session = request.getSession();
                session.setAttribute("customer", customer);
                
                System.out.println("[DEBUG] Login Credentials: ");
                System.out.println("[DEBUG] Email: " + customer.getEmail());
                System.out.println("[DEBUG] Password: " + customer.getPassword());
                
                response.sendRedirect(request.getContextPath() + "/ManageItemServlet?action=inStock");
            } else {
                request.setAttribute("errorMessage", "Invalid email or password!");
                request.getRequestDispatcher("/WEB-INF/users/customerLogin.jsp").forward(request, response);
            }
        } catch(Exception e) {
            handleError(request, response, "An error occurred during login!");
        }
    }
    
    private void logoutCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        if(session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=login");
    }
    
    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Customer> customers = customerDao.getAllCustomers();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/WEB-INF/admin/listCustomers.jsp").forward(request, response);
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String customerId = request.getParameter("customerId");
    	if(customerId == null || customerId.isEmpty()) {
    		handleError(request, response, "Missing Customer Id!");
    	}
    	
    	Customer customer = customerDao.getCustomerById(customerId);
    	if(customer == null) {
    		handleError(request, response, "Customer not found");
    	}
    	
    	request.setAttribute("customer", customer);
    	request.getRequestDispatcher("/WEB-INF/admin/editCustomerData.jsp").forward(request, response);
    }
    
    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String customerId = request.getParameter("customerId");
    	if(customerId == null || customerId.isEmpty()) {
    		handleError(request, response, "Missing customer Id!");
    	}
    	
    	String firstName = request.getParameter("firstName");
    	String lastName = request.getParameter("lastName");
    	String email = request.getParameter("email");
    	String phoneNumber = request.getParameter("phoneNumber");
    	String address = request.getParameter("address");
    	Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
    	
    	Customer customer = customerDao.getCustomerById(customerId);
    	if(customer == null) {
    		handleError(request, response, "Customer not found!");
    	}
    	
    	customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setAddress(address);
        customer.setUpdatedAt(updatedAt);
        
        if(customerDao.updateCustomer(customer)) {
        	response.sendRedirect("CustomerServlet?action=list&success=Customer+updated+successfully");
        } else {
        	handleError(request, response, "Failed to update customer!");
        }
    	
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();    
            for(byte b: bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }    
    }
    
    private void handleError(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute("errorMessage", message);
        request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
    }
}