<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Shopping Cart</title>
    <link rel="stylesheet" href="<c:url value='/css/cartItems.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    
<script>
        function showAlert(message) {
            alert(message);
        }
        document.addEventListener('DOMContentLoaded', function() {
            const modal = document.getElementById("paymentModal");
            const checkoutBtn = document.querySelector(".checkout");
            const closeBtn = document.querySelector(".close");
            
            if (checkoutBtn && modal && closeBtn) {
                checkoutBtn.onclick = function(e) {
                    e.preventDefault();
                    modal.style.display = "block";
                }
                
                closeBtn.onclick = function() {
                    modal.style.display = "none";
                }
                
                window.onclick = function(event) {
                    if (event.target === modal) {
                        modal.style.display = "none";
                    }
                }
            }
        });
        
    </script>
</head>
<body>
    <c:if test="${not empty sessionScope.errorMessage}">
        <script>
            showAlert('${sessionScope.errorMessage}');
        </script>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>
    
    <div class="container">
        <h1>Your Shopping Cart</h1>
        
        <c:choose>
            <c:when test="${empty cartItems}">
                <div class="empty-cart">
                    <p>Your cart is empty.</p>
                    <a href="${pageContext.request.contextPath}/ManageItemServlet?action=inStock" class="btn continue-shopping">
                        Continue Shopping
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${cartItems}" var="cartItem">
                            <tr>
                                <td>${cartItem.item.itemName}</td>
                                <td><fmt:formatNumber value="${cartItem.item.unitPrice}" type="currency"/></td>
                                <td>
                                    <form class="update-form" action="${pageContext.request.contextPath}/CartServlet" method="post">
                                        <input type="hidden" name="action" value="updateItem">
                                        <input type="hidden" name="itemId" value="${cartItem.item.itemId}">
                                        <input type="number" name="quantity" value="${cartItem.quantity}" 
                                               min="1" max="${cartItem.item.stockQty}" class="update-input" required>
                                        <button type="submit" class="btn">Update</button>
                                    </form>
                                </td>
                                <td><fmt:formatNumber value="${cartItem.getTotalPrice()}" type="currency"/></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/CartServlet?action=remove&itemId=${cartItem.item.itemId}">
                                        Remove
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr class="total-row">
                            <td colspan="3" class="total-cell">Grand Total:</td>
                            <td><fmt:formatNumber value="${grandTotal}" type="currency"/></td>
                            <td></td>
                        </tr>
                    </tfoot>
                </table>
                
                <div class="actions">
                    <a href="${pageContext.request.contextPath}/ManageItemServlet?action=inStock" class="btn continue-shopping">
                        Continue Shopping
                    </a>
                    <a href="#" class="btn checkout">Proceed to Checkout</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
                
            <div id="paymentModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <h2>Select Payment Method</h2>
            <form action="${pageContext.request.contextPath}/PaymentServlet" method="post">
                <input type="hidden" name="action" value="payment">
                
                <div style="margin: 15px 0;">
                    <input type="radio" id="cash" name="paymentMethod" value="Cash" checked>
                    <label for="cash">Cash</label>
                </div>
                
                <div style="margin: 15px 0;">
                    <input type="radio" id="card" name="paymentMethod" value="Credit Card">
                    <label for="card">Credit Card</label>
                </div>
                
                <div style="margin: 15px 0;">
                    <input type="radio" id="paypal" name="paymentMethod" value="PayPal">
                    <label for="paypal">PayPal</label>
                </div>
                
                <button type="submit" class="btn checkout" 
                        style="background-color: #ff6f00; color: white; padding: 10px 20px; border: none; border-radius: 3px; cursor: pointer;">
                    Confirm Payment
                </button>
            </form>
        </div>
    </div>
    
    <script>
    function validateQuantity(input, maxStock) {
        if (input.value > maxStock) {
            alert(`Cannot order more than ${maxStock} items!`);
            input.value = maxStock;
        } else if (input.value < 1) {
            input.value = 1;
        }
    }
    </script>
    
</body>
</html>
