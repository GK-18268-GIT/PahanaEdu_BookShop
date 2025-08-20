<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Manage Items</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/listItems.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        .back-to-menu {
            display: inline-block;
            margin: 20px 0;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-weight: 500;
        }
        
        .back-to-menu:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Manage Items</h1>
        
        <!-- Success/Error Messages -->
        <c:if test="${not empty param.success}">
            <div class="success-message">${param.success}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>
        
        <div class="action-buttons">
            <a href="ManageItemServlet?action=showAddForm" class="btn">Add New Item</a>
            <a href="${pageContext.request.contextPath}/AdminServlet?action=dashboard" class="back-to-menu">
                Back to Main Menu
            </a>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${items}">
                    <tr>
                        <td>
                            <c:if test="${not empty item.itemImage}">
                                <img src="${pageContext.request.contextPath}/resources/itemUploads/${item.itemImage}"  
                                     alt="${item.itemName}" width="50">
                            </c:if>
                        </td>
                        <td>${item.itemName}</td>
                        <td>${item.itemDescription}</td>
                        <td>${item.category}</td>
                        <td>$${item.unitPrice}</td>
                        <td>${item.stockQty}</td>
                        <td>${item.stockQty > 0 ? 'In Stock' : 'Out of Stock'}</td>
                        <td class="actions">
                            <a href="ManageItemServlet?action=edit&itemId=${item.itemId}" 
                               class="btn-edit">Edit</a>
                            <form action="ManageItemServlet" method="post" class="delete-form">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="itemId" value="${item.itemId}">
                                <button type="submit" class="btn-delete" 
                                    onclick="return confirm('Are you sure?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>