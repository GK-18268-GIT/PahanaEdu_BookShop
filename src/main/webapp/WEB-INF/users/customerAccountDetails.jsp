<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Account Details</title>
    <link rel="stylesheet" href="<c:url value='/css/customerProfile.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        .account-details {
            margin-top: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 20px;
            background-color: #f9f9f9;
        }
        
        .account-header {
            margin-bottom: 20px;
        }
        
        .account-header h2 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .account-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        .account-table th, .account-table td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }
        
        .account-table th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        
        .account-table tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        
        .account-table tr:hover {
            background-color: #f1f1f1;
        }
        
        .back-button {
            margin-top: 20px;
            display: inline-block;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        
        .back-button:hover {
            background-color: #45a049;
        }
        
        .no-data {
            text-align: center;
            padding: 20px;
            color: #666;
        }
    </style>
</head>

<body>
    <div class="container">
        <h1>Account Details</h1>
        
        <div class="account-details">
            <div class="account-header">
                <h2>${accountDetails[0].customerName}</h2>
                <p><strong>Account Number:</strong> ${accountDetails[0].accountNumber}</p>
            </div>
            
            <c:choose>
                <c:when test="${not empty accountDetails}">
                    <table class="account-table">
                        <thead>
                            <tr>
                                <th>Item Name</th>
                                <th>Quantity</th>
                                <th>Unit Price</th>
                                <th>Total Price</th>
                                <th>Invoice ID</th>
                                <th>Payment ID</th>
                                <th>Payment Method</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="detail" items="${accountDetails}">
                                <tr>
                                    <td>${detail.itemName}</td>
                                    <td>${detail.quantity}</td>
                                    <td><fmt:formatNumber value="${detail.unitPrice}" type="currency"/></td>
                                    <td><fmt:formatNumber value="${detail.totalPrice}" type="currency"/></td>
                                    <td>${detail.invoiceId}</td>
                                    <td>${detail.paymentId}</td>
                                    <td>${detail.paymentMethod}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="no-data">
                        <p>No account details found.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        
        <a href="CustomerServlet?action=profile" class="back-button">Back to Profile</a>
    </div>
</body>
</html>