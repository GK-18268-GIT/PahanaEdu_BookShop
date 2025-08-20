<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu - Sign Up</title>
    
	<link rel="stylesheet" href="<c:url value='/css/adminRegister.css'/>" >
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>

<body>
	<div class = "register-container">
		<div class = "register-wrapper">
			<div class = "register-leftside">
				<img src = "./assets/pahanaEdu_logo.png" alt = ""></img>
				<h2 class = "title">Welcome to Pahana Edu</h2>
				<p class = "subtitle">Please, Register to continue</p>
			</div>
			<% if (session.getAttribute("errorMessage") != null) { %>
				<p style = "color: red;"><%= session.getAttribute("errorMessage") %></p>
				<% session.removeAttribute("errorMessage");  %>
			<% } %>
			
			<form action = "AdminServlet" method = "post" class = "signup-form" onsubmit="return validatePassword()" 
			enctype="multipart/form-data">
			<input type ="hidden" name ="action" value ="adminRegister">
				<div class = "input-box">
					<label for = "firstName">First name: </label>
					<input type = "text" id = "firstName" name = "firstName" placeholder = "Enter your first name" required>
				</div>
				
				<div class = "input-box">
					<label for = "lastName">Last name: </label>
					<input type = "text" id = "lastName" name = "lastName" placeholder = "Enter your last name" required>
				</div>
				
				<div class = "input-box">
					<label for = "profileImage">Profile Image: </label>
					<input type = "file" id = "profileImage" name = "profileImage" accept="image/*" required>
				</div>
						
				<div class = "input-box">
					<label for = "address">Address: </label>
					<input type = "text" id = "address" name = "address" placeholder = "Enter your address" required>
				</div>
				
				<div class = "input-box">
					<label for = "email">Email: </label>
					<input type = "text" id = "email" name = "email" placeholder = "Enter your email" required>
				</div>
				
				<div class = "input-box">
					<label for = "phoneNumber">Phone number: </label>
					<input type = "tel" id = "phoneNumber" name = "phoneNumber" placeholder = "Enter your phone number" required>
				</div>
				
				<div class = "input-box">
					<label for = "password">Password: </label>
					<input type = "password" id = "password" name = "password" placeholder = "Enter your password" required>
					<small>Password must contain: uppercase, lowercase, numbers, and 8+ characters</small>
				</div>
				
				<div class = "input-box">
					<label for = "confirmPassword">Confirm password: </label>
					<input type = "password" id = "confirmpassword" name = "confirmpassword" placeholder = "Confirm your password" required>
				</div>
				
				<div id = "password-error" style = "color: red; margin-bottom: 15px"></div>
				
				<button type ="submit" class ="btn">Sign Up</button>
				
				<c:if test="${not empty success}">
					<p style = "color: green">${success}</p>
				</c:if>
				
					<p class="login-link">Already have an account? <a href="AdminServlet?action=login">Sign In</a></p>
				
			</form>
			
			
		</div>
	</div>
		
	
	<script>
    function validatePassword() {
        var password = document.getElementById("password").value;
        var confirmPassword = document.getElementById("confirmpassword").value;
        var errorMessage = document.getElementById("password-error");
        
        var passwordPattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
        
        if(!passwordPattern.test(password)) {
            errorMessage.textContent = "Password must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters!";
            return false;
        }
        
        if(password !== confirmPassword) {
            errorMessage.textContent = "Passwords do not match!";
            return false;
        }
        
        errorMessage.textContent = "";
        return true;
    }
    
    </script>

	
</body>
</html>