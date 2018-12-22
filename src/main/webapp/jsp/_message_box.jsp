<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:choose>
    <c:when test="${not empty infoMessage}">
        <div class="box_info">
            <c:out value="${infoMessage}"/>
            <c:set var="message" scope="session" value="${infoMessage}"/>
        </div>
    </c:when>

    <c:when test="${not empty successMessage}">
        <div class="box_success">
            <c:out value="${successMessage}"/>
        </div>
    </c:when>

    <c:when test="${not empty warningMessage}">
        <div class="box_warning">
            <c:out value="${warningMessage}"/>
        </div>
    </c:when>

    <c:when test="${not empty errorMessage}">
        <div class="box_error">
            <c:out value="${errorMessage}"/>
        </div>
    </c:when>
</c:choose>