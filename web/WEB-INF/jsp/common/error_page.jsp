<%@ page isErrorPage="true" %>
<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Error" scope="page"/>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>
<table id="main-container">

    <%-- HEADER --%>
    <%@ include file="/WEB-INF/jspf/header.jspf" %>
    <%-- HEADER --%>

    <br/><br/>
    <tr>
        <td class="content">
            <%-- CONTENT --%>

            <h2 class="error">
                <fmt:message key="error_page.text.following_error_occurred"/>
            </h2>

            <%-- this way we obtain an information about an exception (if it has been occurred) --%>
            <c:set var="code" value="${requestScope['javax.servlet.error.status_code']}"/>
            <c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>
            <c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>

            <%-- if we get this page using forward --%>
            <c:if test="${not empty requestScope.errorMessage}">
            <h3 style="color: red">${requestScope.errorMessage}</h3>
            </c:if>

            <%@ include file="/WEB-INF/jspf/footer.jspf" %>
</table>
</body>
</html>