package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.system.db.DBConnectionFactory;
import com.system.db.ManageItemDao;
import com.system.model.ManageItem;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class ManageItemDaoTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    
    private ManageItemDao manageItemDao;
    
    private MockedStatic<DBConnectionFactory> mockedFactory;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        manageItemDao = new ManageItemDao();
        
        mockedFactory = Mockito.mockStatic(DBConnectionFactory.class);

        mockedFactory.when(DBConnectionFactory::getConnection).thenReturn(mockConnection);
    }
    
    @AfterEach
    void tearDown() {
        mockedFactory.close();
    }

    @Test
    void testAddNewItem_Success() throws Exception {

        ManageItem item = new ManageItem(null, "Test Item", "Desc", "Cat", 10.0, 5, "test.jpg", null, null, null);
        
        PreparedStatement mockSelectStmt = mock(PreparedStatement.class);
        ResultSet mockSelectRs = mock(ResultSet.class);
        when(mockConnection.prepareStatement(startsWith("SELECT itemId"))).thenReturn(mockSelectStmt);
        when(mockSelectStmt.executeQuery()).thenReturn(mockSelectRs);
        when(mockSelectRs.next()).thenReturn(false); 
        
        when(mockConnection.prepareStatement(startsWith("INSERT INTO items"))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        boolean result = manageItemDao.addNewItem(item);
        
        assertTrue(result);

        verify(mockPreparedStatement).setString(eq(1), anyString());
        verify(mockPreparedStatement).setString(eq(2), eq("Test Item"));
        verify(mockPreparedStatement).setDouble(eq(5), eq(10.0));
        verify(mockPreparedStatement).setInt(eq(6), eq(5));
        verify(mockPreparedStatement).setString(eq(8), eq("inStock"));
    }


    @Test
    void testUpdateItem_Success() throws Exception {
        ManageItem item = new ManageItem("Item-001", "Updated", "Desc", "Cat", 15.0, 10, "new.jpg", "inStock", 
        		null, new Timestamp(System.currentTimeMillis()));
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        boolean result = manageItemDao.updateItem(item);
        
        assertTrue(result);
        verify(mockPreparedStatement).setString(1, "Updated");
        verify(mockPreparedStatement).setDouble(4, 15.0);
        verify(mockPreparedStatement).setInt(5, 10);
        verify(mockPreparedStatement).setString(9, "Item-001");
    }

    @Test
    void testGetAllItems() throws Exception {
        List<ManageItem> expectedItems = new ArrayList<>();
        expectedItems.add(new ManageItem("Item-001", "Item 1", "Desc 1", "Cat 1", 10.0, 5, "img1.jpg", "inStock", null, null));
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("itemId")).thenReturn("Item-001");
        when(mockResultSet.getString("itemName")).thenReturn("Item 1");
        when(mockResultSet.getDouble("unitPrice")).thenReturn(10.0);
        
        List<ManageItem> result = manageItemDao.getAllItems();
        
        assertEquals(1, result.size());
        assertEquals("Item-001", result.get(0).getItemId());
    }

    @Test
    void testGetItemById() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("itemId")).thenReturn("Item-001");
        when(mockResultSet.getString("itemName")).thenReturn("Test Item");
        
        ManageItem result = manageItemDao.getItemById("Item-001");
        
        assertNotNull(result);
        assertEquals("Item-001", result.getItemId());
        assertEquals("Test Item", result.getItemName());
    }

    @Test
    void testDeleteItem_Success() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        boolean result = manageItemDao.deleteItem("Item-001");
        
        assertTrue(result);
        verify(mockPreparedStatement).setString(1, "Item-001");
    }

    @Test
    void testUpdateStockQuantities_Success() throws Exception {
        ManageItem mockItem = new ManageItem("Item-001", "Test", "Desc", "Cat", 10.0, 10, "img.jpg", "inStock", null, null);

        ManageItemDao spyDao = spy(manageItemDao);
        doReturn(mockItem).when(spyDao).getItemById("Item-001");

        when(mockConnection.prepareStatement(startsWith("UPDATE items"))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = spyDao.updateStockQuantities("Item-001", 3);

        assertTrue(result);
        verify(mockPreparedStatement).setInt(1, 7);
        verify(mockPreparedStatement).setString(3, "Item-001");
    }

    @Test
    void testGenerateItemId_FirstItem() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 
        
        String result = manageItemDao.generateItemId();
        
        assertTrue(result.startsWith("Item-"));
    }
}