<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
    <style>
        body {
            font-family: 'Nunito Sans', sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        h1 {
            color: #00796b;
            text-align: center;
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
        tr:hover {
            background-color: #f5f5f5;
        }
        .total-row {
            font-weight: bold;
            font-size: 1.1em;
        }
        .total-cell {
            text-align: right;
            padding-right: 20px;
        }
        .actions {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            font-size: 1em;
        }
        .continue-shopping {
            background-color: #00796b;
            color: white;
        }
        .checkout {
            background-color: #ff6f00;
            color: white;
        }
        .btn:hover {
            opacity: 0.9;
        }
        .empty-cart {
            text-align: center;
            padding: 20px;
            font-size: 1.2em;
        }
        .update-form {
            display: flex;
            gap: 5px;
        }
        .update-input {
            width: 60px;
            padding: 5px;
        }
        
        .modal {
            display: none;
            position: fixed;
            z-index: 1000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0,0,0,0.5);
        }
        
        .modal-content {
            background-color: #fff;
            margin: 15% auto;
            padding: 20px;
            width: 50%;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        
        .close {
            float: right;
            cursor: pointer;
            font-size: 24px;
        }
    </style>
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
</body>
</html>
