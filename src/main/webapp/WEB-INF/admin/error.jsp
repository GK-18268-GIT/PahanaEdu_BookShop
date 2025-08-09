<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu - Error</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap"
        rel="stylesheet">
        
</head>
<body>

	<div class="error-container">
        <h1>Error Occurred</h1>
        <p>
            <strong>Sorry, there was a problem processing your request.</strong>
        </p>

        <c:if test="${not empty errorMessage}">
            <p><strong>Error Message:</strong> ${errorMessage}</p>
        </c:if>

        <a href="<%= request.getContextPath() %>/home">Go Back to Home</a>
    </div>

</body>
</html>