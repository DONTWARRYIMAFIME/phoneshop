<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
    <div class="container">
        <div class="d-flex justify-content-between align-items-end">
            <h1>
                <a href="<c:url value="/productList"/>" id="logo">Phonify</a>
            </h1>
            <div class="d-flex flex-column align-items-end">
                <a href="#" class="mb-3">Login</a>
                <a href="<c:url value="/cart"/>" class="btn btn-outline-primary">
                    My cart:
                    <span id="cart-items-quantity"></span>
                    <span id="total-price"></span>
                </a>
            </div>
        </div>
        <hr>
    </div>
</header>