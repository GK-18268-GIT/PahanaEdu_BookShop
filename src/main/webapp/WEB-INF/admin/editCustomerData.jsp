<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Edit Customer Data</title>
    <link rel="stylesheet" href="<c:url value='css/editCustomerData.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">    
</head>
<body>
    <div class="container">
        <h1>Edit Customer</h1>
        
        <c:if test="${not empty errorMessage}">
            <div class="error-message" style="color: red; margin-bottom: 15px;">
                ${errorMessage}
            </div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/CustomerServlet" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="customerId" value="${customer.customerId}">
            
            <div class="form-group">
                <label for="firstName">First Name:</label>
                <input type="text" id="firstName" name="firstName" value="${customer.firstName}" required>
            </div>
            
            <div class="form-group">
                <label for="lastName">Last Name:</label>
                <input type="text" id="lastName" name="lastName" value="${customer.lastName}" required>
            </div>
            
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${customer.email}" required>
            </div>
            
            <div class="form-group">
                <label for="phoneNumber">Phone Number:</label>
                <input type="text" id="phoneNumber" name="phoneNumber" value="${customer.phoneNumber}" required>
            </div>
            
            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" id="address" name="address" value="${customer.address}" required>
            </div>
            
            <div class="form-group">
                <label for="accountNumber">Account Number:</label>
                <input type="text" id="accountNumber" name="accountNumber" value="${customer.accountNumber}" readonly>
            </div>
            
            <button type="submit" class="btn">Update Customer</button>
        </form>
    </div>
</body>
</html>