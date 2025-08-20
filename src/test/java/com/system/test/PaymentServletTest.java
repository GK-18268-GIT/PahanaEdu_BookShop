package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.system.controller.PaymentServlet;
import com.system.db.CartDao;
import com.system.db.ManageItemDao;
import com.system.db.PaymentDao;
import com.system.model.CartItem;
import com.system.model.Customer;
import com.system.model.Invoice;
import com.system.model.ManageItem;
import com.system.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PaymentServletTest {

    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private HttpSession mockSession;
    @Mock
    private RequestDispatcher mockRequestDispatcher;
    @Mock
    private PaymentDao mockPaymentDao;
    @Mock
    private ManageItemDao mockManageItemDao;
    @Mock
    private CartDao mockCartDao;

    @InjectMocks
    private PaymentServlet paymentServlet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        paymentServlet.init(); 
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockRequest.getRequestDispatcher(anyString())).thenReturn(mockRequestDispatcher);
    }

    @Test
    void testDoPost_PaymentProcess_Cash_Success() throws Exception {
        Customer mockCustomer = new Customer("Cus-001", "John", "Doe", null, "Address", 
            "test@example.com", "1234567890", "Acc-001", "pass", null, null);
        
        ManageItem mockItem = new ManageItem("Item-001", "Test", "Desc", "Cat", 10.0, 5, "img.jpg", "inStock", null, null);
        CartItem cartItem = new CartItem(mockItem, 2);
        
        Map<String, CartItem> cartMap = new HashMap<>();
        cartMap.put("Item-001", cartItem);

        when(mockSession.getAttribute("customer")).thenReturn(mockCustomer);
        when(mockSession.getAttribute("cart")).thenReturn(cartMap);
        when(mockRequest.getParameter("action")).thenReturn("payment");
        when(mockRequest.getParameter("paymentMethod")).thenReturn("Cash");

        when(mockPaymentDao.generateInvoiceId()).thenReturn("Inv-001");
        when(mockPaymentDao.generatePaymentId()).thenReturn("Pay-001");
        doNothing().when(mockPaymentDao).addInvoice(any(Invoice.class));
        doNothing().when(mockPaymentDao).addPayment(any(Payment.class));
        doNothing().when(mockCartDao).addCartItems(any(Invoice.class));
        when(mockManageItemDao.updateStockQuantities(anyString(), anyInt())).thenReturn(true);

        paymentServlet.doPost(mockRequest, mockResponse);

        verify(mockSession).removeAttribute("cart");
        verify(mockSession).setAttribute(eq("invoice"), any(Invoice.class));
        verify(mockSession).setAttribute(eq("payment"), any(Payment.class));
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testPaymentProcess_NotLoggedIn() throws Exception {
        when(mockRequest.getSession(false)).thenReturn(null);
        when(mockRequest.getParameter("action")).thenReturn("payment");
        
        paymentServlet.doPost(mockRequest, mockResponse);
        
        verify(mockSession).setAttribute("error", "Please login to complete your purchase");
        verify(mockResponse).sendRedirect(contains("AdminServlet?action=login"));
    }

    @Test
    void testPaymentProcess_EmptyCart() throws Exception {
        Customer mockCustomer = new Customer("Cus-001", "John", "Doe", null, "Address", 
            "test@example.com", "1234567890", "Acc-001", "pass", null, null);
        
        when(mockSession.getAttribute("customer")).thenReturn(mockCustomer);
        when(mockSession.getAttribute("cart")).thenReturn(new HashMap<>());
        when(mockRequest.getParameter("action")).thenReturn("payment");
        
        paymentServlet.doPost(mockRequest, mockResponse);

        verify(mockSession).setAttribute("error", "Your cart is empty!");
        verify(mockResponse).sendRedirect(contains("CartServlet?action=viewMyCart"));
    }

    @Test
    void testUpdateStockQuantities() throws Exception {
        ManageItem mockItem = new ManageItem("Item-001", "Test", "Desc", "Cat", 10.0, 5, "img.jpg", "inStock", null, null);
        CartItem cartItem = new CartItem(mockItem, 2);
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        
        when(mockManageItemDao.updateStockQuantities("Item-001", 2)).thenReturn(true);
        
        paymentServlet.updateStockQuantities(cartItems);
        
        verify(mockManageItemDao).updateStockQuantities("Item-001", 2);
    }
}