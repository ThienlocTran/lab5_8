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
    <title>Share CRUD</title>
</head>
<body>
<i>${message}</i>
<c:url var="url" value = "/share/crud"/>
<form method="post">

    <input name ="id" type="number" value="${share.id}" placeholder="Share ID"> <br>
    <input name ="emails" value="${share.emails}" placeholder="Emails"> <br>
    <input name ="shareDate" type="datetime-local" value="${share.shareDate}" placeholder="Share Date"> <br>
    <input name ="userId" value="${share.user.id}" placeholder="User ID"> <br>
    <input name ="videoId" value="${share.video.id}" placeholder="Video ID"> <br>
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
        <th>Emails</th>
        <th>Share Date</th>
        <th>User ID</th>
        <th>Video ID</th>
        <th></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="s" items ="${shares}">
        <tr>
            <td>${s.id}</td>
            <td>${s.emails}</td>
            <td><fmt:formatDate value="${s.shareDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${s.user.id}</td>
            <td>${s.video.id}</td>
            <td><a href="${url}/edit/${s.id}">Edit</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
