<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pahana Edu Bookshop - Item Store</title>
    <style>
        body {
            font-family: 'Nunito Sans', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 20px;
            background-color: #00796b;
            color: white;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        .item-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
        }
        .item-card {
            border: 1px solid #ddd;
            padding: 15px;
            width: 250px;
            background: white;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .item-image {
            max-width: 100%;
            height: 200px;
            object-fit: cover;
            border-radius: 3px;
        }
        .image-caption {
            height: 200px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #f0f0f0;
            border-radius: 3px;
        }
        .in-stock {
            color: green;
            font-weight: bold;
        }
        .out-stock {
            color: red;
            font-weight: bold;
        }
        .add-to-cart-form {
            margin-top: 10px;
        }
        .quantity-input {
            width: 60px;
            padding: 5px;
            margin-right: 5px;
        }
        .add-to-cart {
            background-color: #00796b;
            color: white;
            border: none;
            padding: 6px 12px;
            border-radius: 3px;
            cursor: pointer;
        }
        .add-to-cart:hover {
            background-color: #004d40;
        }
        .view-cart {
            color: white;
            text-decoration: none;
            background: #ff6f00;
            padding: 5px 10px;
            border-radius: 3px;
        }
        .view-cart:hover {
            background: #e65100;
        }
    </style>
	</head>
<body>
    <c:if test="${not empty sessionScope.successMessage}">
        <script>
            showAlert('${sessionScope.successMessage}');
        </script>
        <c:remove var="successMessage" scope="session"/>
    </c:if>
    
    <c:if test="${not empty sessionScope.errorMessage}">
        <script>
            showAlert('${sessionScope.errorMessage}');
        </script>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>
    
    <header>
        <h1>Book Store</h1>
        <a href="${pageContext.request.contextPath}/CartServlet?action=viewMyCart" class="view-cart">
            View Cart (${not empty sessionScope.cart ? sessionScope.cart.size() : 0})
        </a>
    </header>
    
    <div class="item-container">
        <c:forEach var="item" items="${itemList}">
            <div class="item-card">
                <c:choose>
                    <c:when test="${not empty item.itemImage}">
                        <img src="${pageContext.request.contextPath}/resources/itemUploads/${item.itemImage}" alt="${item.itemName}" class="item-image">
                    </c:when>
                    <c:otherwise>
                        <div class="image-caption">No image available</div>
                    </c:otherwise>
                </c:choose>
                
                <div class="item-info">
                    <h3>${item.itemName}</h3>
                    <p>${item.itemDescription}</p>
                    <div class="unitPrice">
                        Price: <fmt:formatNumber value="${item.unitPrice}" type="currency"/>
                    </div>
                    <div class="stock ${item.status eq 'inStock' ? 'in-stock' : 'out-stock'}">
                        ${item.status eq 'inStock' ? 'In Stock (' += item.stockQty += ' available)' : 'Out of Stock'}
                    </div>
                    
                    <c:if test="${item.status eq 'inStock'}">
                        <form class="add-to-cart-form" 
                              action="${pageContext.request.contextPath}/CartServlet" 
                              method="post">
                            <input type="hidden" name="action" value="addItem">
                            <input type="hidden" name="itemId" value="${item.itemId}">
                            <label>Quantity:
                                <input type="number" name="quantity" value="1" min="1" 
                                       max="${item.stockQty}" class="quantity-input" required>
                            </label>
                            <button type="submit" class="add-to-cart">Add to Cart</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
</body>
</html>