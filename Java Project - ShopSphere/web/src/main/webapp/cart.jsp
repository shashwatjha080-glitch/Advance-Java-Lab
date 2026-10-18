<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="cartDAO" class="com.shopsphere.dao.CartDAO" scope="page"/>
<jsp:useBean id="checkoutService" class="com.shopsphere.service.CheckoutService" scope="page"/>

<%-- Fallback fetch if opened directly without CartServlet --%>
<c:if test="${empty cartItems && not empty sessionScope.user}">
    <c:catch var="cartEx">
        <c:set var="cartItems" value="${cartDAO.getCartItems(sessionScope.user.userId)}" scope="request"/>
        <c:set var="calc" value="${checkoutService.calculateOrder(cartItems, param.coupon, sessionScope.user.userId)}" scope="request"/>
    </c:catch>
</c:if>

<c:set var="pageTitle" value="Shopping Cart - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">Shopping Cart</li>
            </ol>
        </nav>

        <h3 class="fw-extrabold mb-4 text-dark d-flex align-items-center gap-2">
            <i class="bi bi-cart3 text-primary"></i> Your Shopping Cart
        </h3>

        <c:choose>
            <c:when test="${empty cartItems}">
                <div class="bg-white p-5 rounded-4 text-center shadow-sm border border-light">
                    <div class="display-1 text-muted mb-3"><i class="bi bi-cart-x"></i></div>
                    <h4 class="fw-bold">Your Cart is Empty</h4>
                    <p class="text-muted mb-4">Looks like you haven't added any products to your shopping cart yet.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 py-2 fw-semibold">
                        Start Shopping Now <i class="bi bi-arrow-right ms-1"></i>
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-4">
                    <!-- Cart Items Column -->
                    <div class="col-lg-8">
                        <div class="bg-white p-4 rounded-4 shadow-sm border border-light mb-3">
                            <div class="d-flex justify-content-between align-items-center border-bottom pb-3 mb-3">
                                <span class="fw-bold text-dark">${cartItems.size()} Items in Cart</span>
                                <form action="${pageContext.request.contextPath}/cart" method="post" onsubmit="return confirm('Clear all items from your cart?');">
                                    <input type="hidden" name="action" value="clear">
                                    <button type="submit" class="btn btn-link text-danger text-decoration-none btn-sm p-0">
                                        <i class="bi bi-trash3 me-1"></i> Clear Cart
                                    </button>
                                </form>
                            </div>

                            <c:forEach var="item" items="${cartItems}">
                                <div class="cart-item-row d-flex flex-column flex-sm-row align-items-sm-center justify-content-between gap-3">
                                    <div class="d-flex align-items-center gap-3">
                                        <img src="${item.product.imageUrl}" alt="${item.product.name}" class="cart-thumb"
                                             onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80';">
                                        <div>
                                            <span class="badge bg-light text-muted small mb-1">${item.product.brand}</span>
                                            <h6 class="mb-1">
                                                <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="text-decoration-none text-dark fw-bold">
                                                    ${item.product.name}
                                                </a>
                                            </h6>
                                            <div class="text-primary fw-bold">
                                                ₹<fmt:formatNumber value="${item.product.discountedPrice}" pattern="#,##0.00"/>
                                                <c:if test="${item.product.discount > 0}">
                                                    <small class="text-muted text-decoration-line-through ms-1">₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00"/></small>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Quantity form & Item Subtotal -->
                                    <div class="d-flex align-items-center justify-content-between justify-content-sm-end gap-4">
                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="d-flex align-items-center gap-2">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="cartId" value="${item.cartId}">
                                            <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stock}"
                                                   class="form-control form-control-sm rounded-pill text-center cart-qty-input" style="width: 70px;">
                                        </form>

                                        <div class="text-end" style="min-width: 100px;">
                                            <div class="fw-bold text-dark">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></div>
                                        </div>

                                        <form action="${pageContext.request.contextPath}/cart" method="post">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="cartId" value="${item.cartId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm rounded-circle" title="Remove Item">
                                                <i class="bi bi-x-lg"></i>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary rounded-pill px-4 btn-sm">
                            <i class="bi bi-arrow-left me-1"></i> Continue Shopping
                        </a>
                    </div>

                    <!-- Order Summary Column -->
                    <div class="col-lg-4">
                        <div class="order-summary-card">
                            <h5 class="fw-bold mb-3 text-dark">Order Summary</h5>

                            <!-- Promo Coupon Box -->
                            <form action="${pageContext.request.contextPath}/cart" method="get" class="mb-3">
                                <label for="couponCodeInput" class="form-label small fw-semibold">Have a Promo Coupon?</label>
                                <div class="input-group mb-2">
                                    <input type="text" id="couponCodeInput" name="coupon" class="form-control form-control-sm rounded-start-pill text-uppercase"
                                           placeholder="Enter code" value="${param.coupon}">
                                    <button class="btn btn-outline-primary btn-sm rounded-end-pill px-3" type="submit">Apply</button>
                                </div>
                                <c:if test="${not empty calc.couponMessage}">
                                    <div class="small ${calc.couponDiscount > 0 ? 'text-success' : 'text-danger'} fw-semibold mb-2">
                                        ${calc.couponMessage}
                                    </div>
                                </c:if>
                            </form>

                            <!-- Demo Coupon Chips -->
                            <div class="mb-4">
                                <span class="small text-muted d-block mb-1">Click to apply demo coupon:</span>
                                <div class="d-flex flex-wrap gap-1">
                                    <button type="button" class="badge bg-light text-primary border border-primary-subtle text-decoration-none" onclick="copyCoupon('WELCOME10')">WELCOME10 (10% Off)</button>
                                    <button type="button" class="badge bg-light text-primary border border-primary-subtle text-decoration-none" onclick="copyCoupon('FESTIVE20')">FESTIVE20 (20% Off)</button>
                                    <button type="button" class="badge bg-light text-primary border border-primary-subtle text-decoration-none" onclick="copyCoupon('FLAT500')">FLAT500 (₹500 Off)</button>
                                </div>
                            </div>

                            <!-- Calculations Breakdown -->
                            <div class="d-flex justify-content-between mb-2">
                                <span class="text-muted">Subtotal</span>
                                <span class="fw-semibold">₹<fmt:formatNumber value="${calc.subtotal}" pattern="#,##0.00"/></span>
                            </div>
                            <c:if test="${calc.couponDiscount > 0}">
                                <div class="d-flex justify-content-between mb-2 text-success">
                                    <span>Coupon Discount</span>
                                    <span class="fw-semibold">-₹<fmt:formatNumber value="${calc.couponDiscount}" pattern="#,##0.00"/></span>
                                </div>
                            </c:if>
                            <div class="d-flex justify-content-between mb-3">
                                <span class="text-muted">Estimated Shipping</span>
                                <span class="fw-semibold">
                                    <c:choose>
                                        <c:when test="${calc.shippingCharge == 0}">
                                            <span class="badge bg-success-subtle text-success">FREE</span>
                                        </c:when>
                                        <c:otherwise>₹<fmt:formatNumber value="${calc.shippingCharge}" pattern="#,##0.00"/></c:otherwise>
                                    </c:choose>
                                </span>
                            </div>

                            <hr>

                            <div class="d-flex justify-content-between align-items-baseline mb-4">
                                <span class="fs-5 fw-bold text-dark">Grand Total</span>
                                <span class="fs-4 fw-extrabold text-primary">₹<fmt:formatNumber value="${calc.grandTotal}" pattern="#,##0.00"/></span>
                            </div>

                            <div class="d-grid">
                                <a href="${pageContext.request.contextPath}/checkout?coupon=${param.coupon}" class="btn btn-primary rounded-pill py-2 fw-bold">
                                    Proceed to Checkout <i class="bi bi-shield-lock-fill ms-1"></i>
                                </a>
                            </div>

                            <div class="text-center mt-3">
                                <small class="text-muted"><i class="bi bi-shield-check text-success"></i> 256-bit SSL Simulated Secure Checkout</small>
                            </div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
