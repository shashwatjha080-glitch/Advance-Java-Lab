<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Extract saved email cookie if present --%>
<c:set var="savedEmailCookie" value="" />
<%
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if ("savedEmail".equals(c.getName())) {
                request.setAttribute("savedEmailCookie", c.getValue());
                break;
            }
        }
    }
%>

<c:set var="pageTitle" value="Sign In - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card border-0 shadow-lg rounded-4 overflow-hidden">
                    <div class="bg-primary text-white p-4 text-center">
                        <i class="bi bi-person-circle fs-1"></i>
                        <h4 class="fw-bold mt-2 mb-1">Customer Sign In</h4>
                        <p class="small text-white-50 m-0">Access your ShopSphere orders, cart, and wishlist</p>
                    </div>
                    <div class="card-body p-4 p-md-5">
                        <form action="${pageContext.request.contextPath}/login" method="post">
                            <div class="mb-3">
                                <label for="emailInput" class="form-label small fw-semibold">Email Address</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
                                    <input type="email" id="emailInput" name="email" class="form-control bg-light border-start-0"
                                           placeholder="e.g. john.doe@example.com"
                                           value="${not empty enteredEmail ? enteredEmail : savedEmailCookie}" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <div class="d-flex justify-content-between">
                                    <label for="passwordInput" class="form-label small fw-semibold">Password</label>
                                    <a href="#" class="text-primary text-decoration-none small">Forgot?</a>
                                </div>
                                <div class="input-group">
                                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock text-muted"></i></span>
                                    <input type="password" id="passwordInput" name="password" class="form-control bg-light border-start-0"
                                           placeholder="Enter password" required>
                                </div>
                            </div>

                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <div class="form-check">
                                    <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe"
                                           ${not empty savedEmailCookie ? 'checked' : ''}>
                                    <label class="form-check-label small text-muted" for="rememberMe">Remember my email</label>
                                </div>
                            </div>

                            <div class="d-grid mb-3">
                                <button type="submit" class="btn btn-primary rounded-pill py-2 fw-bold">
                                    Sign In <i class="bi bi-arrow-right ms-1"></i>
                                </button>
                            </div>
                        </form>

                        <!-- Demo credentials helper box -->
                        <div class="p-3 bg-light rounded-3 small border border-secondary border-opacity-10 mb-4">
                            <div class="fw-bold text-dark mb-1"><i class="bi bi-info-circle-fill text-primary me-1"></i> Demo Customer Credentials:</div>
                            <div class="text-muted">Email: <code>john.doe@example.com</code></div>
                            <div class="text-muted">Password: <code>customer123</code></div>
                        </div>

                        <div class="text-center small text-muted">
                            Don't have an account? 
                            <a href="${pageContext.request.contextPath}/register.jsp" class="text-primary fw-bold text-decoration-none">Create Account</a>
                        </div>

                        <hr class="my-4">

                        <div class="text-center">
                            <a href="${pageContext.request.contextPath}/admin-login.jsp" class="btn btn-outline-secondary btn-sm rounded-pill px-3">
                                <i class="bi bi-shield-lock me-1"></i> Switch to Administrator Portal
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
