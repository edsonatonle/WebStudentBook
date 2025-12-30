<%--
  Created by IntelliJ IDEA.
  User: atonl
  Date: 01/12/2025
  Time: 17:43
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="css/add-student-style.css">
    <link type="text/css" rel="stylesheet" href="css/style.css">
    <title>Edit a student</title>
</head>
<body>
<div id="wrapper">
    <div id="header">
        <h2>ESILV Engineer School</h2>
    </div>
</div>
<div id="container">
    <h3> Edit a Student</h3>

    <form action="EditStudentServlet" method="post">
        <input type="hidden" name="id" value="${Student.id}"/>
        <table>
            <tbody>
            <tr>
                <td><label>FirstName: </label> </td>
                <td><input type="text" name="firstName" value="${Student.firstName}"/></td>
            </tr>
            <tr>
                <td><label>LastName: </label> </td>
                <td><input type="text" name="lastName" value="${Student.lastName}"/></td>
            </tr>
            <tr>
                <td><label>Email: </label> </td>
                <td><input type="text" name="email" value="${Student.email}"/></td>
            </tr>
            <tr>
                <td><label></label> </td>
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
