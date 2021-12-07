<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="isAdminPage" required="false" %>

<header>
    <div class="container">
        <div class="d-flex justify-content-between align-items-end">
            <h1>
                <a href="<c:url value="/productList"/>" id="logo">Phonify</a>
            </h1>
            <c:choose>
                <c:when test="${isAdminPage eq true}">
                    <div class="d-flex flex-end mb-3">
                        <div>admin</div>
                        <a href="<c:url value="/productList"/>" class="ml-3">Logout</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="d-flex flex-column align-items-end">
                        <a href="<c:url value="/admin/orders"/>" class="mb-3">Login</a>
                        <a href="<c:url value="/cart"/>" class="btn btn-outline-primary">
                            My cart:
                            <span id="cart-items-quantity"></span>
                            <span id="total-price"></span>
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <hr>
    </div>
</header>