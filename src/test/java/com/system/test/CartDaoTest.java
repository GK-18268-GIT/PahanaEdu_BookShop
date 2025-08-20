package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.system.db.CartDao;
import com.system.db.DBConnectionFactory;
import com.system.model.CartItem;
import com.system.model.Invoice;
import com.system.model.ManageItem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CartDaoTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    private CartDao cartDao;
    
    private MockedStatic<DBConnectionFactory> mockedFactory;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        cartDao = new CartDao();

        mockedFactory = Mockito.mockStatic(DBConnectionFactory.class);

        mockedFactory.when(DBConnectionFactory::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        mockedFactory.close();
    }

    @Test
    void testAddCartItems() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId("Inv-001");
        invoice.setCustomerId("Cus-001");
        invoice.setAccountNumber("Acc-001");
        
        ManageItem mockItem = mock(ManageItem.class);

        when(mockItem.getItemId()).thenReturn("Item-123");
        when(mockItem.getUnitPrice()).thenReturn(50.0);

        List<CartItem> items = new ArrayList<>();

        CartItem cartItem = new CartItem(mockItem, 2); 
        items.add(cartItem);
        invoice.setItems(items);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        cartDao.addCartItems(invoice);

        verify(mockPreparedStatement).setString(1, "Inv-001");
        verify(mockPreparedStatement).setString(2, "Item-123"); 
        verify(mockPreparedStatement).setInt(3, 2);
        verify(mockPreparedStatement).setDouble(4, 50.0); 
        verify(mockPreparedStatement).addBatch();
        verify(mockPreparedStatement).executeBatch();
    }

    @Test
    void testDeleteCartItemsByInvoice() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        cartDao.deleteCartItemsByInvoice("Inv-001");
        
        verify(mockPreparedStatement).setString(1, "Inv-001");
        verify(mockPreparedStatement).executeUpdate();
    }
}