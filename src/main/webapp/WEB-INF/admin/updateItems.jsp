<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Update Items</title>
    <link rel="stylesheet" href="<c:url value='/css/updateItem.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <div class="update-container">
        <div class="update-wrapper">
            <div class="update-leftside">
                <img src="${pageContext.request.contextPath}/assets/avatar.png" alt="">
                <h2 class="title">Update The Item</h2>
                <p class="subtitle">Enter Details To update Item</p>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <p style="color: red;">${errorMessage}</p>
            </c:if>
            
            <form action="ManageItemServlet" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="itemId" value="${item.itemId}">
                
                <div class="input-box">
                    <label for="itemName">Item Name: </label>
                    <input type="text" id="itemName" name="itemName" value="${item.itemName}" placeholder="Enter item name" required>
                </div>
                <div class="input-box">
                    <label for="itemDescription">Item Description: </label>
                    <input type="text" id="itemDescription" name="itemDescription" value="${item.itemDescription}" placeholder="Description" required>
                </div>
                <div class="input-box">
                    <label for="category">Category: </label>
                    <select id="category" name="category" required>
                        <option value="">Select Category</option>
                        <option value="stationery" ${item.category == 'stationery' ? 'selected' : ''}>Stationery</option>
                        <option value="textBooks" ${item.category == 'textBooks' ? 'selected' : ''}>Text Books</option>
                        <option value="accessories" ${item.category == 'accessories' ? 'selected' : ''}>Accessories</option>
                    </select>
                </div>
                <div class="input-box">
                    <label for="unitPrice">Unit Price: </label>
                    <input type="number" step="0.01" id="unitPrice" name="unitPrice"
                        value="${item.unitPrice}" placeholder="Enter unit price" required>
                </div>
                <div class="input-box">
                    <label for="stockQty">Stock Quantity</label>
                    <input type="number" id="stockQty" name="stockQty" min="0"
                        value="${item.stockQty}" placeholder="Enter stock quantity" required>
                </div>
                <div class="input-box">
                    <label for="itemImage">Item Image: </label>
                    <input type="file" id="itemImage" name="itemImage">
                    <c:if test="${not empty item.itemImage}">
                        <p>Current image: ${item.itemImage}</p>
                    </c:if>
                </div>
                <div class="input-box">
                    <label for="status">Status</label>
                    <select id="status" name="status" required>
                        <option value="inStock" ${item.status == 'inStock' ? 'selected' : ''}>In Stock</option>
                        <option value="outStock" ${item.status == 'outStock' ? 'selected' : ''}>Out of Stock</option>
                    </select>
                </div>
                <button type="submit" class="btn">Update Item</button> 
            </form>
        </div>
    </div>
</body>
</html>