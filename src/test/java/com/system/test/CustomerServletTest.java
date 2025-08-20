package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.system.controller.CustomerServlet;
import com.system.db.AdminDao;
import com.system.db.CustomerDao;
import com.system.db.ManageItemDao;
import com.system.model.Customer;
import com.system.model.CustomerAccountDetail;
import com.system.model.ManageItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CustomerServletTest {

    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private HttpSession mockSession;
    @Mock
    private RequestDispatcher mockRequestDispatcher;
    @Mock
    private CustomerDao mockCustomerDao;
    @Mock
    private AdminDao mockAdminDao;
    @Mock
    private ManageItemDao mockManageItemDao;

    @InjectMocks
    private CustomerServlet customerServlet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getRequestDispatcher(anyString())).thenReturn(mockRequestDispatcher);
    }

    @Test
    void testDoGet_ShowLoginForm() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("login");
        
        customerServlet.doGet(mockRequest, mockResponse);
        
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
        verify(mockRequest).getRequestDispatcher("/WEB-INF/users/customerLogin.jsp");
    }

    @Test
    void testDoPost_LoginCustomer_Success() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("login");
        when(mockRequest.getParameter("email")).thenReturn("test@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        
        Customer mockCustomer = new Customer("Cus-001", "John", "Doe", null, "Address", 
            "test@example.com", "1234567890", null, "hashedPassword", null, null);
        
        when(mockCustomerDao.getCustomerByEmail(anyString(), anyString())).thenReturn(mockCustomer);
        
        customerServlet.doPost(mockRequest, mockResponse);
        
        verify(mockSession).setAttribute("customer", mockCustomer);
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoPost_LoginCustomer_Failure() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("login");
        when(mockRequest.getParameter("email")).thenReturn("wrong@example.com");
        when(mockRequest.getParameter("password")).thenReturn("wrongpass");
        
        when(mockCustomerDao.getCustomerByEmail(anyString(), anyString())).thenReturn(null);
        
        customerServlet.doPost(mockRequest, mockResponse);
        
        verify(mockRequest).setAttribute("errorMessage", "Invalid email or password!");
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testShowProfileForm() throws Exception {
        Customer mockCustomer = new Customer("Cus-001", "John", "Doe", null, "Address", 
            "test@example.com", "1234567890", null, "hashedPassword", null, null);
        
        when(mockSession.getAttribute("customer")).thenReturn(mockCustomer);
        when(mockCustomerDao.getCustomerById("Cus-001")).thenReturn(mockCustomer);
        
        customerServlet.showProfileForm(mockRequest, mockResponse);
        
        verify(mockRequest).setAttribute("customer", mockCustomer);
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testShowStoreForm() throws Exception {
        List<ManageItem> mockItems = new ArrayList<>();
        mockItems.add(new ManageItem("Item-001", "Test", "Desc", "Cat", 10.0, 5, "img.jpg", "inStock", null, null));
        
        when(mockManageItemDao.getAllItems()).thenReturn(mockItems);
        
        customerServlet.showStoreForm(mockRequest, mockResponse);
        
        verify(mockRequest).setAttribute("itemList", mockItems);
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testLogoutCustomer() throws Exception {
        when(mockRequest.getContextPath()).thenReturn("/context");
        
        customerServlet.logoutCustomer(mockRequest, mockResponse);
        
        verify(mockSession).invalidate();
        verify(mockResponse).sendRedirect("/context/CustomerServlet?action=login");
    }

    @Test
    void testUpdateCustomer_Success() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("update");
        when(mockRequest.getParameter("customerId")).thenReturn("Cus-001");
        when(mockRequest.getParameter("firstName")).thenReturn("Updated");
        when(mockRequest.getParameter("lastName")).thenReturn("Name");
        when(mockRequest.getParameter("email")).thenReturn("updated@example.com");
        when(mockRequest.getParameter("phoneNumber")).thenReturn("9876543210");
        when(mockRequest.getParameter("address")).thenReturn("New Address");
        
        Customer existingCustomer = new Customer("Cus-001", "John", "Doe", null, "Old Address", 
            "john@example.com", "1234567890", null, "pass", null, null);
        
        when(mockCustomerDao.getCustomerById("Cus-001")).thenReturn(existingCustomer);
        when(mockCustomerDao.updateCustomer(any(Customer.class))).thenReturn(true);
        
        customerServlet.doPost(mockRequest, mockResponse);
        
        verify(mockResponse).sendRedirect(contains("CustomerServlet?action=list"));
    }

    @Test
    void testHashPassword() {
        String password = "testPassword123";
        String hashed = customerServlet.hashPassword(password);
        
        assertNotNull(hashed);
        assertNotEquals(password, hashed);
        assertEquals(64, hashed.length());
    }
    
    @Test
    void testShowCustomerOwnAccountDetails() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("customerOwnDetails");

        Customer mockCustomer = new Customer("Cus-001", "John", "Doe", null, "Address", 
                "test@example.com", "1234567890", null, "hashedPassword", null, null);

        when(mockSession.getAttribute("customer")).thenReturn(mockCustomer);
        
        List<CustomerAccountDetail> mockDetails = new ArrayList<>();
        CustomerAccountDetail detail = new CustomerAccountDetail();
        detail.setCustomerName("John Doe");
        detail.setAccountNumber("Acc-001");
        mockDetails.add(detail);
        
        when(mockCustomerDao.getCustomerAccountDetails("Cus-001")).thenReturn(mockDetails);
        
        customerServlet.doGet(mockRequest, mockResponse);
        
        verify(mockRequest).setAttribute("accountDetails", mockDetails);
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testShowCustomerOwnAccountDetails_NotLoggedIn() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("customerOwnDetails");
        when(mockSession.getAttribute("customer")).thenReturn(null);
        
        customerServlet.doGet(mockRequest, mockResponse);
        
        verify(mockResponse).sendRedirect(contains("CustomerServlet?action=login"));
    }
}