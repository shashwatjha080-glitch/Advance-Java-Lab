<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="orderDAO" class="com.shopsphere.dao.OrderDAO" scope="page"/>

<c:if test="${empty order && not empty param.orderId}">
    <c:catch var="oEx">
        <c:set var="order" value="${orderDAO.findById(param.orderId)}" scope="request"/>
    </c:catch>
</c:if>

<c:set var="pageTitle" value="Order Confirmed - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="py-5">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card border-0 shadow-sm rounded-4 overflow-hidden text-center p-5 mb-4 bg-white">
                    <div class="mb-3">
                        <div class="rounded-circle bg-success bg-opacity-10 text-success d-inline-flex align-items-center justify-content-center"
                             style="width: 80px; height: 80px;">
                            <i class="bi bi-check2-circle display-4"></i>
                        </div>
                    </div>

                    <h2 class="fw-extrabold text-dark mb-1">Thank You for Your Order!</h2>
                    <p class="text-muted">Your order has been placed and is currently being processed by our automated fulfillment center.</p>

                    <c:choose>
                        <c:when test="${not empty order}">
                            <div class="badge bg-light text-dark fs-6 px-4 py-2 rounded-pill border mb-4">
                                Order Reference: <strong class="text-primary">#SP-ORD-${order.orderId}</strong>
                            </div>

                            <div class="card bg-light border-0 rounded-4 text-start p-4 mb-4">
                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <h6 class="fw-bold small text-muted text-uppercase mb-1">Shipping Destination</h6>
                                        <c:choose>
                                            <c:when test="${not empty order.address}">
                                                <p class="small mb-0 text-dark">
                                                    <strong>${order.userName}</strong><br>
                                                    ${order.address.addressLine}<br>
                                                    ${order.address.city}, ${order.address.state} - ${order.address.pincode}
                                                </p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="small mb-0 text-muted">Standard Express Delivery</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="col-md-6">
                                        <h6 class="fw-bold small text-muted text-uppercase mb-1">Payment &amp; Logistics</h6>
                                        <p class="small mb-0 text-dark">
                                            Method: <strong>${order.paymentMethod}</strong><br>
                                            Payment Status: <span class="badge bg-success-subtle text-success">${order.paymentStatus}</span><br>
                                            Order Status: <span class="badge bg-primary-subtle text-primary">${order.orderStatus}</span><br>
                                            Total Amount: <strong class="text-primary fs-6">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong>
                                        </p>
                                    </div>
                                </div>
                            </div>

                            <div class="d-flex justify-content-center gap-3">
                                <a href="${pageContext.request.contextPath}/order-details?id=${order.orderId}" class="btn btn-primary rounded-pill px-4 fw-semibold">
                                    <i class="bi bi-box-seam me-1"></i> Track Order Details
                                </a>
                                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary rounded-pill px-4 fw-semibold">
                                    Continue Shopping
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="text-success fw-bold">Order placed successfully!</p>
                            <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary rounded-pill px-4">
                                View Order History
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
