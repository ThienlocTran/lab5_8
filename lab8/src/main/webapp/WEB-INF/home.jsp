<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Video Management</a>
            <div class="d-flex align-items-center">
                <span class="badge bg-info me-3">
                    Visitors: ${applicationScope.visitors}
                </span>
                <span class="text-white me-3">
                    Welcome, <strong>${sessionScope.user.fullName}</strong> 
                    <c:if test="${sessionScope.user.admin}">(Admin)</c:if>
                    <c:if test="${!sessionScope.user.admin}">(User)</c:if>
                </span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0">User Information</h5>
                    </div>
                    <div class="card-body">
                        <table class="table table-bordered">
                            <tr>
                                <th style="width: 30%;">ID</th>
                                <td>${sessionScope.user.id}</td>
                            </tr>
                            <tr>
                                <th>Full Name</th>
                                <td>${sessionScope.user.fullName}</td>
                            </tr>
                            <tr>
                                <th>Email</th>
                                <td>${sessionScope.user.email}</td>
                            </tr>
                            <tr>
                                <th>Role</th>
                                <td>
                                    <c:if test="${sessionScope.user.admin}">
                                        <span class="badge bg-danger">Admin</span>
                                    </c:if>
                                    <c:if test="${!sessionScope.user.admin}">
                                        <span class="badge bg-info">User</span>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>

                <div class="mt-4">
                    <c:if test="${sessionScope.user.admin}">
                        <a href="${pageContext.request.contextPath}/user/crud" class="btn btn-primary me-2">Manage Users</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/video/crud" class="btn btn-info me-2">Manage Videos</a>
                    <a href="${pageContext.request.contextPath}/favorite/crud" class="btn btn-warning me-2">Manage Favorites</a>
                    <a href="${pageContext.request.contextPath}/share/crud" class="btn btn-success">Manage Shares</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
