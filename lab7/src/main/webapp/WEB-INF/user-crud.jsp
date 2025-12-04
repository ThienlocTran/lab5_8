<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 11/26/2025
  Time: 2:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<html>
<head>
    <title>User CRUD</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="position-fixed top-0 end-0 p-3">
    <span class="badge bg-info fs-6">
        Visitors: <strong>${applicationScope.visitors}</strong>
    </span>
</div>
<div class="container mt-3">
<i class="alert alert-info">${message}</i>
<c:url var="url" value = "/user/crud"/>
<form method="post">

    <input name ="id" value="${user.id}"> <br>
    <input name ="password" type="password" value="${user.password}"> <br>
    <input name ="fullName" value="${user.fullName}"> <br>
    <input name="email" value="${user.email}"><br>
    <input name="role" type="radio" value="true" ${user.admin?'checked':''}>
    Admin
    <input name="role" type="radio" value="false" ${user.admin?'':'checked'}>
    User
    <hr>
    <button formaction="${url}/create">Create</button>
    <button formaction="${url}/update">Update</button>
    <button formaction="${url}/delete">Delete</button>
    <button formaction="${url}/reset">Reset</button>

</form>

<table>
    <thead>
    <tr>
        <th>Id</th>
        <th>Password</th>
        <th>Fullname</th>
        <th>Email</th>
        <th>Role</th>
        <th></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="u" items ="${users}">
        <tr>
            <td>${u.id}</td>
            <td>${u.password}</td>
            <td>${u.fullName}</td>
            <td>${u.email}</td>
            <td>${u.admin?'Admin':'User'}</td>
            <td><a href="${url}/edit/${u.id}">Edit</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
