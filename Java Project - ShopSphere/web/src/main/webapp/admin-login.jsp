<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administrator Sign In - ShopSphere</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-dark d-flex align-items-center justify-content-center min-vh-100 py-5">

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card border-0 shadow-lg rounded-4 overflow-hidden bg-white">
                <div class="p-4 text-center bg-dark text-white border-bottom border-secondary border-opacity-25">
                    <div class="display-5 text-warning mb-2"><i class="bi bi-shield-lock-fill"></i></div>
                    <h4 class="fw-extrabold m-0">ShopSphere Control Portal</h4>
                    <small class="text-white-50">Enterprise Administration &amp; Inventory Management</small>
                </div>
                <div class="card-body p-4 p-md-5">

                    <c:if test="${not empty sessionScope.adminAuthError}">
                        <div class="alert alert-danger small rounded-3 mb-3">
                            ${sessionScope.adminAuthError}
                        </div>
                        <c:remove var="adminAuthError" scope="session"/>
                    </c:if>

                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger small rounded-3 mb-3">
                            ${errorMessage}
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/admin-login" method="post">
                        <div class="mb-3">
                            <label for="adminEmail" class="form-label small fw-semibold">Admin Account</label>
                            <input type="email" id="adminEmail" name="email" class="form-control rounded-3"
                                   placeholder="admin@shopsphere.com" value="${not empty enteredEmail ? enteredEmail : 'admin@shopsphere.com'}" required>
                        </div>
                        <div class="mb-4">
                            <label for="adminPassword" class="form-label small fw-semibold">Master Password</label>
                            <input type="password" id="adminPassword" name="password" class="form-control rounded-3"
                                   placeholder="••••••••" value="admin123" required>
                        </div>
                        <div class="d-grid mb-3">
                            <button type="submit" class="btn btn-dark rounded-pill py-2 fw-bold text-warning">
                                <i class="bi bi-key-fill me-1"></i> Authenticate &amp; Launch Dashboard
                            </button>
                        </div>
                    </form>

                    <div class="p-3 bg-light rounded-3 small border text-muted">
                        <strong class="text-dark"><i class="bi bi-info-circle me-1"></i> Pre-seeded Credentials:</strong><br>
                        Email: <code>admin@shopsphere.com</code><br>
                        Password: <code>admin123</code>
                    </div>

                    <div class="text-center mt-4">
                        <a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none small text-muted">
                            <i class="bi bi-arrow-left me-1"></i> Return to Public Storefront
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
