<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:useBean id="userDAO" class="com.shopsphere.dao.UserDAO" scope="page"/>
<jsp:useBean id="addressDAO" class="com.shopsphere.dao.AddressDAO" scope="page"/>

<c:if test="${empty userProfile && not empty sessionScope.user}">
    <c:catch var="profEx">
        <c:set var="userProfile" value="${userDAO.findById(sessionScope.user.userId)}" scope="request"/>
        <c:set var="addresses" value="${addressDAO.findByUserId(sessionScope.user.userId)}" scope="request"/>
    </c:catch>
</c:if>

<c:set var="pageTitle" value="My Profile - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">Profile Settings</li>
            </ol>
        </nav>

        <h3 class="fw-extrabold mb-4 text-dark d-flex align-items-center gap-2">
            <i class="bi bi-person-gear text-primary"></i> Account &amp; Profile Settings
        </h3>

        <div class="row g-4">
            <!-- Left: Personal Info & Security -->
            <div class="col-lg-6">
                <!-- Profile Information Card -->
                <div class="bg-white p-4 rounded-4 shadow-sm border border-light mb-4">
                    <h5 class="fw-bold mb-3">Personal Details</h5>
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="update_info">
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Email Address (Read-Only)</label>
                            <input type="email" class="form-control rounded-3 bg-light" value="${userProfile.email}" readonly>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Full Name</label>
                            <input type="text" name="name" class="form-control rounded-3" value="${userProfile.name}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Mobile Number</label>
                            <input type="tel" name="mobile" class="form-control rounded-3" value="${userProfile.mobile}">
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm rounded-pill px-4 fw-semibold">Save Changes</button>
                    </form>
                </div>

                <!-- Password Change Card -->
                <div class="bg-white p-4 rounded-4 shadow-sm border border-light">
                    <h5 class="fw-bold mb-3">Change Password</h5>
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="change_password">
                        <div class="mb-3">
                            <label class="form-label small fw-semibold">Current Password</label>
                            <input type="password" name="oldPassword" class="form-control rounded-3" required>
                        </div>
                        <div class="row g-2 mb-3">
                            <div class="col-md-6">
                                <label class="form-label small fw-semibold">New Password</label>
                                <input type="password" name="newPassword" class="form-control rounded-3" minlength="6" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-semibold">Confirm Password</label>
                                <input type="password" name="confirmPassword" class="form-control rounded-3" minlength="6" required>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-outline-primary btn-sm rounded-pill px-4 fw-semibold">Update Password</button>
                    </form>
                </div>
            </div>

            <!-- Right: Address Book Management -->
            <div class="col-lg-6">
                <div class="bg-white p-4 rounded-4 shadow-sm border border-light">
                    <h5 class="fw-bold mb-3">Saved Delivery Addresses</h5>

                    <c:choose>
                        <c:when test="${empty addresses}">
                            <p class="text-muted small">No saved addresses found. Add an address below for swift checkout.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="d-flex flex-column gap-2 mb-4">
                                <c:forEach var="a" items="${addresses}">
                                    <div class="p-3 border rounded-3 bg-light d-flex justify-content-between align-items-center">
                                        <div>
                                            <span class="badge bg-secondary-subtle text-dark me-2">${a.addressType}</span>
                                            <span class="small text-dark">${a.addressLine}, ${a.city}, ${a.state} - ${a.pincode}</span>
                                        </div>
                                        <form action="${pageContext.request.contextPath}/profile" method="post">
                                            <input type="hidden" name="action" value="delete_address">
                                            <input type="hidden" name="addressId" value="${a.addressId}">
                                            <button type="submit" class="btn btn-link text-danger p-0 ms-2" title="Delete Address">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <h6 class="fw-bold mt-4 mb-3">Add New Address</h6>
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <input type="hidden" name="action" value="add_address">
                        <div class="mb-2">
                            <input type="text" name="addressLine" class="form-control form-control-sm rounded-3" placeholder="Address line / Flat / Street" required>
                        </div>
                        <div class="row g-2 mb-2">
                            <div class="col-md-5">
                                <input type="text" name="city" class="form-control form-control-sm rounded-3" placeholder="City" required>
                            </div>
                            <div class="col-md-4">
                                <input type="text" name="state" class="form-control form-control-sm rounded-3" placeholder="State" required>
                            </div>
                            <div class="col-md-3">
                                <input type="text" name="pincode" class="form-control form-control-sm rounded-3" placeholder="PIN Code" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <select name="addressType" class="form-select form-select-sm rounded-3">
                                <option value="HOME">Home</option>
                                <option value="WORK">Work</option>
                                <option value="OTHER">Other</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-outline-secondary btn-sm rounded-pill px-4">+ Save Address</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
