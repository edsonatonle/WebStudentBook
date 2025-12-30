<%--
  Created by IntelliJ IDEA.
  User: atonl
  Date: 01/12/2025
  Time: 10:24
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="css/add-student-style.css">
    <link type="text/css" rel="stylesheet" href="css/style.css">
    <title>Add a Student</title>
</head>
<body>
<div id="wrapper">
    <div id="header">
        <h2>ESILV Engineer School</h2>
    </div>
</div>
<div id="container">
    <h3> Add New Student</h3>

    <form action="StudentControllerServlet" method="post">
        <input type="hidden" name="command" value="ADD" />

        <table>
            <tbody>
            <tr>
                <td><label>FirstName: </label></td>
                <td><input type="text" name="firstName"/></td>
            </tr>
            <tr>
                <td><label>LastName: </label></td>
                <td><input type="text" name="lastName"/></td>
            </tr>
            <tr>
                <td><label>Email: </label></td>
                <td><input type="text" name="email"/></td>
            </tr>
            <tr>
                <td><label></label></td>
                <td><input type="submit" value="Save"/></td>
            </tr>
            </tbody>
        </table>
    </form>
    <div style="clear:both;"></div>

    <a href="StudentControllerServlet">Back to List</a>
</div>
</body>
</html>
