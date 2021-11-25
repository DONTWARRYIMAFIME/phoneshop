<%@ tag trimDirectiveWhitespaces="true" pageEncoding="utf-8" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:header/>
<tags:master pageTitle="${pageTitle}">
    <jsp:doBody/>
</tags:master>
<tags:footer/>