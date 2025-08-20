<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Customers' Account Details</title>
    <link rel="stylesheet" href="<c:url value='/css/accountDetails.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<style>
        .back-to-menu {
            display: block;
            text-align: center;
            margin: 20px auto;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-weight: 500;
            width: fit-content;
        }
        
        .back-to-menu:hover {
            background-color: #45a049;
        }
    </style>
<body>
  <div class="container">
      <div class="header">
          <h1>Customers' Account Details</h1>
      </div>
      
      <c:choose>
          <c:when test="${not empty accountDetails && !accountDetails.isEmpty()}">
              <div class="account-cards">
                  <c:forEach var="detail" items="${accountDetails}">
                      <div class="account-card">
                          <div class="card-header">
                              <h2 class="customer-name">${detail.customerName}</h2>
                              <div class="customer-id">ID: ${detail.customerId}</div>
                              <div class="account-number">Account: ${detail.accountNumber}</div>
                          </div>
          
                          <div class="section-title">Invoice #${detail.invoiceId}</div>
                          
                          <div class="details-grid">
                              <div class="detail-item">
                                  <div class="detail-label">Payment ID</div>
                                  <div class="detail-value">${detail.paymentId}</div>
                              </div>
                              
                              <div class="detail-item">
                                  <div class="detail-label">Payment Date</div>
                                  <div class="detail-value">
                                      <fmt:formatDate value="${detail.paymentDate}" pattern="MMM dd, yyyy HH:mm"/>
                                  </div>
                              </div>
                              
                              <div class="detail-item">
                                  <div class="detail-label">Payment Method</div>
                                  <div class="detail-value">${detail.paymentMethod}</div>
                              </div>
                              
                              <div class="detail-item">
                                  <div class="detail-label">Total Amount</div>
                                  <div class="detail-value">
                                      <fmt:formatNumber value="${detail.totalAmount}" type="currency"/>
                                  </div>
                              </div>
                          </div>
                          
                          <div class="section-title">Purchased Items</div>
                          
                          <table class="items-table">
                              <thead>
                                  <tr>
                                      <th>Item ID</th>
                                      <th>Item Name</th>
                                      <th>Quantity</th>
                                      <th>Unit Price</th>
                                      <th>Total</th>
                                  </tr>
                              </thead>
                              <tbody>
                                  <c:forEach var="item" items="${detail.purchaseItems}">
                                      <tr>
                                          <td>${item.itemId}</td>
                                          <td>${item.itemName}</td>
                                          <td>${item.quantity}</td>
                                          <td><fmt:formatNumber value="${item.unitPrice}" type="currency"/></td>
                                          <td><fmt:formatNumber value="${item.itemTotal}" type="currency"/></td>
                                      </tr>
                                  </c:forEach>
                              </tbody>
                          </table>
                      </div>
                  </c:forEach>
              </div>
          </c:when>
          <c:otherwise>
              <div class="no-data">
                  <p>No account details found.</p>
              </div>
          </c:otherwise>
      </c:choose>
  </div>
  <a href="${pageContext.request.contextPath}/AdminServlet?action=dashboard" class="back-to-menu">
        Back to Main Menu
    </a>
</body>
</html>