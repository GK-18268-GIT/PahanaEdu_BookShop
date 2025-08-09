<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Payment Method</title>
    <style>
        .payment-container {
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .payment-option {
            margin: 15px 0;
        }
    </style>
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