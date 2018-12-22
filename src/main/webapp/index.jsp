<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
<h3> Please, <a href="${pageContext.request.contextPath}/signin"> sign in </a> or <a href="${pageContext.request.contextPath}/signup">register</a>
    for an account and go to the <a href="${pageContext.request.contextPath}/mainmenu"> main menu. </a> </h3>
<jsp:include page="./jsp/_message_box.jsp"></jsp:include>
</body>
</html>
