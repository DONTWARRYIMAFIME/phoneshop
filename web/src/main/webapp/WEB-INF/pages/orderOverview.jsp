<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tags:page pageTitle="Order overview">
    <div class="container mt-3 mb-3">
        <h3 class="mt-3">Thank you for your order</h3>
        <h4 class="mt-3">Order number: ${order.id}</h4>
        <table class="table table-bordered mt-3">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Brand</th>
                <th scope="col">Model</th>
                <th scope="col">Color</th>
                <th scope="col">Display size</th>
                <th scope="col">Quantity</th>
                <th scope="col">Price</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="orderItem" items="${order.orderItems}">
                <tr>
                    <td>${orderItem.phone.brand}</td>
                    <td>${orderItem.phone.model}</td>
                    <td>
                        <c:if test="${empty orderItem.phone.colors}">&mdash;</c:if>

                        <c:forEach var="color" items="${orderItem.phone.colors}" varStatus="index">
                            ${color.code}<c:if test="${not index.last}">,</c:if>
                        </c:forEach>
                    </td>
                    <td>${orderItem.phone.displaySizeInches}"</td>
                    <td>${orderItem.quantity}</td>
                    <td>
                        <fmt:formatNumber type="currency" value="${orderItem.phone.price}" currencySymbol="$"/>
                    </td>
                </tr>
            </c:forEach>

            <tr>
                <td rowspan="3" colspan="4" id="empty-cell"></td>
                <td>Subtotal</td>
                <td>
                    <fmt:formatNumber type="currency" value="${order.subtotal}" currencySymbol="$"/>
                </td>
            </tr>
            <tr>
                <td>Delivery</td>
                <td>
                    <fmt:formatNumber type="currency" value="${order.deliveryPrice}" currencySymbol="$"/>
                </td>
            </tr>
            <tr id="total-price-row">
                <td>TOTAL</td>
                <td>
                    <fmt:formatNumber type="currency" value="${order.totalPrice}" currencySymbol="$"/>
                </td>
            </tr>
            </tbody>
        </table>

        <div>
            <div class="row mb-3">
                <div class="col-2">First name</div>
                <div class="col">${order.firstName}</div>
            </div>
            <div class="row mb-3">
                <div class="col-2">Last name</div>
                <div class="col">${order.lastName}</div>
            </div>
            <div class="row mb-3">
                <div class="col-2">Address</div>
                <div class="col">${order.deliveryAddress}</div>
            </div>
            <div class="row mb-3">
                <div class="col-2">Phone</div>
                <div class="col">${order.contactPhoneNo}</div>
            </div>
            <div class="clip row mb-3">
                <div class="col-6">${order.additionalInfo}</div>
            </div>
        </div>

        <a href="<c:url value="/products"/>" class="btn btn-outline-primary">Back to shopping</a>
    </div>
</tags:page>