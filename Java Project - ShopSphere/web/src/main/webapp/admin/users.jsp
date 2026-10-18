<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="userDAO" class="com.shopsphere.dao.UserDAO" scope="page"/>

<c:if test="${empty users}">
    <c:catch var="uEx">
        <c:set var="users" value="${userDAO.findAll()}" scope="request"/>
    </c:catch>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Accounts - ShopSphere Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row flex-nowrap">
        <jsp:include page="/admin/fragments/sidebar.jsp" />

        <div class="col py-4 px-4 px-md-5 overflow-auto" style="height: 100vh;">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-extrabold text-dark m-0">Customer &amp; User Accounts</h3>
                    <small class="text-muted">Manage authenticated members and system administrators</small>
                </div>
            </div>

            <c:if test="${not empty sessionScope.flashSuccess}">
                <div class="alert alert-success alert-dismissible fade show rounded-3 mb-3">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.flashSuccess}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="flashSuccess" scope="session"/>
            </c:if>

            <div class="card border-0 rounded-4 shadow-sm bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr class="small text-muted">
                                <th>User ID</th>
                                <th>Full Name</th>
                                <th>Email</th>
                                <th>Mobile</th>
                                <th>Role</th>
                                <th>Joined</th>
                                <th>Status</th>
                                <th class="text-end">Account Control</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="u" items="${users}">
                                <tr>
                                    <td class="fw-bold">#${u.userId}</td>
                                    <td><strong class="text-dark">${u.name}</strong></td>
                                    <td><code>${u.email}</code></td>
                                    <td class="text-muted small">${not empty u.mobile ? u.mobile : 'N/A'}</td>
                                    <td>
                                        <span class="badge ${u.admin ? 'bg-warning text-dark' : 'bg-light text-dark border'}">
                                            ${u.role}
                                        </span>
                                    </td>
                                    <td class="small text-muted"><fmt:formatDate value="${u.createdAt}" pattern="MMM dd, yyyy" /></td>
                                    <td>
                                        <span class="badge ${u.status == 'ACTIVE' ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'}">
                                            ${u.status}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <c:if test="${!u.admin}">
                                            <form action="${pageContext.request.contextPath}/admin/users" method="post" class="d-inline">
                                                <input type="hidden" name="userId" value="${u.userId}">
                                                <input type="hidden" name="status" value="${u.status == 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'}">
                                                <button type="submit" class="btn btn-sm ${u.status == 'ACTIVE' ? 'btn-outline-danger' : 'btn-outline-success'} rounded-pill px-3">
                                                    ${u.status == 'ACTIVE' ? 'Deactivate' : 'Activate'}
                                                </button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
