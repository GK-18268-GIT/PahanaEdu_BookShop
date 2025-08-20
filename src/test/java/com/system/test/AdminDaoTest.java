package com.system.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.system.db.AdminDao;
import com.system.db.DBConnectionFactory;
import com.system.model.AccountDetails;
import com.system.model.Admin;
import com.system.model.Customer;

class AdminDaoTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    private AdminDao adminDao;

    private MockedStatic<DBConnectionFactory> mockedFactory;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        adminDao = new AdminDao();

        mockedFactory = Mockito.mockStatic(DBConnectionFactory.class);

        mockedFactory.when(DBConnectionFactory::getConnection).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        mockedFactory.close();
    }
    
    @Test
    void testGetAdminByEmail_WhenAdminExists() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); 
        when(mockResultSet.getString("adminId")).thenReturn("Adm-0001");
        when(mockResultSet.getString("firstName")).thenReturn("Test");
        when(mockResultSet.getString("lastName")).thenReturn("Admin");
        when(mockResultSet.getString("email")).thenReturn("test@example.com");
        when(mockResultSet.getString("password")).thenReturn("hashedPassword");

        Admin result = adminDao.getAdminByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("Adm-0001", result.getAdminId());
        assertEquals("Test", result.getFirstName());
        assertEquals("test@example.com", result.getEmail());

        verify(mockPreparedStatement).setString(1, "test@example.com");
    }

    @Test
    void testGetAdminByEmail() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 

        Admin result = adminDao.getAdminByEmail("nouser@example.com");

        assertNull(result);
    }

    @Test
    void testAddNewCustomer_Success() throws Exception {
        Customer customer = new Customer(null, "John", "Doe", "profile.jpg", "123 Street",
            "john@example.com", "1234567890", null, "hashedPassword", null, null);

        PreparedStatement mockIdStmt = mock(PreparedStatement.class);
        ResultSet mockIdRs = mock(ResultSet.class);
        when(mockConnection.prepareStatement(startsWith("SELECT customerId"))).thenReturn(mockIdStmt);
        when(mockIdStmt.executeQuery()).thenReturn(mockIdRs);
        when(mockIdRs.next()).thenReturn(false);
        
        PreparedStatement mockAccStmt = mock(PreparedStatement.class);
        ResultSet mockAccRs = mock(ResultSet.class);
        when(mockConnection.prepareStatement(startsWith("SELECT accountNumber"))).thenReturn(mockAccStmt);
        when(mockAccStmt.executeQuery()).thenReturn(mockAccRs);
        when(mockAccRs.next()).thenReturn(false); 

        when(mockConnection.prepareStatement(startsWith("INSERT INTO customers"))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);


        boolean result = adminDao.addNewCustomer(customer);

        assertTrue(result);

        verify(mockPreparedStatement).setString(1, "Cus-0001");
        verify(mockPreparedStatement).setString(2, "John");
        verify(mockPreparedStatement).setString(8, "PahanaEdu-JD-0001");
        verify(mockPreparedStatement).setTimestamp(eq(10), any(Timestamp.class));
    }


    @Test
    void testAddNewCustomer_Failure() throws Exception {
        Customer customer = new Customer(null, "John", "Doe", "profile.jpg", "123 Street",
                "john@example.com", "1234567890", null, "hashedPassword", null, null);

        PreparedStatement mockIdStmt = mock(PreparedStatement.class);
        ResultSet mockIdRs = mock(ResultSet.class);
        when(mockConnection.prepareStatement(startsWith("SELECT customerId"))).thenReturn(mockIdStmt);
        when(mockIdStmt.executeQuery()).thenReturn(mockIdRs);
        when(mockIdRs.next()).thenReturn(false);

        PreparedStatement mockAccStmt = mock(PreparedStatement.class);
        ResultSet mockAccRs = mock(ResultSet.class);
        when(mockConnection.prepareStatement(startsWith("SELECT accountNumber"))).thenReturn(mockAccStmt);
        when(mockAccStmt.executeQuery()).thenReturn(mockAccRs);
        when(mockAccRs.next()).thenReturn(false);

        when(mockConnection.prepareStatement(startsWith("INSERT INTO customers"))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("DB connection lost"));

        assertThrows(SQLException.class, () -> adminDao.addNewCustomer(customer));
    }
    
    @Test
    void testGetCustomerAccountDetails() throws SQLException {

        String query = "SELECT c.customerId, CONCAT(c.firstName, ' ', c.lastName) AS customerName, " +
                      "c.accountNumber, c.email, c.phoneNumber, c.address, p.paymentId, " +
                      "p.paymentDate, p.paymentMethod, inv.invoiceId, inv.totalAmount, " +
                      "i.itemId, i.itemName, ct.quantity, ct.unitPrice, ct.totalPrice AS itemTotal " +
                      "FROM customers c " +
                      "JOIN invoice inv ON c.customerId = inv.customerId " +
                      "JOIN payment p ON inv.invoiceId = p.invoiceId " +
                      "JOIN cart ct ON inv.invoiceId = ct.invoiceId " +
                      "JOIN items i ON ct.itemId = i.itemId " +
                      "ORDER BY p.paymentDate DESC, inv.invoiceId";
                      
        when(mockConnection.prepareStatement(query)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(mockResultSet.getString("customerId")).thenReturn("Cus-0001");
        when(mockResultSet.getString("customerName")).thenReturn("Jane Doe");
        when(mockResultSet.getString("invoiceId")).thenReturn("Inv-0001");
        when(mockResultSet.getDouble("totalAmount")).thenReturn(1500.0);
        
        when(mockResultSet.getString("itemId")).thenReturn("Item-001", "Item-002");
        when(mockResultSet.getString("itemName")).thenReturn("Laptop", "Mouse");
        when(mockResultSet.getInt("quantity")).thenReturn(1, 2);
        when(mockResultSet.getDouble("itemTotal")).thenReturn(1400.0, 100.0);

        List<AccountDetails> result = adminDao.getCustomerAccountDetails();

        assertNotNull(result);
        assertEquals(1, result.size(), "Should aggregate two rows into one AccountDetails object");
        
        AccountDetails details = result.get(0);
        assertEquals("Cus-0001", details.getCustomerId());
        assertEquals("Jane Doe", details.getCustomerName());
        assertEquals("Inv-0001", details.getInvoiceId());
        assertEquals(2, details.getPurchaseItems().size(), "Should have two purchased items");
    }

    @Test
    void testAddNewAdmin_Success() throws SQLException {
        Admin admin = new Admin(null, "Admin", "User", "admin.jpg", "456 Admin St",
            "admin@example.com", "9876543210", "hashedPassword", null);

        PreparedStatement mockIdStatement = mock(PreparedStatement.class);
        ResultSet mockIdResultSet = mock(ResultSet.class);
        when(mockConnection.prepareStatement(startsWith("SELECT adminId"))).thenReturn(mockIdStatement);
        when(mockIdStatement.executeQuery()).thenReturn(mockIdResultSet);
        when(mockIdResultSet.next()).thenReturn(false);

        PreparedStatement mockInsertStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(startsWith("INSERT INTO admins"))).thenReturn(mockInsertStatement);
        when(mockInsertStatement.executeUpdate()).thenReturn(1);

        boolean result = adminDao.addNewAdmin(admin);

        assertTrue(result);
        verify(mockInsertStatement).setString(1, "Adm-0001");
        verify(mockInsertStatement).setString(2, "Admin");
        verify(mockInsertStatement).setString(6, "admin@example.com");
        verify(mockInsertStatement).setTimestamp(eq(9), any(Timestamp.class));
    }
    
    @Test
    public void testGenerateCustomerId_FirstCustomer() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 
        
        String result = adminDao.generateCustomerId(mockConnection);
        
        assertEquals("Cus-0001", result);
    }

    @Test
    public void testGenerateCustomerId_ExistingCustomers() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("customerId")).thenReturn("Cus-0042");
        
        String result = adminDao.generateCustomerId(mockConnection);
        
        assertEquals("Cus-0043", result);
    }

    @Test
    public void testGenerateAccountNumber_FirstAccount() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 
        
        String result = adminDao.generateAccountNumber(mockConnection, "John", "Doe");
        
        assertEquals("PahanaEdu-JD-0001", result);
    }

    @Test
    public void testGenerateAccountNumber_ExistingAccounts() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("accountNumber")).thenReturn("PahanaEdu-JD-0042");
        
        String result = adminDao.generateAccountNumber(mockConnection, "John", "Doe");
        
        assertEquals("PahanaEdu-JD-0043", result);
    }

    @Test
    public void testGenerateAdminId_FirstAdmin() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 
        
        String result = adminDao.generateAdminId(mockConnection);
        
        assertEquals("Adm-0001", result);
    }

    @Test
    public void testGenerateAdminId_ExistingAdmins() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("adminId")).thenReturn("Adm-0042");
        
        String result = adminDao.generateAdminId(mockConnection);
        
        assertEquals("Adm-0043", result);
    }
}