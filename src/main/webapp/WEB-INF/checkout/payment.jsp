<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Payment</title>
    <link rel="stylesheet" href="<c:url value='/css/payment.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    
</head>
<body>
    <div class="payment-container">
        <h2>Select Payment Method</h2>
        <form action="${pageContext.request.contextPath}/PaymentServlet" method="post">
            <input type="hidden" name="action" value="payment">
            
            <div class="payment-option">
                <input type="radio" id="cash" name="paymentMethod" value="Cash" checked>
                <label for="cash">Cash</label>
            </div>
            
            <div class="payment-option">
                <input type="radio" id="card" name="paymentMethod" value="Credit Card">
                <label for="card">Credit Card</label>
            </div>
            
            <div class="payment-option">
                <input type="radio" id="paypal" name="paymentMethod" value="PayPal">
                <label for="paypal">PayPal</label>
            </div>
            
            <button type="submit">Complete Payment</button>
        </form>
    </div>
</body>
</html>