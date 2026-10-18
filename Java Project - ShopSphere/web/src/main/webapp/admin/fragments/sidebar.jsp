<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="col-auto col-md-3 col-xl-2 px-sm-2 px-0 admin-sidebar">
    <div class="d-flex flex-column align-items-center align-items-sm-start px-3 pt-3 text-white min-vh-100">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="d-flex align-items-center pb-3 mb-md-3 text-white text-decoration-none border-bottom border-secondary border-opacity-25 w-100">
            <i class="bi bi-shield-check text-warning fs-3 me-2"></i>
            <span class="fs-5 fw-extrabold d-none d-sm-inline">ShopSphere</span>
        </a>
        <ul class="nav nav-pills flex-column mb-sm-auto mb-0 align-items-center align-items-sm-start w-100 gap-1" id="menu">
            <li class="nav-item w-100">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link px-0 align-middle ${pageContext.request.servletPath.contains('dashboard') ? 'active' : ''}">
                    <i class="bi bi-speedometer2 fs-5 me-2"></i> <span class="d-none d-sm-inline">Dashboard</span>
                </a>
            </li>
            <li class="w-100">
                <a href="${pageContext.request.contextPath}/admin/products" class="nav-link px-0 align-middle ${pageContext.request.servletPath.contains('products') ? 'active' : ''}">
                    <i class="bi bi-box-seam fs-5 me-2"></i> <span class="d-none d-sm-inline">Products &amp; Stock</span>
                </a>
            </li>
            <li class="w-100">
                <a href="${pageContext.request.contextPath}/admin/categories" class="nav-link px-0 align-middle ${pageContext.request.servletPath.contains('categories') ? 'active' : ''}">
                    <i class="bi bi-tags fs-5 me-2"></i> <span class="d-none d-sm-inline">Categories</span>
                </a>
            </li>
            <li class="w-100">
                <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link px-0 align-middle ${pageContext.request.servletPath.contains('orders') ? 'active' : ''}">
                    <i class="bi bi-receipt fs-5 me-2"></i> <span class="d-none d-sm-inline">Orders</span>
                </a>
            </li>
            <li class="w-100">
                <a href="${pageContext.request.contextPath}/admin/users" class="nav-link px-0 align-middle ${pageContext.request.servletPath.contains('users') ? 'active' : ''}">
                    <i class="bi bi-people fs-5 me-2"></i> <span class="d-none d-sm-inline">Customers</span>
                </a>
            </li>
            <li class="w-100">
                <a href="${pageContext.request.contextPath}/admin/coupons" class="nav-link px-0 align-middle ${pageContext.request.servletPath.contains('coupons') ? 'active' : ''}">
                    <i class="bi bi-ticket-perforated fs-5 me-2"></i> <span class="d-none d-sm-inline">Coupons</span>
                </a>
            </li>
        </ul>
        <hr class="w-100 border-secondary border-opacity-25">
        <div class="dropdown pb-4 w-100">
            <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle small" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                <div class="rounded-circle bg-warning text-dark fw-bold d-flex align-items-center justify-content-center me-2" style="width: 30px; height: 30px;">A</div>
                <span class="d-none d-sm-inline">${sessionScope.user.name}</span>
            </a>
            <ul class="dropdown-menu dropdown-menu-dark text-small shadow">
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/index.jsp"><i class="bi bi-shop me-2"></i> Customer Store</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right me-2"></i> Sign Out</a></li>
            </ul>
        </div>
    </div>
</div>
