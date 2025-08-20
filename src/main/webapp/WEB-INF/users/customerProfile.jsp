<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Customer Profile</title>
    <link rel="stylesheet" href="<c:url value='/css/customerProfile.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>

<body>
	<div class="container">
        <h1>Customer Profile</h1>
        
        <div class="profile-section">
            <div class="profile-image">
                <c:choose>
                    <c:when test="${not empty customer.profileImage}">
                        <img src="<c:url value='${customer.profileImage}'/>" alt="Profile Image">
                    </c:when>
                    <c:otherwise>
                        <img src="<c:url value='/images/default-profile.png'/>" alt="Default Profile Image">
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="profile-details">
                <div class="detail-row">
                    <span class="label">Customer ID:</span>
                    <span class="value">${customer.customerId}</span>
                </div>
                <div class="detail-row">
                    <span class="label">Name:</span>
                    <span class="value">${customer.firstName} ${customer.lastName}</span>
                </div>
                <div class="detail-row">
                    <span class="label">Email:</span>
                    <span class="value">${customer.email}</span>
                </div>
                <div class="detail-row">
                    <span class="label">Phone Number:</span>
                    <span class="value">${customer.phoneNumber}</span>
                </div>
                <div class="detail-row">
                    <span class="label">Address:</span>
                    <span class="value">${customer.address}</span>
                </div>
                <div class="detail-row">
                    <span class="label">Account Number:</span>
                    <span class="value">${customer.accountNumber}</span>
                </div>
                <div class="detail-row">
                    <span class="label">Member Since:</span>
                    <span class="value">
                        <fmt:formatDate value="${customer.createdAt}" pattern="MMMM d, yyyy"/>
                    </span>
                </div>
            </div>
        </div>
        
        <div class="action-buttons">
        <!--  
            <a href="CustomerServlet?action=edit&customerId=${customer.customerId}" class="btn">Edit Profile</a>
        -->
        	<a href="CustomerServlet?action=customerOwnDetails" class="btn">View Account Details</a>
            <a href="CustomerServlet?action=menu" class="btn">Back to Menu</a>
        </div>
    </div>
</body>
</html>