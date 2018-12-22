<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 29.08.2018
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <title>Update user</title>
</head>
<body>
<jsp:include page="/jsp/_menu.jsp"></jsp:include>
<jsp:include page="/jsp/_current_user.jsp"></jsp:include>

<form action="${pageContext.request.contextPath}/updateuser" method="POST" class="form">
    <fieldset>
        <div class="form-group">
            Login: <input type="text" name="login" value="<c:out value="${user.login}" />"/>
        </div>
        <div class="form-group">
            Role: "<c:out value="${user.role}" />"
            <c:choose>
                <c:when test="${user.role == userRole}">
                    <c:set var="userSelected" value="selected"/>
                    <c:set var="adminSelected" value=""/>
                </c:when>
                <c:when test="${user.role == adminRole}">
                    <c:set var="userSelected" value=""/>
                    <c:set var="adminSelected" value="selected"/>
                </c:when>
            </c:choose>
            <select name="role">
                <option disabled>Change role</option>
                <option value="user" ${userSelected}>User</option>
                <option value="admin" ${adminSelected}>Admin</option>
            </select>
        </div>
        <div class="form-group">
            Password: <input type="text" name="password" value="<c:out value="${user.password}" />"/>
        </div>
        <div class="form-group">
            <input type="submit" value="Update user">
        </div>
    </fieldset>
</form>
<jsp:include page="/jsp/_message_box.jsp"></jsp:include>
</body>
</html>
