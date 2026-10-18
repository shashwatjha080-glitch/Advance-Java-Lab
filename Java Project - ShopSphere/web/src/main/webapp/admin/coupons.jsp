<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="couponDAO" class="com.shopsphere.dao.CouponDAO" scope="page"/>

<c:if test="${empty coupons}">
    <c:catch var="coupEx">
        <c:set var="coupons" value="${couponDAO.findAll()}" scope="request"/>
    </c:catch>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Coupons - ShopSphere Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row flex-nowrap">
        <jsp:include page="/admin/fragments/sidebar.jsp" />

        <div class="col py-4 px-4 px-md-5 overflow-auto" style="height: 100vh;">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-extrabold text-dark m-0">Promotions &amp; Coupons</h3>
                    <small class="text-muted">Create discount promo codes and set eligibility constraints</small>
                </div>
                <button type="button" class="btn btn-primary rounded-pill btn-sm px-3" data-bs-toggle="modal" data-bs-target="#addCouponModal">
                    <i class="bi bi-plus-lg me-1"></i> Create Coupon
                </button>
            </div>

            <c:if test="${not empty sessionScope.flashSuccess}">
                <div class="alert alert-success alert-dismissible fade show rounded-3 mb-3">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.flashSuccess}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="flashSuccess" scope="session"/>
            </c:if>

            <div class="card border-0 rounded-4 shadow-sm bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr class="small text-muted">
                                <th>Coupon Code</th>
                                <th>Discount Type</th>
                                <th>Discount Value</th>
                                <th>Min. Order</th>
                                <th>Max. Cap</th>
                                <th>Valid Until</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cp" items="${coupons}">
                                <tr>
                                    <td><span class="badge bg-primary-subtle text-primary fs-6 px-3 py-1 font-monospace">${cp.code}</span></td>
                                    <td><span class="badge bg-light text-dark border">${cp.discountType}</span></td>
                                    <td class="fw-bold">
                                        <c:choose>
                                            <c:when test="${cp.discountType == 'PERCENTAGE'}">${cp.discountValue}% Off</c:when>
                                            <c:otherwise>₹<fmt:formatNumber value="${cp.discountValue}" pattern="#,##0.00"/> Flat</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>₹<fmt:formatNumber value="${cp.minimumOrder}" pattern="#,##0.00"/></td>
                                    <td>₹<fmt:formatNumber value="${cp.maximumDiscount}" pattern="#,##0.00"/></td>
                                    <td>${cp.expiryDate}</td>
                                    <td>
                                        <span class="badge ${cp.status && !cp.expired ? 'bg-success' : 'bg-secondary'}">
                                            ${cp.status && !cp.expired ? 'Active' : 'Expired/Disabled'}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <form action="${pageContext.request.contextPath}/admin/coupons" method="post" class="d-inline"
                                              onsubmit="return confirm('Delete coupon code?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="couponId" value="${cp.couponId}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger rounded-circle" title="Delete">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Add Coupon Modal -->
<div class="modal fade" id="addCouponModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 border-0">
            <form action="${pageContext.request.contextPath}/admin/coupons" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold">Create Coupon</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Coupon Code (Uppercase)</label>
                        <input type="text" name="code" class="form-control rounded-3 text-uppercase font-monospace" placeholder="FESTIVE30" required>
                    </div>
                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Discount Type</label>
                            <select name="discountType" class="form-select rounded-3">
                                <option value="PERCENTAGE">Percentage (%)</option>
                                <option value="FLAT">Flat Amount (₹)</option>
                            </select>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Discount Value</label>
                            <input type="number" step="0.01" name="discountValue" class="form-control rounded-3" placeholder="20" required>
                        </div>
                    </div>
                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Min. Order (₹)</label>
                            <input type="number" step="0.01" name="minimumOrder" value="500.00" class="form-control rounded-3" required>
                        </div>
                        <div class="col-6">
                            <label class="form-label small fw-semibold">Max. Discount Cap (₹)</label>
                            <input type="number" step="0.01" name="maximumDiscount" value="1000.00" class="form-control rounded-3" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Expiry Date</label>
                        <input type="date" name="expiryDate" value="2027-12-31" class="form-control rounded-3" required>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="status" id="cpStatus" checked>
                        <label class="form-check-label small" for="cpStatus">Active Immediately</label>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4">Save Coupon</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
