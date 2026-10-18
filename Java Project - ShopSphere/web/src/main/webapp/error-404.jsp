<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="404 - Page Not Found - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="py-5 text-center">
    <div class="container">
        <div class="display-1 text-primary fw-extrabold mb-2">404</div>
        <h2 class="fw-bold text-dark mb-3">Page Not Found</h2>
        <p class="text-muted mb-4">The page or resource you requested could not be located on the ShopSphere server.</p>
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary rounded-pill px-4">
            <i class="bi bi-house me-1"></i> Return to Homepage
        </a>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
