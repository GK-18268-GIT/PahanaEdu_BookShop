package com.system.controller;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.system.db.AdminDao;
import com.system.model.Customer;
import com.system.model.Admin;
import com.system.model.AccountDetails;
//import com.system.util.RegisterEmailConfirmation;


//import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
//import javax.mail.MessagingException;

@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,    
	    maxFileSize = 1024 * 1024 * 10,     
	    maxRequestSize = 1024 * 1024 * 50   
	)


public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminDao adminDao;
	
	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}
       
	@Override
    public void init() {
		adminDao  = new AdminDao();
        if(adminDao != null) {
        	System.out.println("[DEBUG] AdminDao initialized: " + (adminDao !=null));
        } else {
        	System.out.println("[DEBUG] AdminDao initialized failed!");
        }
       
    }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if(action == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
			return;
		} 
		
		try {
			switch(action) {
			case "index" :
				request.getRequestDispatcher("/WEB-INF/users/adminLogin.jsp").forward(request, response);
				break;
			case "login" :
				request.getRequestDispatcher("/WEB-INF/users/adminLogin.jsp").forward(request, response);
				break;
			case "register" :
				request.getRequestDispatcher("/WEB-INF/users/customerRegister.jsp").forward(request, response);
				break;
			case "adminRegister" :
				request.getRequestDispatcher("/WEB-INF/users/adminRegister.jsp").forward(request, response);
				break;
			case "accountDetails":
				showAccountDetails(request, response);
				break;
			case "dashboard" :
				showDashboardForm(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action!");
		}
		
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("[Error] An error occured while processing. please try again!");
			request.getRequestDispatcher("/WEB-INF/users/adminLogin.jsp").forward(request, response);
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		try {
			if(action == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
			}
			
			switch(action) {
			case "register" :
				addNewCustomer(request, response);
				break;
			case "login" :
				authenticateUser(request, response);
				break;
			case "adminRegister" :
				addNewAdmin(request, response);
				break;
			case "logout" :
				logoutUser(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action!");
			
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("[Error] An error occured while processing. please try again!");
			request.getRequestDispatcher("/WEB-INF/users/adminLogin.jsp").forward(request, response);
		}
		
	}
	
	private void showDashboardForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    String userRole = (String) session.getAttribute("userRole");
	    
	    if(userRole != null && userRole.equals("admin")) {
	        request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);
	    } else {
	        response.sendRedirect(request.getContextPath() + "/AdminServlet?action=login");
	    }
	}
	
	public void logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			session.invalidate();
		}
		
		response.sendRedirect(request.getContextPath() + "/AdminServlet?action=login");
	}
	
	private void addNewCustomer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmpassword");
        
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        
        Part filePart = request.getPart("profileImage");
        String profileImage = "";
        
        if(filePart != null && filePart.getSize() > 0) {
            String fileName = UUID.randomUUID().toString() + "-" + extractFileName(filePart);
            String uploadPath = getServletContext().getRealPath("") + File.separator + "profileImage";
            
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()) uploadDir.mkdir();
            
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);
            profileImage = "profileImage/" + fileName;
        }
        
        if(!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("/WEB-INF/users/customerRegister.jsp").forward(request, response);
            return;
        }
        
        if(!password.matches(passwordPattern)) {
            request.setAttribute("error", "Password must contain at least one number, one uppercase and lowercase letter, and at least 8 characters!");
            request.getRequestDispatcher("/WEB-INF/users/customerRegister.jsp").forward(request, response);
            return;
        }
        
        String hashedPassword = hashPassword(password);
        
        Customer customer = new Customer(null, firstName, lastName, profileImage, address, email, phoneNumber, null, hashedPassword, null, null);
        
        boolean success = adminDao.addNewCustomer(customer);
        if(!success) {
            request.setAttribute("error", "Registration failed. Please try again!");
            request.getRequestDispatcher("/WEB-INF/users/customerRegister.jsp").forward(request, response);
            return;
        }
        
        response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");
		
		System.out.println("[DEBUG] Received Parameters:");
		System.out.println("[DEBUG] First name: " + firstName);
		System.out.println("[DEBUG] Last name: " + lastName);
		System.out.println("[DEBUG] Address: " + address);
		System.out.println("[DEBUG] Email: " + email);
		System.out.println("[DEBUG] Phone number: " + phoneNumber);
	}
	
	private void addNewAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmpassword");
        
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        
        Part filePart = request.getPart("profileImage");
        String profileImage = "";
        
        if(filePart != null && filePart.getSize() > 0) {
            String fileName = UUID.randomUUID().toString() + "-" + extractFileName(filePart);
            String uploadPath = getServletContext().getRealPath("") + File.separator + "profileImage";
            
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()) uploadDir.mkdir();
            
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);
            profileImage = "profileImage/" + fileName;
        }
        
        if(!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("/WEB-INF/users/customerRegister.jsp").forward(request, response);
            return;
        }
        
        if(!password.matches(passwordPattern)) {
            request.setAttribute("error", "Password must contain at least one number, one uppercase and lowercase letter, and at least 8 characters!");
            request.getRequestDispatcher("/WEB-INF/users/customerRegister.jsp").forward(request, response);
            return;
        }
        
        String hashedPassword = hashPassword(password);
        
        Admin admin = new Admin(null, firstName, lastName, profileImage, address, email, phoneNumber, null, hashedPassword);
        
        boolean success = adminDao.addNewAdmin(admin);
        if(!success) {
            request.setAttribute("error", "Registration failed. Please try again!");
            request.getRequestDispatcher("/WEB-INF/users/adminRegister.jsp").forward(request, response);
            return;
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("user", email);
        session.setAttribute("userRole", "admin");
        response.sendRedirect(request.getContextPath() + "/AdminServlet?action=dashboard");
		
		System.out.println("[DEBUG] Received Parameters:");
		System.out.println("[DEBUG] First name: " + firstName);
		System.out.println("[DEBUG] Last name: " + lastName);
		System.out.println("[DEBUG] Address: " + address);
		System.out.println("[DEBUG] Email: " + email);
		System.out.println("[DEBUG] Phone number: " + phoneNumber);
	}
	
	private void authenticateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    String email = request.getParameter("email");
	    String password = request.getParameter("password");
	    String hashedPassword = hashPassword(password);
	    
	    Admin admin = adminDao.getAdminByEmail(email);
	    
	    if(admin != null && admin.getPassword().equals(hashedPassword)) {
	        HttpSession session = request.getSession();
	        session.setAttribute("user", email);
	        session.setAttribute("userRole", "admin");
	        response.sendRedirect(request.getContextPath() + "/AdminServlet?action=dashboard");
	    } else {
	        request.setAttribute("error", "Invalid email or password!");
	        request.getRequestDispatcher("/WEB-INF/users/adminLogin.jsp").forward(request, response);
	    }
	}
	
	public void showAccountDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    try {
	        List<AccountDetails> accountDetails = adminDao.getCustomerAccountDetails();
	        request.setAttribute("accountDetails", accountDetails);
	        request.getRequestDispatcher("/WEB-INF/admin/accountDetails.jsp").forward(request, response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        handleError(request, response, "Failed to load account details");
	    }
	}

	
	public String hashPassword(String password) {
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
	
	private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) {
            return "unknown";
        }
        
        String[] items = contentDisposition.split(";");
        for(String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "unknown";
    }
	
	private void handleError(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
   	 request.setAttribute("errorMessage", message);
   	 request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
   	 return;
    }
}