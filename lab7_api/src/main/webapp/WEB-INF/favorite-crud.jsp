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
    <title>Favorite CRUD</title>
</head>
<body>
<i>${message}</i>
<c:url var="url" value = "/favorite/crud"/>
<form method="post">

    <input name ="id" type="number" value="${favorite.id}" placeholder="Favorite ID"> <br>
    <input name ="likeDate" type="datetime-local" value="${favorite.likeDate}" placeholder="Like Date"> <br>
    <input name ="videoId" value="${favorite.video.id}" placeholder="Video ID"> <br>
    <input name ="userId" value="${favorite.user.id}" placeholder="User ID"> <br>
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
        <th>Like Date</th>
        <th>Video ID</th>
        <th>User ID</th>
        <th></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="f" items ="${favorites}">
        <tr>
            <td>${f.id}</td>
            <td><fmt:formatDate value="${f.likeDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${f.video.id}</td>
            <td>${f.user.id}</td>
            <td><a href="${url}/edit/${f.id}">Edit</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
