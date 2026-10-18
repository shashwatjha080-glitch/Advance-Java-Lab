<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="orderDAO" class="com.shopsphere.dao.OrderDAO" scope="page"/>

<c:if test="${empty order && not empty param.id}">
    <c:catch var="ex">
        <c:set var="order" value="${orderDAO.findById(param.id)}" scope="request"/>
    </c:catch>
</c:if>

<c:set var="pageTitle" value="Order #SP-ORD-${order.orderId} - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/orders" class="text-decoration-none text-muted">My Orders</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">#SP-ORD-${order.orderId}</li>
            </ol>
        </nav>

        <c:choose>
            <c:when test="${empty order}">
                <div class="alert alert-warning text-center p-5 rounded-4">
                    <h4>Order Not Found</h4>
                    <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary rounded-pill px-4 mt-2">Back to Orders</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="d-flex flex-wrap justify-content-between align-items-center mb-4">
                    <div>
                        <h3 class="fw-extrabold text-dark m-0">Order #SP-ORD-${order.orderId}</h3>
                        <span class="text-muted small">Placed on <fmt:formatDate value="${order.createdAt}" pattern="MMMM dd, yyyy 'at' hh:mm a" /></span>
                    </div>
                    <span class="badge bg-primary px-3 py-2 rounded-pill fs-6">${order.orderStatus}</span>
                </div>

                <!-- Order Status Timeline Tracking -->
                <div class="card border-0 rounded-4 shadow-sm bg-white p-4 mb-4">
                    <h6 class="fw-bold mb-3">Order Status Progression</h6>
                    <div class="row text-center position-relative">
                        <div class="col-3">
                            <i class="bi bi-check-circle-fill text-success fs-3"></i>
                            <div class="fw-bold small mt-1">Placed</div>
                            <small class="text-muted d-block" style="font-size: 0.75rem;">Verified</small>
                        </div>
                        <div class="col-3">
                            <i class="bi ${order.orderStatus == 'CONFIRMED' || order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'bi-check-circle-fill text-success' : 'bi-circle text-muted'} fs-3"></i>
                            <div class="fw-bold small mt-1">Confirmed</div>
                            <small class="text-muted d-block" style="font-size: 0.75rem;">Inventory Reserved</small>
                        </div>
                        <div class="col-3">
                            <i class="bi ${order.orderStatus == 'SHIPPED' || order.orderStatus == 'DELIVERED' ? 'bi-truck text-success' : 'bi-circle text-muted'} fs-3"></i>
                            <div class="fw-bold small mt-1">Shipped</div>
                            <small class="text-muted d-block" style="font-size: 0.75rem;">In Transit</small>
                        </div>
                        <div class="col-3">
                            <i class="bi ${order.orderStatus == 'DELIVERED' ? 'bi-box2-heart-fill text-success' : 'bi-circle text-muted'} fs-3"></i>
                            <div class="fw-bold small mt-1">Delivered</div>
                            <small class="text-muted d-block" style="font-size: 0.75rem;">Completed</small>
                        </div>
                    </div>
                </div>

                <div class="row g-4">
                    <!-- Line Items List -->
                    <div class="col-lg-8">
                        <div class="card border-0 rounded-4 shadow-sm bg-white p-4">
                            <h5 class="fw-bold text-dark mb-3">Purchased Items (${order.items.size()})</h5>
                            <c:forEach var="item" items="${order.items}">
                                <div class="cart-item-row d-flex align-items-center justify-content-between gap-3">
                                    <div class="d-flex align-items-center gap-3">
                                        <img src="${item.productImageUrl}" alt="${item.productName}" class="cart-thumb"
                                             onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80';">
                                        <div>
                                            <span class="badge bg-light text-muted small">${item.productBrand}</span>
                                            <h6 class="mb-1">
                                                <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="text-decoration-none text-dark fw-bold">
                                                    ${item.productName}
                                                </a>
                                            </h6>
                                            <span class="text-muted small">Qty: ${item.quantity} × ₹<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/></span>
                                        </div>
                                    </div>
                                    <div class="text-end fw-bold text-dark">
                                        ₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Shipping & Payment Breakdown -->
                    <div class="col-lg-4">
                        <div class="order-summary-card mb-4">
                            <h5 class="fw-bold mb-3 text-dark">Delivery Address</h5>
                            <c:choose>
                                <c:when test="${not empty order.address}">
                                    <p class="small text-secondary mb-0">
                                        <strong class="text-dark">${order.userName}</strong><br>
                                        ${order.address.addressLine}<br>
                                        ${order.address.city}, ${order.address.state} - ${order.address.pincode}<br>
                                        <span class="badge bg-secondary-subtle text-dark mt-2">${order.address.addressType}</span>
                                    </p>
                                </c:when>
                                <c:otherwise>
                                    <p class="small text-muted mb-0">Standard Delivery Destination</p>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="order-summary-card">
                            <h5 class="fw-bold mb-3 text-dark">Payment Details</h5>
                            <div class="d-flex justify-content-between mb-2 small">
                                <span class="text-muted">Payment Method</span>
                                <span class="fw-bold">${order.paymentMethod}</span>
                            </div>
                            <div class="d-flex justify-content-between mb-2 small">
                                <span class="text-muted">Payment Status</span>
                                <span class="badge bg-success-subtle text-success">${order.paymentStatus}</span>
                            </div>
                            <c:if test="${order.discount > 0}">
                                <div class="d-flex justify-content-between mb-2 small text-success">
                                    <span>Coupon Discount</span>
                                    <span class="fw-bold">-₹<fmt:formatNumber value="${order.discount}" pattern="#,##0.00"/></span>
                                </div>
                            </c:if>
                            <hr>
                            <div class="d-flex justify-content-between align-items-baseline mb-3">
                                <span class="fs-6 fw-bold">Total Paid</span>
                                <span class="fs-5 fw-extrabold text-primary">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></span>
                            </div>
                            <button onclick="window.print()" class="btn btn-outline-secondary btn-sm rounded-pill w-100">
                                <i class="bi bi-printer me-1"></i> Print Invoice Receipt
                            </button>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
