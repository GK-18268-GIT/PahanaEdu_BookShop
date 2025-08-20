package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.system.controller.ManageItemServlet;
import com.system.db.ManageItemDao;
import com.system.model.ManageItem;
import org.apache.commons.fileupload.FileItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ManageItemServletTest {

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
    private Part mockPart;
    @Mock
    private FileItem mockFileItem;
    
    @Mock
    private ServletContext mockServletContext;

    @InjectMocks
    private ManageItemServlet manageItemServlet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getRequestDispatcher(anyString())).thenReturn(mockRequestDispatcher);
        
        when(mockRequest.getServletContext()).thenReturn(mockServletContext);
        
        when(mockServletContext.getRealPath(anyString())).thenReturn("C:/fake/path/for/testing/");
        
    }

    @Test
    void testDoGet_ShowAddForm() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("showAddForm");
        
        manageItemServlet.doGet(mockRequest, mockResponse);
        
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
        verify(mockRequest).getRequestDispatcher("/WEB-INF/admin/addItems.jsp");
    }

    @Test
    void testDoGet_ItemList() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("itemList");
        List<ManageItem> mockItems = new ArrayList<>();
        when(mockManageItemDao.getAllItems()).thenReturn(mockItems);
        
        manageItemServlet.doGet(mockRequest, mockResponse);
        
        verify(mockRequest).setAttribute("items", mockItems);
        verify(mockRequestDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoPost_AddNewItem_Success() throws Exception {
        when(mockRequest.getParameter("action")).thenReturn("add");
        Map<String, String> formData = new HashMap<>();
        formData.put("itemName", "Test Item");
        formData.put("itemDescription", "Test Description");
        formData.put("category", "Test Category");
        formData.put("unitPrice", "10.99");
        formData.put("stockQty", "5");
        
        when(mockManageItemDao.addNewItem(any(ManageItem.class))).thenReturn(true);
        
        manageItemServlet.addNewItems(mockRequest, mockResponse, formData, "test.jpg");
        
        verify(mockResponse).sendRedirect(contains("ManageItemServlet?action=itemList"));
    }

    @Test
    void testDoPost_UpdateItem_Success() throws Exception {
        Map<String, String> formData = new HashMap<>();
        formData.put("itemId", "Item-001");
        formData.put("itemName", "Updated Item");
        formData.put("itemDescription", "Updated Description");
        formData.put("category", "Updated Category");
        formData.put("unitPrice", "15.99");
        formData.put("stockQty", "10");
        
        ManageItem existingItem = new ManageItem("Item-001", "Old Item", "Old Desc", "Old Cat", 
            10.0, 5, "old.jpg", "inStock", new Timestamp(System.currentTimeMillis()), null);
        
        when(mockManageItemDao.getItemById("Item-001")).thenReturn(existingItem);
        when(mockManageItemDao.updateItem(any(ManageItem.class))).thenReturn(true);
        
        manageItemServlet.updateItems(mockRequest, mockResponse, formData, "new.jpg");
        
        verify(mockResponse).sendRedirect(contains("ManageItemServlet?action=itemList"));
    }

    @Test
    void testDeleteItem_Success() throws Exception {
        when(mockRequest.getParameter("itemId")).thenReturn("Item-001");
        ManageItem item = new ManageItem("Item-001", "Test", "Desc", "Cat", 10.0, 5, "test.jpg", "inStock", null, null);
        
        when(mockManageItemDao.getItemById("Item-001")).thenReturn(item);
        when(mockManageItemDao.deleteItem("Item-001")).thenReturn(true);

        manageItemServlet.deleteItems(mockRequest, mockResponse);

        verify(mockResponse).sendRedirect(contains("ManageItemServlet?action=itemList"));
    }

    @Test
    void testHandleFileUploads() throws Exception {

        when(mockRequest.getServletContext().getRealPath("/")).thenReturn("/test/path/");
        
        Map<String, String> formData = new HashMap<>();
        
        String fileName = manageItemServlet.handleFileUploads(mockRequest, mockResponse, formData);

        assertNotNull(fileName);
    }

    @Test
    void testItemFormData() {
        Map<String, String> formData = new HashMap<>();
        formData.put("itemName", "Test Item");
        formData.put("itemDescription", "Test Description");
        formData.put("category", "Test Category");
        formData.put("unitPrice", "10.99");
        formData.put("stockQty", "5");
        
        ManageItem item = new ManageItem();
        manageItemServlet.itemFormData(item, formData);
        
        assertEquals("Test Item", item.getItemName());
        assertEquals("Test Description", item.getItemDescription());
        assertEquals("Test Category", item.getCategory());
        assertEquals(10.99, item.getUnitPrice(), 0.001);
        assertEquals(5, item.getStockQty());
        assertEquals("inStock", item.getStatus());
    }
}