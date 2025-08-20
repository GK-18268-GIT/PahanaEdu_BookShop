package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.system.db.DBConnectionFactory;
import com.system.db.PaymentDao;
import com.system.model.Invoice;
import com.system.model.Payment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class PaymentDaoTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    
    private PaymentDao paymentDao;
    
    private MockedStatic<DBConnectionFactory> mockedFactory;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        paymentDao = new PaymentDao();
        
        mockedFactory = Mockito.mockStatic(DBConnectionFactory.class);

        mockedFactory.when(DBConnectionFactory::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        mockedFactory.close();
    }

    @Test
    void testAddInvoice_Success() throws Exception {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId("Inv-001");
        invoice.setInvoiceDate(new Timestamp(System.currentTimeMillis()));
        invoice.setTotalAmount(100.0);
        invoice.setCustomerId("Cus-001");
        invoice.setAccountNumber("Acc-001");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        
        paymentDao.addInvoice(invoice);
        
        verify(mockPreparedStatement).setString(1, "Inv-001");
        verify(mockPreparedStatement).setDouble(3, 100.0);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testAddPayment_Success() throws Exception {
        Payment payment = new Payment();
        payment.setPaymentId("Pay-001");
        payment.setInvoiceId("Inv-001");
        payment.setPaymentMethod("Credit Card");
        payment.setAmount(100.0);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        
        paymentDao.addPayment(payment);
        
        verify(mockPreparedStatement).setString(1, "Pay-001");
        verify(mockPreparedStatement).setString(3, "Credit Card");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testGenerateInvoiceId() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 
        
        String result = paymentDao.generateInvoiceId();
        
        assertTrue(result.startsWith("Inv-"));
    }

    @Test
    void testGeneratePaymentId() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 
        
        String result = paymentDao.generatePaymentId();
        
        assertTrue(result.startsWith("Pay-"));
    }
}