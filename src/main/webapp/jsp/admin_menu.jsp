<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 18.08.2018
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    <title>Admin menu</title>
</head>
<body>
<jsp:include page="/jsp/_menu.jsp"></jsp:include>
<jsp:include page="/jsp/_current_user.jsp"></jsp:include>
<div>
    <h3>Admin menu</h3>
</div>

<form action="/deleteuser" method="post" id="userForm" role="form" >
    <input type="hidden" id="userId" name="userId">
    <input type="hidden" id="userLogin" name="userLogin">
    <c:choose>
        <c:when test="${not empty users}">
            <table  class="table table-striped">
                <thead>
                <tr>
                    <th>User Id</th>
                    <th>Login</th>
                    <th>Role</th>
                    <td></td>
                </tr>
                </thead>
                <c:forEach var="user" items="${users}">
                    <c:set var="classSucess" value=""/>
                    <c:if test ="${userId == user.id}">
                        <c:set var="classSucess" value="info"/>
                    </c:if>
                    <tr class="${classSucess}">
                        <td><a href="/updateuser?id=${user.id}"><c:out value="${user.id}" /></a></td>
                        <td><c:out value="${user.login}" /></td>
                        <td><c:out value="${user.role}" /></td>

                        <td>
                            <a href="#" id="remove"
                               onclick="
                               document.getElementById('userId').value = '${user.id}';
                               document.getElementById('userLogin').value = '${user.login}';
                               document.getElementById('userForm').submit();">
                                <span class="glyphicon glyphicon-trash"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
    </c:choose>
</form>

<form action="${pageContext.request.contextPath}/adduser" method="GET" class="form">
    <fieldset>
        <div class="form-group">
            <input type="submit" value="Add user">
        </div>
    </fieldset>
</form>

<jsp:include page="/jsp/_message_box.jsp"></jsp:include>
</body>
</html>
