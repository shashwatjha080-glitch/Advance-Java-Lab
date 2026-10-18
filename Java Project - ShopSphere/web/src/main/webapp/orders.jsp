<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="orderDAO" class="com.shopsphere.dao.OrderDAO" scope="page"/>

<c:if test="${empty orders && not empty sessionScope.user}">
    <c:catch var="ordersEx">
        <c:set var="orders" value="${orderDAO.findByUserId(sessionScope.user.userId)}" scope="request"/>
    </c:catch>
</c:if>

<c:set var="pageTitle" value="My Orders - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">My Orders</li>
            </ol>
        </nav>

        <h3 class="fw-extrabold mb-4 text-dark d-flex align-items-center gap-2">
            <i class="bi bi-box-seam text-primary"></i> Order History &amp; Tracking
        </h3>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="bg-white p-5 rounded-4 text-center shadow-sm border border-light">
                    <div class="display-1 text-muted mb-3"><i class="bi bi-box"></i></div>
                    <h4 class="fw-bold">No Orders Placed Yet</h4>
                    <p class="text-muted mb-4">You have not placed any orders yet. Discover our latest products and exclusive discounts.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4">
                        Explore Catalog
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="d-flex flex-column gap-3">
                    <c:forEach var="o" items="${orders}">
                        <div class="card border-0 rounded-4 shadow-sm bg-white overflow-hidden">
                            <div class="card-header bg-light bg-opacity-50 p-3 d-flex flex-wrap justify-content-between align-items-center gap-2 border-0">
                                <div class="d-flex flex-wrap align-items-center gap-3">
                                    <span class="fw-bold text-dark">Order #SP-ORD-${o.orderId}</span>
                                    <span class="text-muted small">
                                        <i class="bi bi-calendar3 me-1"></i>
                                        <fmt:formatDate value="${o.createdAt}" pattern="MMM dd, yyyy HH:mm" />
                                    </span>
                                </div>
                                <div class="d-flex align-items-center gap-2">
                                    <c:choose>
                                        <c:when test="${o.orderStatus == 'DELIVERED'}">
                                            <span class="badge bg-success-subtle text-success px-3 py-2 rounded-pill fw-semibold">Delivered</span>
                                        </c:when>
                                        <c:when test="${o.orderStatus == 'SHIPPED'}">
                                            <span class="badge bg-info-subtle text-info-emphasis px-3 py-2 rounded-pill fw-semibold">Shipped</span>
                                        </c:when>
                                        <c:when test="${o.orderStatus == 'CONFIRMED'}">
                                            <span class="badge bg-primary-subtle text-primary px-3 py-2 rounded-pill fw-semibold">Confirmed</span>
                                        </c:when>
                                        <c:when test="${o.orderStatus == 'CANCELLED'}">
                                            <span class="badge bg-danger-subtle text-danger px-3 py-2 rounded-pill fw-semibold">Cancelled</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning-subtle text-warning-emphasis px-3 py-2 rounded-pill fw-semibold">Placed</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="card-body p-4 d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
                                <div>
                                    <div class="text-dark small mb-1">
                                        Total Items: <strong>${o.totalQuantity} items</strong>
                                    </div>
                                    <div class="text-muted small">
                                        Payment: <strong>${o.paymentMethod}</strong> (<span class="text-success">${o.paymentStatus}</span>)
                                    </div>
                                </div>
                                <div class="d-flex align-items-center gap-4">
                                    <div class="text-md-end">
                                        <span class="text-muted small d-block">Total Amount</span>
                                        <span class="fs-5 fw-extrabold text-primary">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></span>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/order-details?id=${o.orderId}" class="btn btn-outline-primary rounded-pill btn-sm px-4 fw-semibold">
                                        View Details <i class="bi bi-chevron-right ms-1"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
