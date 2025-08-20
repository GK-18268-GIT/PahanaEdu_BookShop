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
    <link rel="stylesheet" href="<c:url value='/css/cartItems.css'/>">
    <link rel="stylesheet" href="<c:url value='/css/payment.css'/>">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        .payment-details {
            display: none;
            margin-top: 20px;
            padding: 20px;
            background: #f9f9f9;
            border-radius: 8px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        
        .payment-details.active {
            display: block;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
        }
        
        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
        }
        
        .card-row {
            display: flex;
            gap: 25px; /* Increased spacing between fields */
            margin-bottom: 15px;
        }
        
        .card-row .form-group {
            flex: 1;
        }
        
        .expiry-date-container {
            margin-right: 15px; /* Extra right margin for expiry date */
        }
        
        .cvv-container {
            margin-left: 10px; /* Extra left margin for CVV */
        }
        
        .card-icons {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }
        
        .card-icons img {
            height: 30px;
            width: auto;
        }
        
        .error-message {
            color: #f44336;
            font-size: 14px;
            margin-top: 5px;
            display: none;
        }
        
        .input-error {
            border-color: #f44336 !important;
        }
        
        .payment-option {
            margin-bottom: 15px;
            padding: 10px;
            background: white;
            border-radius: 5px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        
        .payment-option label {
            margin-left: 10px;
            cursor: pointer;
        }
        
        .form-actions {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: 500;
            text-decoration: none;
        }
        
        .back-btn {
            background: #757575;
            color: white;
        }
        
        .checkout-btn {
            background: #ff6f00;
            color: white;
        }
        
        .back-btn:hover {
            background: #616161;
        }
        
        .checkout-btn:hover {
            background: #e65100;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Complete Your Purchase</h1>
        
        <div class="order-summary">
            <h2>Order Summary</h2>
            <table>
                <thead>
                    <tr>
                        <th>Item</th><th>Price</th><th>Quantity</th><th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${cartItems}" var="cartItem">
                        <tr>
                            <td>${cartItem.item.itemName}</td>
                            <td><fmt:formatNumber value="${cartItem.item.unitPrice}" type="currency"/></td>
                            <td>${cartItem.quantity}</td>
                            <td><fmt:formatNumber value="${cartItem.getTotalPrice()}" type="currency"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr class="total-row">
                        <td colspan="3" class="total-cell">Grand Total:</td>
                        <td><fmt:formatNumber value="${grandTotal}" type="currency"/></td>
                    </tr>
                </tfoot>
            </table>
        </div>
        
        <div class="payment-section">
            <h2>Select Payment Method</h2>
            <form action="${pageContext.request.contextPath}/PaymentServlet" method="post" id="paymentForm">
                <input type="hidden" name="action" value="payment">
                
                <div class="payment-methods">
                    <div class="payment-option">
                        <input type="radio" id="cash" name="paymentMethod" value="Cash" checked>
                        <label for="cash">Cash</label>
                    </div>
                    <div class="payment-option">
                        <input type="radio" id="card" name="paymentMethod" value="Credit Card">
                        <label for="card">Credit/Debit Card</label>
                    </div>
                </div>
                
                <div class="payment-details" id="cardDetails">
                    <div class="form-group">
                        <label for="cardNumber">Card Number</label>
                        <input type="text" id="cardNumber" name="cardNumber" placeholder="1234 5678 9012 3456" maxlength="19">
                        <div id="cardNumberError" class="error-message">Please enter a valid 16-digit card number</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="cardName">Name on Card</label>
                        <input type="text" id="cardName" name="cardName" placeholder="John Doe">
                    </div>
                    
                    <div class="card-row">
                        <div class="form-group expiry-date-container">
                            <label for="expiryDate">Expiry Date</label>
                            <input type="text" id="expiryDate" name="expiryDate" placeholder="MM/YY" maxlength="5">
                            <div id="expiryDateError" class="error-message">Please enter a valid expiry date (MM/YY)</div>
                        </div>
                        <div class="form-group cvv-container">
                            <label for="cvv">CVV</label>
                            <input type="text" id="cvv" name="cvv" placeholder="123" maxlength="3">
                            <div id="cvvError" class="error-message">Please enter a valid 3-digit CVV</div>
                        </div>
                    </div>
                </div>
                
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/CartServlet?action=viewMyCart" class="btn back-btn">Back to Cart</a>
                    <button type="submit" class="btn checkout-btn">Complete Payment</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const paymentMethods = document.querySelectorAll('input[name="paymentMethod"]');
            const cardDetails = document.getElementById('cardDetails');
            const cardNumber = document.getElementById('cardNumber');
            const cardName = document.getElementById('cardName');
            const expiryDate = document.getElementById('expiryDate');
            const cvv = document.getElementById('cvv');
            const paymentForm = document.getElementById('paymentForm');
            
            // Toggle card details and required fields
            function toggleCardFields(isCard) {
                if (isCard) {
                    cardDetails.classList.add('active');
                    cardNumber.setAttribute('required', 'true');
                    cardName.setAttribute('required', 'true');
                    expiryDate.setAttribute('required', 'true');
                    cvv.setAttribute('required', 'true');
                } else {
                    cardDetails.classList.remove('active');
                    cardNumber.removeAttribute('required');
                    cardName.removeAttribute('required');
                    expiryDate.removeAttribute('required');
                    cvv.removeAttribute('required');
                }
            }
            
            paymentMethods.forEach(method => {
                method.addEventListener('change', function() {
                    toggleCardFields(this.value === 'Credit Card');
                });
            });
            
            // Init state (default Cash selected)
            toggleCardFields(false);
            
            // Format card number input
            cardNumber.addEventListener('input', function() {
                let value = this.value.replace(/\D/g, '');
                if (value.length > 0) {
                    value = value.match(new RegExp('.{1,4}', 'g')).join(' ');
                }
                this.value = value;
                validateCardNumber();
            });
            
            expiryDate.addEventListener('input', function() {
                let value = this.value.replace(/\D/g, '');
                if (value.length > 2) {
                    value = value.substring(0, 2) + '/' + value.substring(2, 4);
                }
                this.value = value;
                validateExpiryDate();
            });
            
            cvv.addEventListener('input', function() {
                this.value = this.value.replace(/\D/g, '');
                validateCVV();
            });
            
            // Validation on submit
            paymentForm.addEventListener('submit', function(e) {
                const selectedMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
                if (selectedMethod === 'Credit Card') {
                    const isCardNumberValid = validateCardNumber();
                    const isExpiryDateValid = validateExpiryDate();
                    const isCvvValid = validateCVV();
                    if (!isCardNumberValid || !isExpiryDateValid || !isCvvValid) {
                        e.preventDefault();
                    }
                }
            });
            
            function validateCardNumber() {
                const digitsOnly = cardNumber.value.replace(/\D/g, '');
                const errorElement = document.getElementById('cardNumberError');
                if (digitsOnly.length !== 16) {
                    cardNumber.classList.add('input-error');
                    errorElement.style.display = 'block';
                    return false;
                } else {
                    cardNumber.classList.remove('input-error');
                    errorElement.style.display = 'none';
                    return true;
                }
            }
            
            function validateExpiryDate() {
                const value = expiryDate.value;
                const errorElement = document.getElementById('expiryDateError');
                const regex = /^(0[1-9]|1[0-2])\/?([0-9]{2})$/;
                if (!regex.test(value)) {
                    expiryDate.classList.add('input-error');
                    errorElement.style.display = 'block';
                    return false;
                } else {
                    expiryDate.classList.remove('input-error');
                    errorElement.style.display = 'none';
                    return true;
                }
            }
            
            function validateCVV() {
                const value = cvv.value;
                const errorElement = document.getElementById('cvvError');
                if (value.length !== 3) {
                    cvv.classList.add('input-error');
                    errorElement.style.display = 'block';
                    return false;
                } else {
                    cvv.classList.remove('input-error');
                    errorElement.style.display = 'none';
                    return true;
                }
            }
        });
    </script>
</body>
</html>