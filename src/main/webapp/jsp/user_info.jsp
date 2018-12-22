<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 05.09.2018
  Time: 19:38
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <title>
        User info
    </title>
</head>
<body>
<jsp:include page="/jsp/_menu.jsp"></jsp:include>
<div>
    <h3>Your profile</h3>
</div>
<form action="${pageContext.request.contextPath}/logout" method="POST" class="form">
    <fieldset>
        <div class="form-group">
            ID: <input type="text" name="id" value="<c:out value="${currentUser.id}" />"/>
        </div>
        <div class="form-group">
            Login: <input type="text" name="login" value="<c:out value="${currentUser.login}" />"/>
        </div>
        <div class="form-group">
            Role: <input type="text" name="role" value="<c:out value="${currentUser.role}" />"/>
        </div>
        <div class="form-group">
            Password: <input type="text" name="password" value="<c:out value="${currentUser.password}" />"/>
        </div>
        <div class="form-group">
            <input type="submit" value="Log out">
        </div>
    </fieldset>
</form>
<jsp:include page="/jsp/_message_box.jsp"></jsp:include>
</body>
</html>
