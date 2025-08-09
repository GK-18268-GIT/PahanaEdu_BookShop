<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu - Sign In</title>
    <link rel="stylesheet" href="<c:url value='/css/customerLogin.css'/>" >
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>

<body>
    <div class="login-container">
        <div class="login-wrapper">
            <div class="login-leftside">
                <img src="${pageContext.request.contextPath}/assets/avatar.png" alt=""></img>
                <h2 class="title">Welcome back</h2>
                <p class="subtitle">Please, Login to continue</p>
            </div>
            
            <c:if test="${not empty errorMessage}">
                <p style="color: red;">${errorMessage}</p>
            </c:if>
            
            <form action="CustomerServlet" method="post" class="signup-form">
                <input type="hidden" name="action" value="login">
                <div class="input-box">
                    <label for="email" class="signup-label">Email</label>
                    <input type="email" id="email" name="email" placeholder="Enter your email" required>
                </div>
                
                <div class="input-box">
                    <label for="password" class="signup-label">Password</label>
                    <input type="password" id="password" name="password" placeholder="Enter your password" required>
                </div>
                
                <div class="forgot-password">
                    <a href="#" class="forgot-password">Forgot your password?</a>
                </div>
                <button type="submit" class="btn">Sign In</button>        
            </form>
        </div>
    </div>
</body>
</html>