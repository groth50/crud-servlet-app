<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 04.07.2018
  Time: 20:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <title>SIGN IN</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/signin" method="POST" class="form">
    <fieldset>
        <div class="form-group">
            Login: <input type="text" name="login"/>
        </div>
        <div class="form-group">
            Password: <input type="password" name="password"/>
        </div>
        <div class="form-group">
            <input type="submit" value="Sign in">
        </div>
    </fieldset>
</form>

<div>
    or <a href="${pageContext.request.contextPath}/signup">register</a> for an account
</div>

<jsp:include page="/jsp/_message_box.jsp"></jsp:include>

</body>
</html>
