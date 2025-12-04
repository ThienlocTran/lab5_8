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
    <title>Video CRUD</title>
</head>
<body>
<i>${message}</i>
<c:url var="url" value = "/video/crud"/>
<form method="post">

    <input name ="id" value="${video.id}" placeholder="Video ID"> <br>
    <input name ="title" value="${video.title}" placeholder="Title"> <br>
    <input name ="poster" value="${video.poster}" placeholder="Poster URL"> <br>
    <input name ="views" type="number" value="${video.views}" placeholder="Views"> <br>
    <input name ="description" value="${video.description}" placeholder="Description"> <br>
    <input name="active" type="radio" value="true" ${video.active?'checked':''}>
    Active
    <input name="active" type="radio" value="false" ${video.active?'':'checked'}>
    Inactive
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
        <th>Title</th>
        <th>Poster</th>
        <th>Views</th>
        <th>Description</th>
        <th>Active</th>
        <th></th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="v" items ="${videos}">
        <tr>
            <td>${v.id}</td>
            <td>${v.title}</td>
            <td>${v.poster}</td>
            <td>${v.views}</td>
            <td>${v.description}</td>
            <td>${v.active?'Yes':'No'}</td>
            <td><a href="${url}/edit/${v.id}">Edit</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
