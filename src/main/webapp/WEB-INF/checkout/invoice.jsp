<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Invoice</title>
    <style>
        body {
            font-family: 'Nunito Sans', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .invoice-container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 30px;
            border-bottom: 2px solid #00796b;
            padding-bottom: 20px;
        }
        .invoice-title {
            color: #00796b;
            text-align: center;
            margin-bottom: 30px;
        }
        .invoice-details {
            display: flex;
            justify-content: space-between;
            margin-bottom: 30px;
        }
        .detail-box {
            flex: 1;
        }
        .detail-box:first-child {
            margin-right: 20px;
        }
        .detail-label {
            font-weight: bold;
            color: #00796b;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #00796b;
            color: white;
        }
        .total-row {
            font-weight: bold;
            font-size: 1.1em;
        }
        .total-cell {
            text-align: right;
            padding-right: 20px;
        }
        .payment-info {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 2px solid #00796b;
        }
        .actions {
            text-align: center;
            margin-top: 30px;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            font-size: 1em;
            display: inline-block;
            margin: 0 10px;
        }
        .print-btn {
            background-color: #00796b;
            color: white;
        }
        .home-btn {
            background-color: #ff6f00;
            color: white;
        }
    </style>
</head>
<body>
    <div class="invoice-container">
        <div class="header">
            <div>
                <h1>Pahana Edu Bookshop</h1>
                <p>123 Education Street, City</p>
                <p>Phone: (123) 456-7890</p>
                <p>Email: info@pahanaedubookshop.com</p>
            </div>
            <div>
                <h2>INVOICE</h2>
            </div>
        </div>
        
        <h1 class="invoice-title">Your Order Confirmation</h1>
        
        <div class="invoice-details">
            <div class="detail-box">
                <p><span class="detail-label">Invoice ID:</span> ${invoice.invoiceId}</p>
                <p><span class="detail-label">Invoice Date:</span> ${invoice.invoiceDate}</p>
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
        
        <div class="actions">
            <button onclick="window.print()" class="btn print-btn">Print Invoice</button>
            <a href="${pageContext.request.contextPath}/ManageItemServlet?action=inStock" 
               class="btn home-btn">Back to Store</a>
        </div>
    </div>
</body>
</html>