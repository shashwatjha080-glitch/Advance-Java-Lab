<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="container my-3">
    <!-- Flash Success Alert -->
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alert alert-success alert-dismissible fade show rounded-3 shadow-sm d-flex align-items-center" role="alert">
            <i class="bi bi-check-circle-fill fs-5 me-2"></i>
            <div>${sessionScope.flashSuccess}</div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashSuccess" scope="session" />
    </c:if>

    <!-- Flash Error Alert -->
    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm d-flex align-items-center" role="alert">
            <i class="bi bi-exclamation-triangle-fill fs-5 me-2"></i>
            <div>${sessionScope.flashError}</div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashError" scope="session" />
    </c:if>

    <!-- Request Scope Error Message -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show rounded-3 shadow-sm d-flex align-items-center" role="alert">
            <i class="bi bi-x-circle-fill fs-5 me-2"></i>
            <div>${errorMessage}</div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <!-- Authentication Redirect Error Message -->
    <c:if test="${not empty sessionScope.authError}">
        <div class="alert alert-warning alert-dismissible fade show rounded-3 shadow-sm d-flex align-items-center" role="alert">
            <i class="bi bi-shield-lock-fill fs-5 me-2"></i>
            <div>${sessionScope.authError}</div>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="authError" scope="session" />
    </c:if>
</div>
