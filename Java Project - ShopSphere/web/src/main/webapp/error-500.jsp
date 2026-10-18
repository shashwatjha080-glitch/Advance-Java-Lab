<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="500 - Server Error - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="py-5 text-center">
    <div class="container">
        <div class="display-1 text-danger fw-extrabold mb-2">500</div>
        <h2 class="fw-bold text-dark mb-3">Internal Server Error</h2>
        <p class="text-muted mb-4">An unexpected condition was encountered on the server. Our system administrators have been notified.</p>
        <c:if test="${not empty pageContext.exception}">
            <div class="card bg-light p-3 text-start small font-monospace mx-auto mb-4" style="max-width: 600px;">
                <strong>Exception:</strong> ${pageContext.exception.message}
            </div>
        </c:if>
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary rounded-pill px-4">
            <i class="bi bi-house me-1"></i> Return to Homepage
        </a>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
