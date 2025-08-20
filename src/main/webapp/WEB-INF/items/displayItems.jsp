<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Items Store</title>
    <link rel="stylesheet" href="<c:url value='/css/displayItems.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        .back-to-menu {
            display: block;
            text-align: center;
            margin: 30px auto;
            padding: 12px 25px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-weight: 500;
            width: fit-content;
            transition: background-color 0.3s;
        }
        
        .back-to-menu:hover {
            background-color: #45a049;
        }
        
        .already-in-cart {
            background-color: #ff9800;
            color: white;
            padding: 8px 12px;
            border-radius: 4px;
            font-size: 14px;
            margin-top: 10px;
        }
    </style>
</head>    
<body>
    <c:if test="${not empty sessionScope.successMessage}">
        <script>
            alert('${sessionScope.successMessage}');
           
            setTimeout(function() {
                window.location.href = '${pageContext.request.contextPath}/ManageItemServlet?action=inStock';
            }, 100);
        </script>
        <c:remove var="successMessage" scope="session"/>
    </c:if>
    
    <c:if test="${not empty sessionScope.errorMessage}">
        <script>
            alert('${sessionScope.errorMessage}');
            
            setTimeout(function() {
                window.location.href = '${pageContext.request.contextPath}/ManageItemServlet?action=inStock';
            }, 100);
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
                    <div class="stock ${item.stockQty > 0 ? 'in-stock' : 'out-stock'}">
                        ${item.stockQty > 0 ? 'In Stock (' += item.stockQty += ' available)' : 'Out of Stock'}
                    </div>
                    
                    <c:choose>
                        <c:when test="${item.stockQty > 0}">
                            <c:choose>
                                <c:when test="${not empty sessionScope.cart && not empty sessionScope.cart[item.itemId]}">
                                    <div class="already-in-cart">Already in cart</div>
                                </c:when>
                                <c:otherwise>
                                    <form class="add-to-cart-form" 
                                          action="${pageContext.request.contextPath}/CartServlet" method="post">
                                        <input type="hidden" name="action" value="addItem">
                                        <input type="hidden" name="itemId" value="${item.itemId}">
                                        <button type="submit" class="add-to-cart">Add to Cart</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                    </c:choose>
                </div>      
            </div>
        </c:forEach>
    </div>
    
    <a href="${pageContext.request.contextPath}/CustomerServlet?action=menu" class="back-to-menu">
        Back to Main Menu
    </a>
</body>
</html>