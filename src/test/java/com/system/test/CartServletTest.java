package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.system.controller.CartServlet;
import com.system.db.CartDao;
import com.system.db.ManageItemDao;
import com.system.model.CartItem;
import com.system.model.ManageItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CartServletTest {

    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private HttpSession mockSession;
    @Mock
    private RequestDispatcher mockRequestDispatcher;
    @Mock
    private ManageItemDao mockManageItemDao;
    @Mock
    private CartDao mockCartDao;

    @InjectMocks
    private CartServlet cartServlet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getRequestDispatcher(anyString())).thenReturn(mockRequestDispatcher);
    }

    @Test
    void testAddItemToCart_Success() throws Exception {
        String itemId = "Item-001";
        ManageItem mockItem = new ManageItem(itemId, "Test Item", "Desc", "Cat", 10.0, 5, "img.jpg", "inStock", null, null);
        
        when(mockRequest.getParameter("itemId")).thenReturn(itemId);
        when(mockManageItemDao.getItemById(itemId)).thenReturn(mockItem);
        
        Map<String, CartItem> cartMap = new HashMap<>();
        when(mockSession.getAttribute("cart")).thenReturn(cartMap);
        
        cartServlet.addItemToCart(mockRequest, mockResponse);
        
        verify(mockSession).setAttribute("cart", cartMap);
        assertEquals(1, cartMap.size());
        verify(mockResponse).sendRedirect(contains("ManageItemServlet?action=inStock"));
    }

    @Test
    void testUpdateCartItem_Success() throws Exception {
        String itemId = "Item-001";
        int newQuantity = 3;
        ManageItem mockItem = new ManageItem(itemId, "Test Item", "Desc", "Cat", 10.0, 5, "img.jpg", "inStock", null, null);
        
        when(mockRequest.getParameter("itemId")).thenReturn(itemId);
        when(mockRequest.getParameter("quantity")).thenReturn(String.valueOf(newQuantity));
        when(mockManageItemDao.getItemById(itemId)).thenReturn(mockItem);
        
        Map<String, CartItem> cartMap = new HashMap<>();
        cartMap.put(itemId, new CartItem(mockItem, 1));
        when(mockSession.getAttribute("cart")).thenReturn(cartMap);
        
        cartServlet.updateCartItem(mockRequest, mockResponse);
        
        assertEquals(newQuantity, cartMap.get(itemId).getQuantity());
        verify(mockResponse).sendRedirect(contains("CartServlet?action=viewMyCart"));
    }

    @Test
    void testViewMyCart() throws Exception {
        ManageItem mockItem = new ManageItem("Item-001", "Test", "Desc", "Cat", 10.0, 5, "img.jpg", "inStock", null, null);
        CartItem cartItem = new CartItem(mockItem, 2);
        
        Map<String, CartItem> cartMap = new HashMap<>();
        cartMap.put("Item-001", cartItem);
        when(mockSession.getAttribute("cart")).thenReturn(cartMap);
        
        cartServlet.viewMyCart(mockRequest, mockResponse);

        verify(mockRequest).setAttribute(eq("cartItems"), anyList());
        verify(mockRequest).setAttribute(eq("grandTotal"), anyDouble());
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testRemoveItemFromCart() throws Exception {
        String itemId = "Item-001";
        when(mockRequest.getParameter("itemId")).thenReturn(itemId);
        
        Map<String, CartItem> cartMap = new HashMap<>();
        cartMap.put(itemId, mock(CartItem.class));
        when(mockSession.getAttribute("cart")).thenReturn(cartMap);
        
        cartServlet.removeItemFromCart(mockRequest, mockResponse);
        
        assertTrue(cartMap.isEmpty());
        verify(mockResponse).sendRedirect(contains("CartServlet?action=viewMyCart"));
    }

    @Test
    void testProcessCheckout() throws Exception {
        ManageItem mockItem = new ManageItem("Item-001", "Test", "Desc", "Cat", 10.0, 5, "img.jpg", "inStock", null, null);
        CartItem cartItem = new CartItem(mockItem, 2);
        
        Map<String, CartItem> cartMap = new HashMap<>();
        cartMap.put("Item-001", cartItem);
        when(mockSession.getAttribute("cart")).thenReturn(cartMap);
        
        cartServlet.processCheckout(mockRequest, mockResponse);
        
        verify(mockRequest).setAttribute(eq("cartItems"), anyList());
        verify(mockRequest).setAttribute(eq("grandTotal"), anyDouble());
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }


    @Test
    void testProcessCheckout_EmptyCart() throws Exception {
        when(mockSession.getAttribute("cart")).thenReturn(new HashMap<>());
        
        cartServlet.processCheckout(mockRequest, mockResponse);
        
        verify(mockSession).setAttribute("error", "Your cart is empty!");
        verify(mockResponse).sendRedirect(contains("CartServlet?action=viewMyCart"));
    }
}