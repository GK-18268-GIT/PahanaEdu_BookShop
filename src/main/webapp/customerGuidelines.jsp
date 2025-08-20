<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>PahanaEdu - Customer Guidelines</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
	<style>
		body {
		    font-family: 'Montserrat', sans-serif;
		    margin: 0;
		    padding: 0;
		    background-color: #e6f7ff; 
		    color: #0b3d91; 
		}
	
	.container {
	    max-width: 800px;
	    margin: 40px auto;
	    padding: 25px;
	    background: #ffffff;
	    border-radius: 10px;
	    box-shadow: 2px 2px 15px rgba(11, 61, 145, 0.2);
	    border-left: 5px solid #0077cc; 
	}
	
	h2 {
	    color: #005fa3;
	    text-align: center;
	}
	
	h3 {
	    color: #004a87;
	}
	
	ul {
	    margin-left: 20px;
	}
	
	ul li {
	    line-height: 1.8;
	}
	
	.back-link {
	    display: inline-block;
	    margin-top: 20px;
	    padding: 10px 15px;
	    text-decoration: none;
	    color: white;
	    background-color: #0077cc;
	    border-radius: 5px;
	    font-weight: bold;
	    transition: 0.3s;
	}
	
	.back-link:hover {
	    background-color: #005fa3;
	}
	
	</style>
</head>

<body>
	<div class="container">
    <h2>System Usage Guidelines</h2>
    <p>Welcome to the PahanaEdu BookShop! Please follow these guidelines to use the system efficiently.</p>
    
    <h3>1.Registration and Login</h3>
    <ul>
        <li>Registration: Contact the administrator to create your customer account</li>
        <li>Use your email and password to login to the system </li>
        <li>After login to the system you can navigate to the "Your Profile" or "Store" </li>
    </ul>

    <h3>2. Browsing Products</h3>
    <ul>
        <li>View available books and Educational materials</li>
        <li>Check the item details, price, and availability</li>
    </ul>

    <h3>3. Making Purchase</h3>
    <ul>
        <li>Add Items to your cart</li>
        <li>Proceed to the checkout</li>
        <li>Choose your payment method (Cash/Credit Card)</li>
        <li>Receive invoice information</li>
    </ul>

    <h3>4. Account Management</h3>
    <ul>
    	<li>View Your account history</li>
    	<li>Update your profile</li>
    </ul>

    <h3>5. Viewing Account Details</h3>
    <ul>
        <li>Access your profile page</li>
        <li>Click "View Account details" to see</li>
        <li>Purchase history</li>
        <li>Invoice records</li>
        <li>Payment Informations</li>
        <li>Item details</li>
    </ul>
    
    <h3>6. Security Guidelines</h3>
    <ul>
        <li>Never share your login credentials/li>
        <li>Logout after each session</li>
        <li>Keep your contact information updated</li>
        <li>Report any suspicious activity immediately</li>
    </ul>
    
    <h3>6. Contact Information</h3>
    <ul>
        <li>Email: support@pahanaEdu.com</li>
        <li>Phone: (123) 456-7890</li>
        <li>Hours: 8.30am to 5.00pm </li>
    </ul>

    <a href="customer.jsp" class="back-link">Back to Home</a>
</div>

</body>
</html>
