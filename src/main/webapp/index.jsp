<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Welcome</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap"
        rel="stylesheet">
        
    <style>
    	body {
		    font-family: 'Nunito Sans', sans-serif;
		    display: flex;
		    justify-content: center;
		    align-items: center;
		    height: 100vh;
		    margin: 0;
		    background-color: #E3F2FD; 
		}

		.welcome-container {
		    text-align: center;
		    background: #ffffff;
		    padding: 40px;
		    border-radius: 12px;
		    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
		    max-width: 400px;
		}
		
		.header {
		    color: #00796b; 
		    font-weight: 700;
		    margin-bottom: 10px;
		}
		
		.description {
		    color: #00796b; 
		    font-size: 16px;
		    margin-bottom: 20px;
		}
		
		.button {
		    display: inline-block;
		    background: #00796b; 
		    color: white;
		    text-decoration: none;
		    padding: 10px 20px;
		    border-radius: 8px;
		    font-weight: 600;
		    margin: 5px;
		    transition: background 0.3s ease;
		}
		
		.button:hover {
		    background: #004d40;
		}
    	
    </style>
</head>
	<body>
	
	<div class ="welcome-container">
		<h1 class = "header" > Welcome To Pahana Edu Bookishop </h1>
		<p class = "description">Your trusted ride, anytime, anywhere.</p>
		<a href = "AdminServlet?action=index" class ="button">Get Start</a>
		<a href = "guidelines.jsp" class ="button">Guidelines</a>
	</div>
		
	</body>
</html>
