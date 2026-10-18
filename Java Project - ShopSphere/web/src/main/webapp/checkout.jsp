<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="cartDAO" class="com.shopsphere.dao.CartDAO" scope="page"/>
<jsp:useBean id="addressDAO" class="com.shopsphere.dao.AddressDAO" scope="page"/>
<jsp:useBean id="checkoutService" class="com.shopsphere.service.CheckoutService" scope="page"/>

<c:if test="${empty cartItems && not empty sessionScope.user}">
    <c:catch var="checkoutEx">
        <c:set var="cartItems" value="${cartDAO.getCartItems(sessionScope.user.userId)}" scope="request"/>
        <c:set var="calc" value="${checkoutService.calculateOrder(cartItems, param.coupon, sessionScope.user.userId)}" scope="request"/>
        <c:set var="addresses" value="${addressDAO.findByUserId(sessionScope.user.userId)}" scope="request"/>
    </c:catch>
</c:if>

<c:set var="pageTitle" value="Secure Checkout - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cart" class="text-decoration-none text-muted">Cart</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">Checkout</li>
            </ol>
        </nav>

        <h3 class="fw-extrabold mb-4 text-dark d-flex align-items-center gap-2">
            <i class="bi bi-shield-check text-primary"></i> Order Checkout &amp; Payment
        </h3>

        <form action="${pageContext.request.contextPath}/checkout" method="post">
            <input type="hidden" name="couponCode" value="${param.coupon}">

            <div class="row g-4">
                <!-- Left Details Column: Address & Payment Method -->
                <div class="col-lg-8">
                    <!-- Step 1: Shipping Address -->
                    <div class="bg-white p-4 rounded-4 shadow-sm border border-light mb-4">
                        <h5 class="fw-bold text-dark mb-3 d-flex align-items-center gap-2">
                            <span class="badge bg-primary rounded-circle" style="width: 28px; height: 28px; line-height: 20px;">1</span>
                            Shipping Address
                        </h5>

                        <c:if test="${not empty addresses}">
                            <div class="mb-3">
                                <label class="form-label small fw-semibold text-muted">Select Saved Address:</label>
                                <div class="d-flex flex-column gap-2">
                                    <c:forEach var="addr" items="${addresses}" varStatus="status">
                                        <div class="form-check p-3 border rounded-3 bg-light">
                                            <input class="form-check-input" type="radio" name="addressId" id="addr_${addr.addressId}"
                                                   value="${addr.addressId}" ${status.first ? 'checked' : ''} onchange="toggleNewAddress(false)">
                                            <label class="form-check-label w-100" for="addr_${addr.addressId}">
                                                <span class="badge bg-secondary-subtle text-dark me-2">${addr.addressType}</span>
                                                <strong>${addr.addressLine}</strong>, ${addr.city}, ${addr.state} - ${addr.pincode}
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="radio" name="addressId" id="addr_new" value="new" onchange="toggleNewAddress(true)">
                                <label class="form-check-label fw-bold text-primary" for="addr_new">
                                    + Deliver to a different address
                                </label>
                            </div>
                        </c:if>

                        <!-- New Address Form (Visible if no saved address or 'new' checked) -->
                        <div id="newAddressForm" style="${not empty addresses ? 'display: none;' : ''}">
                            <div class="row g-3">
                                <div class="col-12">
                                    <label class="form-label small fw-semibold">Street Address / House No.</label>
                                    <input type="text" name="addressLine" class="form-control rounded-3" placeholder="Flat 402, Royal Palms, MG Road" ${empty addresses ? 'required' : ''}>
                                </div>
                                <div class="col-md-5">
                                    <label class="form-label small fw-semibold">City</label>
                                    <input type="text" name="city" class="form-control rounded-3" placeholder="Bengaluru" ${empty addresses ? 'required' : ''}>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label small fw-semibold">State</label>
                                    <input type="text" name="state" class="form-control rounded-3" placeholder="Karnataka" ${empty addresses ? 'required' : ''}>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label small fw-semibold">PIN Code</label>
                                    <input type="text" name="pincode" class="form-control rounded-3" placeholder="560001" ${empty addresses ? 'required' : ''}>
                                </div>
                                <div class="col-12">
                                    <label class="form-label small fw-semibold">Address Type</label>
                                    <select name="addressType" class="form-select rounded-3">
                                        <option value="HOME">Home (All day delivery)</option>
                                        <option value="WORK">Work (Delivery 9 AM - 6 PM)</option>
                                        <option value="OTHER">Other</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Step 2: Payment Method (Simulated Gateway) -->
                    <div class="bg-white p-4 rounded-4 shadow-sm border border-light">
                        <h5 class="fw-bold text-dark mb-3 d-flex align-items-center gap-2">
                            <span class="badge bg-primary rounded-circle" style="width: 28px; height: 28px; line-height: 20px;">2</span>
                            Select Payment Method
                        </h5>

                        <div class="d-flex flex-column gap-3">
                            <div class="form-check p-3 border rounded-3 bg-light d-flex align-items-center gap-2">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_card" value="CREDIT_CARD" checked>
                                <label class="form-check-label w-100" for="pay_card">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <span class="fw-bold text-dark"><i class="bi bi-credit-card-2-front text-primary me-2"></i> Credit / Debit Card</span>
                                        <span class="badge bg-success-subtle text-success">Simulated Sandbox</span>
                                    </div>
                                    <small class="text-muted d-block">Visa, MasterCard, RuPay - mock payment transaction reference generated automatically</small>
                                </label>
                            </div>

                            <div class="form-check p-3 border rounded-3 bg-light d-flex align-items-center gap-2">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_upi" value="UPI">
                                <label class="form-check-label w-100" for="pay_upi">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <span class="fw-bold text-dark"><i class="bi bi-qr-code text-primary me-2"></i> UPI Instant Transfer</span>
                                        <span class="badge bg-primary-subtle text-primary">Fastest</span>
                                    </div>
                                    <small class="text-muted d-block">Google Pay, PhonePe, Paytm, BHIM</small>
                                </label>
                            </div>

                            <div class="form-check p-3 border rounded-3 bg-light d-flex align-items-center gap-2">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_net" value="NET_BANKING">
                                <label class="form-check-label w-100" for="pay_net">
                                    <span class="fw-bold text-dark"><i class="bi bi-bank text-primary me-2"></i> Net Banking</span>
                                    <small class="text-muted d-block">All Major Indian &amp; Global Banks supported</small>
                                </label>
                            </div>

                            <div class="form-check p-3 border rounded-3 bg-light d-flex align-items-center gap-2">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_cod" value="COD">
                                <label class="form-check-label w-100" for="pay_cod">
                                    <span class="fw-bold text-dark"><i class="bi bi-cash-stack text-primary me-2"></i> Cash on Delivery (COD)</span>
                                    <small class="text-muted d-block">Pay with cash or digital UPI on package delivery</small>
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Right Review Column -->
                <div class="col-lg-4">
                    <div class="order-summary-card">
                        <h5 class="fw-bold mb-3 text-dark">Order Items (${cartItems.size()})</h5>

                        <div class="d-flex flex-column gap-2 mb-3 overflow-auto" style="max-height: 220px;">
                            <c:forEach var="item" items="${cartItems}">
                                <div class="d-flex align-items-center justify-content-between small">
                                    <div class="text-truncate me-2" style="max-width: 190px;">
                                        <span class="fw-bold">${item.quantity}x</span> ${item.product.name}
                                    </div>
                                    <span class="fw-semibold">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00"/></span>
                                </div>
                            </c:forEach>
                        </div>

                        <hr>

                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted small">Items Subtotal</span>
                            <span class="fw-semibold small">₹<fmt:formatNumber value="${calc.subtotal}" pattern="#,##0.00"/></span>
                        </div>
                        <c:if test="${calc.couponDiscount > 0}">
                            <div class="d-flex justify-content-between mb-2 text-success small">
                                <span>Coupon (${calc.appliedCoupon.code})</span>
                                <span class="fw-semibold">-₹<fmt:formatNumber value="${calc.couponDiscount}" pattern="#,##0.00"/></span>
                            </div>
                        </c:if>
                        <div class="d-flex justify-content-between mb-3 small">
                            <span class="text-muted">Delivery</span>
                            <span class="fw-semibold">${calc.shippingCharge == 0 ? 'FREE' : '₹99.00'}</span>
                        </div>

                        <div class="d-flex justify-content-between align-items-baseline mb-4 pt-2 border-top">
                            <span class="fs-5 fw-bold text-dark">Total Payable</span>
                            <span class="fs-4 fw-extrabold text-primary">₹<fmt:formatNumber value="${calc.grandTotal}" pattern="#,##0.00"/></span>
                        </div>

                        <div class="d-grid mb-3">
                            <button type="submit" class="btn btn-primary rounded-pill py-3 fw-bold fs-6">
                                <i class="bi bi-lock-fill me-1"></i> Place Order Securely
                            </button>
                        </div>

                        <div class="alert alert-info small p-2 text-center rounded-3 mb-0">
                            <i class="bi bi-info-circle-fill me-1"></i> Simulated Enterprise Payment Environment. No real charges will occur.
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</main>

<script>
function toggleNewAddress(show) {
    const el = document.getElementById('newAddressForm');
    if (el) {
        el.style.display = show ? 'block' : 'none';
        const inputs = el.querySelectorAll('input');
        inputs.forEach(input => input.required = show);
    }
}
</script>

<jsp:include page="/fragments/footer.jsp" />
