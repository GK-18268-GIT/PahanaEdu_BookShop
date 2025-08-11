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
                    <div class="stock ${item.stockQty > 0 ? 'in-stock' : 'out-stock'}">
    					${item.stockQty > 0 ? 'In Stock (' += item.stockQty += ' available)' : 'Out of Stock'}
					</div>
                    
                    <c:if test="${item.status eq 'inStock'}">
                        <form class="add-to-cart-form" 
                              action="${pageContext.request.contextPath}/CartServlet" method="post">
                            <input type="hidden" name="action" value="addItem">
                            <input type="hidden" name="itemId" value="${item.itemId}">
                  
                            <button type="submit" class="add-to-cart">Add to Cart</button>
                        </form>
                    </c:if>
                    
                    <c:if test="${item.stockQty <= 0}">
    					<button class="add-to-cart" disabled>Out of Stock</button>
					</c:if>
                    
                </div>
            </div>
        </c:forEach>
    </div>
</body>
</html>