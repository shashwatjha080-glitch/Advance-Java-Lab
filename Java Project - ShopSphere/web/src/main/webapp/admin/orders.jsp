<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="orderDAO" class="com.shopsphere.dao.OrderDAO" scope="page"/>

<c:if test="${empty orders}">
    <c:catch var="oEx">
        <c:set var="orders" value="${orderDAO.findAll()}" scope="request"/>
    </c:catch>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Orders - ShopSphere Admin</title>
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
                    <h3 class="fw-extrabold text-dark m-0">Order Fulfillment &amp; Logistics</h3>
                    <small class="text-muted">Track payments, status transitions and update shipments</small>
                </div>
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
                                <th>Order ID</th>
                                <th>Customer</th>
                                <th>Date Placed</th>
                                <th>Total</th>
                                <th>Payment</th>
                                <th>Fulfillment Status</th>
                                <th>Update Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${orders}">
                                <tr>
                                    <td class="fw-bold text-primary">#SP-${o.orderId}</td>
                                    <td>
                                        <strong class="d-block text-dark small">${o.userName}</strong>
                                        <small class="text-muted">${o.userEmail}</small>
                                    </td>
                                    <td class="small text-muted">
                                        <fmt:formatDate value="${o.createdAt}" pattern="MMM dd, yyyy" />
                                    </td>
                                    <td class="fw-bold text-dark">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                                    <td>
                                        <span class="badge bg-success-subtle text-success">${o.paymentMethod} (${o.paymentStatus})</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.orderStatus == 'DELIVERED'}"><span class="badge bg-success">Delivered</span></c:when>
                                            <c:when test="${o.orderStatus == 'SHIPPED'}"><span class="badge bg-info text-dark">Shipped</span></c:when>
                                            <c:when test="${o.orderStatus == 'CONFIRMED'}"><span class="badge bg-primary">Confirmed</span></c:when>
                                            <c:when test="${o.orderStatus == 'CANCELLED'}"><span class="badge bg-danger">Cancelled</span></c:when>
                                            <c:otherwise><span class="badge bg-warning text-dark">Placed</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/orders" method="post" class="d-flex align-items-center gap-1">
                                            <input type="hidden" name="orderId" value="${o.orderId}">
                                            <select name="orderStatus" class="form-select form-select-sm rounded-pill" style="width: 140px;">
                                                <option value="PLACED" ${o.orderStatus == 'PLACED' ? 'selected' : ''}>PLACED</option>
                                                <option value="CONFIRMED" ${o.orderStatus == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                                <option value="SHIPPED" ${o.orderStatus == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                                <option value="DELIVERED" ${o.orderStatus == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                                <option value="CANCELLED" ${o.orderStatus == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-outline-primary rounded-pill">Update</button>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
