<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 01.08.2018
  Time: 18:32
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
    <title>Main menu</title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/jsp/_menu.jsp"></jsp:include>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/jsp/_current_user.jsp"></jsp:include>
<div>
    <h3>All user profile</h3>
</div>
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
                    <td><c:out value="${user.id}" /></td>
                    <td><c:out value="${user.login}" /></td>
                    <td><c:out value="${user.role}" /></td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
</c:choose>


<jsp:include page="${pageContext.request.contextPath}/WEB-INF/jsp/_message_box.jsp"></jsp:include>

</body>
</html>
