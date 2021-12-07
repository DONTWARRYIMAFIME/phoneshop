<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="isAdminPage" required="false" %>

<tags:master pageTitle="${pageTitle}">
    <tags:header isAdminPage="${isAdminPage}"/>
    <main>
        <jsp:doBody/>
    </main>
</tags:master>