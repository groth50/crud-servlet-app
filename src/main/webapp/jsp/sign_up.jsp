<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 30.07.2018
  Time: 10:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <title>SIGN UP</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/signup" method="POST" class="form">
    <fieldset>
        <div class="form-group">
            Login: <input type="text" name="login"/>
        </div>
        <div class="form-group">
            Password: <input type="password" name="password"/>
        </div>
        <div class="form-group">
            <input type="submit" value="Sign up">
        </div>
    </fieldset>
</form>
<div>
    or <a href="${pageContext.request.contextPath}/signin"> sign in </a>
</div>

<jsp:include page="/jsp/_message_box.jsp"></jsp:include>

</body>
</html>
