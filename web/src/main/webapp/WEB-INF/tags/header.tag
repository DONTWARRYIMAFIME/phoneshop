<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header>
    <div class="container">
        <div class="d-flex justify-content-between align-items-end">
            <h1>
                <a href="<c:url value="/productList"/>" id="logo">Phonify</a>
            </h1>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <div class="d-flex flex-end mb-3">
                    <div>
                        <a href="<c:url value="/admin/orders"/>">
                            <sec:authentication property="name"/>
                        </a>
                    </div>
                    <a href="<c:url value="/logout"/>" class="btn btn-outline-dark ml-3 pt-0 pb-1">Logout</a>
                </div>
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
                <div class="d-flex align-items-end">
                    <a href="<c:url value="/cart"/>" class="btn btn-outline-primary">
                        My cart:
                        <span id="cart-items-quantity"></span>
                        <span id="total-price"></span>
                    </a>
                    <a href="<c:url value="/admin/orders"/>" class="btn btn-outline-dark ml-3">Login</a>
                </div>
            </sec:authorize>
        </div>
        <hr>
    </div>
</header>