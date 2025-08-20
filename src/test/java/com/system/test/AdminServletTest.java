package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.system.controller.AdminServlet;
import com.system.db.AdminDao;
import com.system.model.Admin;
import com.system.model.Customer;
import com.system.model.AccountDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AdminServletTest {
    @Mock
    private HttpServletRequest mockRequest;
    
    @Mock
    private HttpServletResponse mockResponse;
    
    @Mock
    private HttpSession mockSession;
    
    @Mock
    private RequestDispatcher mockRequestDispatcher;
    
    @Mock
    private AdminDao mockAdminDao;
    
    @Mock
    private Part mockPart;
    
    @Mock
    private ServletContext mockServletContext;

    @Mock
    private ServletConfig mockServletConfig;
    
    @InjectMocks
    private AdminServlet adminServlet;
    
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        adminServlet.init(mockServletConfig);
        adminServlet.setAdminDao(mockAdminDao);
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockRequest.getRequestDispatcher(anyString())).thenReturn(mockRequestDispatcher);
        when(mockRequest.getContextPath()).thenReturn("/context");
    }
    
    @Test
    void testDoGet_ShowDashboard_AdminRole() throws ServletException, IOException {
        when(mockRequest.getParameter("action")).thenReturn("dashboard");
        when(mockSession.getAttribute("userRole")).thenReturn("admin");

        adminServlet.doGet(mockRequest, mockResponse);

        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoGet_ShowDashboard_NoRole() throws ServletException, IOException {

        when(mockRequest.getParameter("action")).thenReturn("dashboard");
        when(mockSession.getAttribute("userRole")).thenReturn(null);
        
        adminServlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect(anyString());
    }

    @Test
    void testDoPost_Login_Success() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("login");
        when(mockRequest.getParameter("email")).thenReturn("admin123@gmail.com");
        when(mockRequest.getParameter("password")).thenReturn("password");

        Admin mockAdmin = new Admin("admin123@gmail.com", "password");
        mockAdmin.setEmail("admin123@gmail.com");

        String hashedPassword = adminServlet.hashPassword("password");
        mockAdmin.setPassword(hashedPassword);

        when(mockAdminDao.getAdminByEmail("admin123@gmail.com")).thenReturn(mockAdmin);

        adminServlet.doPost(mockRequest, mockResponse);

        verify(mockSession).setAttribute("user", "admin123@gmail.com");
        verify(mockSession).setAttribute("userRole", "admin");
        verify(mockResponse).sendRedirect(anyString());
    }
    
    @Test
    void testDoPost_Login_Failure() throws Exception {

        when(mockRequest.getParameter("action")).thenReturn("login");
        when(mockRequest.getParameter("email")).thenReturn("wrong@email.com");
        when(mockRequest.getParameter("password")).thenReturn("wrongpass");

        adminServlet.doPost(mockRequest, mockResponse);

        verify(mockRequest).setAttribute("error", "Invalid email or password!");
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoPost_AddNewAdmin_Success() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("adminRegister");
        when(mockRequest.getParameter("firstName")).thenReturn("Admin");
        when(mockRequest.getParameter("lastName")).thenReturn("User");
        when(mockRequest.getParameter("address")).thenReturn("123 Admin St");
        when(mockRequest.getParameter("email")).thenReturn("admin@example.com");
        when(mockRequest.getParameter("phoneNumber")).thenReturn("1234567890");
        when(mockRequest.getParameter("password")).thenReturn("Password123");
        when(mockRequest.getParameter("confirmpassword")).thenReturn("Password123");
        when(mockRequest.getPart("profileImage")).thenReturn(mockPart);
        when(mockPart.getSize()).thenReturn(10L);

        when(adminServlet.getServletContext()).thenReturn(mockServletContext);
        when(mockServletContext.getRealPath("")).thenReturn("some/fake/path");

        when(mockAdminDao.addNewAdmin(any(Admin.class))).thenReturn(true);

        when(mockRequest.getSession()).thenReturn(mockSession);

        adminServlet.doPost(mockRequest, mockResponse);

        verify(mockSession).setAttribute("user", "admin@example.com");
        verify(mockSession).setAttribute("userRole", "admin");

        verify(mockResponse).sendRedirect("/context/AdminServlet?action=dashboard");
    }
    
    @Test
    void testDoPost_AddNewAdmin_PasswordMismatch() throws Exception {

        when(mockRequest.getParameter("action")).thenReturn("adminRegister");
        when(mockRequest.getParameter("password")).thenReturn("Password123");
        when(mockRequest.getParameter("confirmpassword")).thenReturn("Different123");

        adminServlet.doPost(mockRequest, mockResponse);

        verify(mockRequest).setAttribute("error", "Passwords do not match!");
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoPost_AddNewCustomer_Success() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("register");
        when(mockRequest.getParameter("firstName")).thenReturn("John");
        when(mockRequest.getParameter("lastName")).thenReturn("Doe");
        when(mockRequest.getParameter("address")).thenReturn("123 Street");
        when(mockRequest.getParameter("email")).thenReturn("john@example.com");
        when(mockRequest.getParameter("phoneNumber")).thenReturn("1234567890");
        when(mockRequest.getParameter("password")).thenReturn("Password123");
        when(mockRequest.getParameter("confirmpassword")).thenReturn("Password123");
        when(mockRequest.getPart("profileImage")).thenReturn(mockPart);
        when(mockPart.getSize()).thenReturn(10L);

        when(adminServlet.getServletContext()).thenReturn(mockServletContext);
        when(mockServletContext.getRealPath("")).thenReturn("some/fake/path");

        when(mockAdminDao.addNewCustomer(any(Customer.class))).thenReturn(true);

        adminServlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect(anyString());
    }

    @Test
    void testShowAccountDetails() throws Exception {

        List<AccountDetails> mockDetails = new ArrayList<>();
        AccountDetails detail = new AccountDetails();
        detail.setCustomerId("Cus-0001");
        mockDetails.add(detail);
        
        when(mockAdminDao.getCustomerAccountDetails()).thenReturn(mockDetails);
        

        adminServlet.showAccountDetails(mockRequest, mockResponse);
        
        verify(mockRequest).setAttribute("accountDetails", mockDetails);
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testLogoutUser() throws Exception {

        when(mockRequest.getContextPath()).thenReturn("/context");

        adminServlet.logoutUser(mockRequest, mockResponse);

        verify(mockSession).invalidate();
        verify(mockResponse).sendRedirect("/context/AdminServlet?action=login");
    }

    @Test
    void testHashPassword() {

        String password = "testPassword123";

        String hashed = adminServlet.hashPassword(password);
        
        assertNotNull(hashed);
        assertNotEquals(password, hashed);
        assertEquals(64, hashed.length());
    }
}