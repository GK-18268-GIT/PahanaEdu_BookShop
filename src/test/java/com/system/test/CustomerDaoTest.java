package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.system.db.AdminDao;
import com.system.db.CustomerDao;
import com.system.db.DBConnectionFactory;
import com.system.model.Customer;
import com.system.model.CustomerAccountDetail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerDaoTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    
    private CustomerDao customerDao;
    
    private MockedStatic<DBConnectionFactory> mockedFactory;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        customerDao = new CustomerDao();

        mockedFactory = Mockito.mockStatic(DBConnectionFactory.class);

        mockedFactory.when(DBConnectionFactory::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        mockedFactory.close();
    }

    @Test
    void testGetCustomerByEmail_Success() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("customerId")).thenReturn("Cus-001");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        
        Customer result = customerDao.getCustomerByEmail("test@example.com", "hashedPass");
        
        assertNotNull(result);
        assertEquals("Cus-001", result.getCustomerId());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetAllCustomers() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("customerId")).thenReturn("Cus-001");
        when(mockResultSet.getString("firstName")).thenReturn("John");
        
        List<Customer> result = customerDao.getAllCustomers();
        
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void testGetCustomerById() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("customerId")).thenReturn("Cus-001");
        when(mockResultSet.getString("phoneNumber")).thenReturn("1234567890");
        
        Customer result = customerDao.getCustomerById("Cus-001");
        
        assertNotNull(result);
        assertEquals("1234567890", result.getPhoneNumber());
    }

    @Test
    void testUpdateCustomer_Success() throws Exception {
        Customer customer = new Customer("Cus-001", "Updated", "Name", null, "Address", 
            "updated@example.com", "9876543210", null, null, null, new Timestamp(System.currentTimeMillis()));
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        boolean result = customerDao.updateCustomer(customer);
        
        assertTrue(result);
        verify(mockPreparedStatement).setString(1, "Updated");
        verify(mockPreparedStatement).setString(3, "updated@example.com");
        verify(mockPreparedStatement).setString(7, "Cus-001");
    }
    
    @Test
    void testGetCustomerAccountDetails_Success() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(mockResultSet.getString("firstName")).thenReturn("John");
        when(mockResultSet.getString("lastName")).thenReturn("Doe");
        when(mockResultSet.getString("accountNumber")).thenReturn("ACC123");
        when(mockResultSet.getString("itemName")).thenReturn("Laptop").thenReturn("Mouse");
        when(mockResultSet.getInt("quantity")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getDouble("unitPrice")).thenReturn(1200.00).thenReturn(25.00);
        when(mockResultSet.getDouble("totalPrice")).thenReturn(1200.00).thenReturn(50.00);
        when(mockResultSet.getString("invoiceId")).thenReturn("INV-001").thenReturn("INV-002");
        when(mockResultSet.getDouble("totalAmount")).thenReturn(1250.00).thenReturn(50.00);
        when(mockResultSet.getString("paymentId")).thenReturn("PAY-A1").thenReturn("PAY-A2");
        when(mockResultSet.getString("paymentMethod")).thenReturn("Credit Card").thenReturn("PayPal");

        List<CustomerAccountDetail> result = customerDao.getCustomerAccountDetails("Cus-001");

        assertNotNull(result);
        assertEquals(2, result.size(), "Should return two detail records");

        CustomerAccountDetail detail1 = result.get(0);
        assertEquals("John Doe", detail1.getCustomerName());
        assertEquals("ACC123", detail1.getAccountNumber());
        assertEquals("Laptop", detail1.getItemName());
        assertEquals(1, detail1.getQuantity());
        assertEquals(1200.00, detail1.getUnitPrice());
        assertEquals(1200.00, detail1.getTotalPrice());
        assertEquals("INV-001", detail1.getInvoiceId());
        assertEquals(1250.00, detail1.getTotalAmount());
        assertEquals("PAY-A1", detail1.getPaymentId());
        assertEquals("Credit Card", detail1.getPaymentMethod());

        CustomerAccountDetail detail2 = result.get(1);
        assertEquals("Mouse", detail2.getItemName());
        assertEquals(2, detail2.getQuantity());
        assertEquals("INV-002", detail2.getInvoiceId());

        verify(mockPreparedStatement).setString(1, "Cus-001");
    }
}