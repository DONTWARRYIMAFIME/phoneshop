<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:page pageTitle="Quick Order">
    <div class="container mt-3 mb-3">
        <a href="<c:url value="/productList"/>" class="btn btn-outline-primary mb-3">
            Back to product list
        </a>

        <c:forEach var="message" items="${success}">
            <div class="alert alert-success" role="alert">
                 ${message}
            </div>
        </c:forEach>

        <form:form method="post" modelAttribute="quickCartItemListDto">
            <table class="table table-bordered mb-3">
                <thead class="thead-dark">
                    <tr>
                        <th id="code-col" scope="col" class="col-1">Phone Model</th>
                        <th id="quantity-col" scope="col" class="col-2">Quantity</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cartItem" items="${quickCartItemListDto.cartItems}" varStatus="status">
                        <c:set var="i" value="${status.index}"/>
                    <tr>
                        <td>
                            <form:input path="cartItems[${i}].model" cssClass="form-control"/>
                            <form:errors path="cartItems[${i}].model" cssClass="error"/>
                        </td>

                        <td>
                            <form:input path="cartItems[${i}].quantity" cssClass="form-control"/>
                            <form:errors path="cartItems[${i}].quantity" cssClass="error"/>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="d-flex justify-content-end p-1">
                <div class="justify-content-end">
                    <button type="submit" class="btn btn-outline-primary mr-2 pr-4 pl-4">
                        Add to cart
                    </button>
                </div>
            </div>
        </form:form>
    </div>
</tags:page>
