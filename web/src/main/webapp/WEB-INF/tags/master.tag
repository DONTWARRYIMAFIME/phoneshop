<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
    <title>${pageTitle}</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css"/>">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.13.0/css/all.css">
</head>
<body class="product-list mt-2">
<header>
    <div class="container">
        <div class="d-flex justify-content-between align-items-end">
            <div>
                <h1>Phonify</h1>
            </div>
            <div class="d-flex flex-column align-items-end">
                <a href="#" class="mb-3">Login</a>
                <a href="#" class="btn btn-outline-primary">
                    My cart:
                    <span id="cart-items-quantity"></span>
                    <span id="total-price"></span>
                </a>
            </div>
        </div>
        <hr>
    </div>
</header>
<main>
    <jsp:doBody/>
</main>
<footer class="footer mt-3">
    <div class="d-flex justify-content-center align-center">
        <div class="d-flex flex-column align-center p-3">
            (c) Expert Soft 2021
        </div>
    </div>
</footer>
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="<c:url value="/resources/js/addToCart.js"/>"></script>
</body>
</html>