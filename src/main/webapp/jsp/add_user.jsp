<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 18.08.2018
  Time: 16:07
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <title>Add user</title>
</head>

<body>
<jsp:include page="/jsp/_menu.jsp"></jsp:include>
<jsp:include page="/jsp/_current_user.jsp"></jsp:include>
<div>
    You can add users
</div>
<form action="${pageContext.request.contextPath}/adduser" method="POST" class="form">
    <fieldset>
        <div class="form-group">
            Login: <input type="text" name="login"/>
        </div>
        <div class="form-group">
            Password: <input type="password" name="password"/>
        </div>
        <div class="form-group">
            Role:
            <select name="role">
                <option value="user">User</option>
                <option value="admin">Admin</option>
            </select>
        </div>
        <div class="form-group">
            <input type="submit" value="Add user">
        </div>
    </fieldset>
</form>
<jsp:include page="/jsp/_message_box.jsp"></jsp:include>
</body>
</html>
