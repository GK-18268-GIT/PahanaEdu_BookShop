<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Customer Menu</title>
    <link rel="stylesheet" href="<c:url value='/css/customerMenu.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    
</head>

<body>
    <div class="menu-container">
        <h1>Customer Menu</h1>
        <p>Welcome ${customer.firstName} ${customer.lastName}!</p>
        
        <div class="navigate-cards">
            <a href="CustomerServlet?action=profile" class="button">
                <img src="${pageContext.request.contextPath}/assets/profile_icon.png" alt="View profile">
                View Your Profile
            </a>
            <a href="CustomerServlet?action=store" class="button">
                <img src="${pageContext.request.contextPath}/assets/store_icon.png" alt="View store">
                View Store
            </a>
        </div>
        
        <div class="logout-section">
            <a href="CustomerServlet?action=logout" class="logout-button">Logout</a>
        </div>
    </div>
</body>
</html>