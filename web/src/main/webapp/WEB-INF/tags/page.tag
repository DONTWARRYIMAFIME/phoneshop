<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="${pageTitle}">
    <tags:header/>
    <main>
        <jsp:doBody/>
    </main>
</tags:master>