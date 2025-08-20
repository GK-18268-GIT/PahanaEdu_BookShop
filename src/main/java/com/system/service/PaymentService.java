package com.system.service;

import java.sql.SQLException;

import com.system.db.PaymentDao;
import com.system.model.Payment;
import com.system.model.Invoice;

public class PaymentService {
    private static PaymentService instance;
    private PaymentDao paymentDao;
    
    private PaymentService() {
        this.paymentDao = new PaymentDao();
    }
    
    public static PaymentService getInstance() {
        if(instance == null) {
            synchronized (PaymentService.class) {
                if(instance == null) {
                    instance = new PaymentService();
                }
            }
        }
        return instance;
    }
    
    public void addInvoice(Invoice invoice) throws SQLException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        paymentDao.addInvoice(invoice);
    }
    
    public void addPayment(Payment payment) throws SQLException {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        paymentDao.addPayment(payment);
    }
    
    public String generateInvoiceId() throws SQLException {
        return paymentDao.generateInvoiceId();
    }
    
    public String generatePaymentId() throws SQLException {
        return paymentDao.generatePaymentId();
    }
}