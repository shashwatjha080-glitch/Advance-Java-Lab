<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Register Account - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-7 col-lg-6">
                <div class="card border-0 shadow-lg rounded-4 overflow-hidden">
                    <div class="bg-primary text-white p-4 text-center">
                        <i class="bi bi-person-plus-fill fs-1"></i>
                        <h4 class="fw-bold mt-2 mb-1">Create an Account</h4>
                        <p class="small text-white-50 m-0">Join ShopSphere to enjoy personalized recommendations and instant checkout</p>
                    </div>
                    <div class="card-body p-4 p-md-5">
                        <form action="${pageContext.request.contextPath}/register" method="post">
                            <div class="mb-3">
                                <label for="nameInput" class="form-label small fw-semibold">Full Name</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-person text-muted"></i></span>
                                    <input type="text" id="nameInput" name="name" class="form-control bg-light border-start-0"
                                           placeholder="e.g. John Doe" value="${name}" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="emailInput" class="form-label small fw-semibold">Email Address</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope text-muted"></i></span>
                                    <input type="email" id="emailInput" name="email" class="form-control bg-light border-start-0"
                                           placeholder="e.g. john.doe@example.com" value="${email}" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="mobileInput" class="form-label small fw-semibold">Mobile Number</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-light border-end-0"><i class="bi bi-phone text-muted"></i></span>
                                    <input type="tel" id="mobileInput" name="mobile" class="form-control bg-light border-start-0"
                                           placeholder="e.g. 9876543210" value="${mobile}">
                                </div>
                            </div>

                            <div class="row g-3 mb-4">
                                <div class="col-md-6">
                                    <label for="passwordInput" class="form-label small fw-semibold">Password</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock text-muted"></i></span>
                                        <input type="password" id="passwordInput" name="password" class="form-control bg-light border-start-0"
                                               placeholder="Min. 6 chars" minlength="6" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label for="confirmPasswordInput" class="form-label small fw-semibold">Confirm Password</label>
                                    <div class="input-group">
                                        <span class="input-group-text bg-light border-end-0"><i class="bi bi-shield-check text-muted"></i></span>
                                        <input type="password" id="confirmPasswordInput" name="confirmPassword" class="form-control bg-light border-start-0"
                                               placeholder="Re-enter password" minlength="6" required>
                                    </div>
                                </div>
                            </div>

                            <div class="d-grid mb-3">
                                <button type="submit" class="btn btn-primary rounded-pill py-2 fw-bold">
                                    Register Account <i class="bi bi-arrow-right ms-1"></i>
                                </button>
                            </div>
                        </form>

                        <div class="text-center small text-muted">
                            Already have an account? 
                            <a href="${pageContext.request.contextPath}/login.jsp" class="text-primary fw-bold text-decoration-none">Sign In</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
