<%--
  Created by IntelliJ IDEA.
  User: atonl
  Date: 01/12/2025
  Time: 18:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h2>Se connecter à l'application</h2>

<c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
</c:if>

<form action="LoginServlet" method="post">
    <table>
        <tr>
            <td><label>Username:</label></td>
            <td><input type="text" name="username"
                       value="${lastUsername}" required/></td>
        </tr>
        <tr>
            <td><label>Password:</label></td>
            <td><input type="password" name="password" required/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="Login"/></td>
        </tr>
    </table>
</form>
</body>
</html>
