<%--
  Created by IntelliJ IDEA.
  User: atonl
  Date: 29/11/2025
  Time: 13:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Web Student Tracker</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>

<div id="wrapper">
    <div id="header">
        <h2>ESILV Engineer School</h2>
    </div>
</div>

<div id="container">
    <div style="padding-bottom: 15px; border-bottom: 1px solid #ccc; margin-bottom: 15px;">
        <c:if test="${not empty sessionScope.user}">
            Bienvenue, ${sessionScope.user.username} (Rôle : ${sessionScope.role}) |

            <a href="LogoutServlet">Déconnexion</a>
        </c:if>
    </div>

    <div id="content">

        <c:if test="${sessionScope.role == 'instructor'}">
            <form action="AddStudentServlet" method="get">
                <input type="submit" value="Add Student"/>
            </form>
        </c:if>

        <table>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Action</th>
            </tr>

            <c:forEach var="tempStudent" items="${STUDENT_LIST}" >

                <c:url var="EditLink" value="EditStudentServlet">
                    <c:param name="studentId" value="${tempStudent.id}"/>
                </c:url>
                <c:url var="DeleteLink" value="StudentControllerServlet">
                    <c:param name="command" value="DELETE"/>
                    <c:param name="studentId" value="${tempStudent.id}"/>
                </c:url>

                <tr>
                    <td> ${tempStudent.firstName}</td>
                    <td> ${tempStudent.lastName}</td>
                    <td> ${tempStudent.email}</td>

                    <c:if test="${sessionScope.role == 'instructor'}">
                        <td>
                            <a href="${EditLink}">Edit</a>
                            &nbsp;|&nbsp;
                            <a href="${DeleteLink}"
                               onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?')">Delete</a>
                        </td>
                    </c:if>
                    <c:if test="${sessionScope.role != 'instructor'}">
                        <td></td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
