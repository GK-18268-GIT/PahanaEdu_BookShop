<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Invoice</title>
    <link rel="stylesheet" href="<c:url value='/css/invoice.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        .header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 30px;
            border-bottom: 2px solid #eee;
            padding-bottom: 20px;
        }
        .logo-container {
            display: flex;
            align-items: center;
        }
        .logo {
            max-height: 80px;
            margin-right: 20px;
        }
        .shop-info {
            flex-grow: 1;
        }
        .invoice-title {
            text-align: center;
            color: #2c3e50;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="invoice-container">
        <div class="header">
            <div class="logo-container">
                <img src="${pageContext.request.contextPath}/assets/pahanaEdu_logo.png" alt="Pahana Edu Logo" class="logo">
                <div class="shop-info">
                    <h1>Pahana Edu Bookshop</h1>
                    <p>123 Education Street, City</p>
                    <p>Phone: (123) 456-7890</p>
                    <p>Email: info@pahanaedubookshop.com</p>
                </div>
            </div>
            <div>
                <h2>INVOICE</h2>
                <p><span class="detail-label">Invoice ID:</span> ${invoice.invoiceId}</p>
                <p><span class="detail-label">Date:</span> ${invoice.invoiceDate}</p>
            </div>
        </div>
        
        <h1 class="invoice-title">Your Order Confirmation</h1>
        
        <div class="invoice-details">
            <div class="detail-box">
                <p><span class="detail-label">Customer:</span> ${customer.firstName} ${customer.lastName}</p>
                <p><span class="detail-label">Email:</span> ${customer.email}</p>
                <p><span class="detail-label">Phone:</span> ${customer.phoneNumber}</p>
                <p><span class="detail-label">Address:</span> ${customer.address}</p>
            </div>
            <div class="detail-box">
                <p><span class="detail-label">Payment ID:</span> ${payment.paymentId}</p>
                <p><span class="detail-label">Payment Method:</span> ${payment.paymentMethod}</p>
                <p><span class="detail-label">Payment Date:</span> ${payment.paymentDate}</p>
            </div>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>Item</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${cartItems}" var="item">
                    <tr>
                        <td>${item.item.itemName}</td>
                        <td><fmt:formatNumber value="${item.item.unitPrice}" type="currency"/></td>
                        <td>${item.quantity}</td>
                        <td><fmt:formatNumber value="${item.getTotalPrice()}" type="currency"/></td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr class="total-row">
                    <td colspan="3" class="total-cell">Grand Total:</td>
                    <td><fmt:formatNumber value="${invoice.totalAmount}" type="currency"/></td>
                </tr>
            </tfoot>
        </table>
        
        <div class="payment-info">
            <h3>Payment Information</h3>
            <p><strong>Status:</strong> Payment Successful</p>
            <p><strong>Amount Paid:</strong> <fmt:formatNumber value="${payment.amount}" type="currency"/></p>
            <p><strong>Payment Method:</strong> ${payment.paymentMethod}</p>
            <p><strong>Transaction ID:</strong> ${payment.paymentId}</p>
        </div>
        
        <div class="footer">
            <p>Thank you for your purchase!</p>
            <p>For any inquiries, please contact our customer service at support@pahanaedubookshop.com</p>
        </div>
        
        <div class="actions">
            <button onclick="window.print()" class="btn print-btn">Print Invoice</button>
            <a href="${pageContext.request.contextPath}/ManageItemServlet?action=inStock" 
               class="btn home-btn">Back to Store</a>
        </div>
    </div>
</body>
</html>