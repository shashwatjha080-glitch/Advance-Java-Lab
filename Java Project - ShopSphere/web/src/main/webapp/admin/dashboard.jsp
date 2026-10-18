<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="orderService" class="com.shopsphere.service.OrderService" scope="page"/>
<jsp:useBean id="productDAO" class="com.shopsphere.dao.ProductDAO" scope="page"/>
<jsp:useBean id="orderDAO" class="com.shopsphere.dao.OrderDAO" scope="page"/>

<c:if test="${empty stats}">
    <c:catch var="dashEx">
        <c:set var="stats" value="${orderService.getDashboardStats()}" scope="request"/>
        <c:set var="recentOrders" value="${orderDAO.findAll()}" scope="request"/>
        <c:set var="lowStockProducts" value="${productDAO.findLowStock(10)}" scope="request"/>
    </c:catch>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - ShopSphere</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container-fluid">
    <div class="row flex-nowrap">
        <jsp:include page="/admin/fragments/sidebar.jsp" />

        <!-- Main Dashboard Content -->
        <div class="col py-4 px-4 px-md-5 overflow-auto" style="height: 100vh;">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-extrabold text-dark m-0">Operations Dashboard</h3>
                    <small class="text-muted">Enterprise Storefront Health &amp; Order Pipeline</small>
                </div>
                <div class="d-flex gap-2">
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-primary rounded-pill btn-sm px-3">
                        <i class="bi bi-plus-lg me-1"></i> Add Product
                    </a>
                    <a href="${pageContext.request.contextPath}/index.jsp" target="_blank" class="btn btn-outline-secondary rounded-pill btn-sm px-3">
                        <i class="bi bi-box-arrow-up-right me-1"></i> View Live Site
                    </a>
                </div>
            </div>

            <!-- KPI Metric Cards -->
            <div class="row g-4 mb-4">
                <div class="col-sm-6 col-xl-3">
                    <div class="card stat-card bg-white border-start border-primary border-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-muted small text-uppercase fw-bold">Total Revenue</span>
                                <h3 class="fw-extrabold text-dark mt-2 mb-0">₹<fmt:formatNumber value="${stats.totalRevenue}" pattern="#,##0.00"/></h3>
                            </div>
                            <div class="fs-1 text-primary bg-primary-subtle p-3 rounded-circle d-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                <i class="bi bi-currency-rupee"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="card stat-card bg-white border-start border-success border-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-muted small text-uppercase fw-bold">Orders Processed</span>
                                <h3 class="fw-extrabold text-dark mt-2 mb-0">${stats.totalOrders}</h3>
                            </div>
                            <div class="fs-1 text-success bg-success-subtle p-3 rounded-circle d-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                <i class="bi bi-bag-check"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="card stat-card bg-white border-start border-info border-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-muted small text-uppercase fw-bold">Registered Customers</span>
                                <h3 class="fw-extrabold text-dark mt-2 mb-0">${stats.totalCustomers}</h3>
                            </div>
                            <div class="fs-1 text-info bg-info-subtle p-3 rounded-circle d-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                <i class="bi bi-people"></i>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-sm-6 col-xl-3">
                    <div class="card stat-card bg-white border-start border-danger border-4">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <span class="text-muted small text-uppercase fw-bold">Low Stock Warning</span>
                                <h3 class="fw-extrabold text-danger mt-2 mb-0">${stats.lowStockCount}</h3>
                            </div>
                            <div class="fs-1 text-danger bg-danger-subtle p-3 rounded-circle d-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                <i class="bi bi-exclamation-triangle"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Orders and Low Stock Alerts -->
            <div class="row g-4">
                <!-- Recent Orders Table -->
                <div class="col-lg-8">
                    <div class="card border-0 rounded-4 shadow-sm bg-white p-4">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="fw-bold m-0 text-dark">Recent Orders</h5>
                            <a href="${pageContext.request.contextPath}/admin/orders" class="small text-decoration-none fw-semibold">View All Orders</a>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead class="table-light">
                                    <tr class="small text-muted">
                                        <th>Order ID</th>
                                        <th>Customer</th>
                                        <th>Amount</th>
                                        <th>Payment</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="ro" items="${recentOrders}">
                                        <tr>
                                            <td class="fw-bold text-primary">#SP-${ro.orderId}</td>
                                            <td>${ro.userName}</td>
                                            <td class="fw-bold">₹<fmt:formatNumber value="${ro.totalAmount}" pattern="#,##0.00"/></td>
                                            <td><span class="badge bg-success-subtle text-success">${ro.paymentStatus}</span></td>
                                            <td>
                                                <span class="badge bg-light text-dark border">${ro.orderStatus}</span>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-light btn-sm rounded-circle" title="Manage">
                                                    <i class="bi bi-pencil-square text-primary"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Low Stock Inventory Alerts -->
                <div class="col-lg-4">
                    <div class="card border-0 rounded-4 shadow-sm bg-white p-4">
                        <h5 class="fw-bold mb-3 text-dark d-flex align-items-center gap-2">
                            <i class="bi bi-bell-fill text-danger"></i> Inventory Stock Alerts
                        </h5>
                        <c:choose>
                            <c:when test="${empty lowStockProducts}">
                                <div class="text-center py-4 text-muted small">
                                    <i class="bi bi-check2-circle text-success fs-1 d-block mb-2"></i>
                                    All inventory levels are currently optimal.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="d-flex flex-column gap-3">
                                    <c:forEach var="lsp" items="${lowStockProducts}">
                                        <div class="p-3 bg-light rounded-3 d-flex justify-content-between align-items-center">
                                            <div>
                                                <strong class="d-block text-dark small">${lsp.name}</strong>
                                                <span class="badge bg-danger-subtle text-danger small">Stock: ${lsp.stock} remaining</span>
                                            </div>
                                            <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-flex align-items-center gap-1">
                                                <input type="hidden" name="action" value="stock">
                                                <input type="hidden" name="productId" value="${lsp.productId}">
                                                <input type="number" name="stock" value="${lsp.stock + 20}" class="form-control form-control-sm text-center" style="width: 60px;">
                                                <button type="submit" class="btn btn-sm btn-outline-success" title="Restock +20">
                                                    <i class="bi bi-arrow-up-circle"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
