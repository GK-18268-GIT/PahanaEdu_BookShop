<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pahana Edu Bookshop - Admin Dashboard</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@400;600;700&family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: #e3f2fd;
            margin: 0;
            padding: 0;
        }

        .dashboard-container {
            max-width: 1000px;
            margin: 40px auto;
            padding: 30px;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            color: #222;
            font-weight: 600;
            margin-bottom: 20px;
        }

        p {
            text-align: center;
            font-size: 16px;
            margin-bottom: 30px;
            color: #444;
        }

        .navigate-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 25px;
        }

        .button {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px;
            background-color: #0088cc;
            color: #fff;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 500;
            box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }

        .button:hover {
            background-color: #006699;
            transform: translateY(-3px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
        }

        .button img {
            width: 50px;
            height: 50px;
            margin-bottom: 12px;
            object-fit: contain;
        }
    </style>
</head>

<body>
    <div class="dashboard-container">
        <h2>Admin Dashboard</h2>
        <p>Welcome <%= session.getAttribute("user") %>!</p>

        <div class="navigate-cards">
            <a href="AdminServlet?action=register" class="button">
                <img src="${pageContext.request.contextPath}/assets/add_customer.jpg" alt="Add Customer">
                Add new customer
            </a>
            <a href="${pageContext.request.contextPath}/ManageItemServlet?action=itemList" class="button">
                <img src="${pageContext.request.contextPath}/assets/manage_items.jpg" alt="Manage Items">
                Manage Items
            </a>
            <a href="${pageContext.request.contextPath}/CustomerServlet?action=list" class="button">
                <img src="${pageContext.request.contextPath}/assets/view_customer_data.jpg" alt="View Account">
                Display account details
            </a>
            <a href="#" class="button">
                <img src="${pageContext.request.contextPath}/assets/edit_customer_data.jpg" alt="Edit Customer">
                Edit customer data
            </a>
        </div>
    </div>
</body>
</html>
