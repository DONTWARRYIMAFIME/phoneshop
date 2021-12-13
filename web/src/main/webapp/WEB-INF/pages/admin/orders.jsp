<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tags:page pageTitle="Orders" isAdminPage="true">
    <div class="container mt-3 mb-3">
        <h2 class="mt-3">
            Orders
            <c:if test="${empty orders}">list is empty</c:if>
        </h2>
        <a href="<c:url value="/productList"/>" class="btn btn-outline-primary mt-3">
            Back to product list
        </a>

        <c:if test="${not empty orders}">
            <table class="table table-bordered mt-3">
                <thead class="thead-dark">
                <tr>
                    <th scope="col">Order number</th>
                    <th scope="col">Customer</th>
                    <th scope="col">Phone</th>
                    <th scope="col">Address</th>
                    <th scope="col">Date</th>
                    <th scope="col">Total price</th>
                    <th scope="col">Status</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>
                            <a href="<c:url value="/admin/orders/${order.id}"/>">${order.id}</a>
                        </td>
                        <td>${order.firstName} ${order.lastName}</td>
                        <td>${order.contactPhoneNo}</td>
                        <td>${order.deliveryAddress}</td>
                        <td>${order.dateTime}</td>
                        <td>
                            <fmt:formatNumber type="currency" value="${order.totalPrice}" currencySymbol="$"/>
                        </td>
                        <td>${order.status}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>

    </div>
</tags:page>