<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="sticky-top navbar-custom">
    <!-- Top Utility Announcement Bar -->
    <div class="bg-dark text-white py-1 px-3 small border-bottom border-secondary border-opacity-25">
        <div class="container d-flex justify-content-between align-items-center">
            <div class="d-flex align-items-center gap-3">
                <span><i class="bi bi-shield-check text-success me-1"></i> RTU Advanced Java Enterprise Capstone</span>
                <span class="d-none d-md-inline text-muted">|</span>
                <span class="d-none d-md-inline"><i class="bi bi-truck me-1"></i> Free Shipping on orders over ₹999</span>
            </div>
            <div class="d-flex align-items-center gap-3">
                <!-- Internationalization Language Switcher -->
                <div class="dropdown">
                    <button class="btn btn-sm btn-link text-white text-decoration-none dropdown-toggle p-0 small" type="button" data-bs-toggle="dropdown">
                        <i class="bi bi-translate me-1"></i> ${sessionScope.lang == 'hi' ? 'हिंदी (HI)' : 'English (EN)'}
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end shadow-sm">
                        <li><a class="dropdown-item ${sessionScope.lang != 'hi' ? 'active' : ''}" href="?lang=en"><i class="bi bi-check2 me-1"></i> English</a></li>
                        <li><a class="dropdown-item ${sessionScope.lang == 'hi' ? 'active' : ''}" href="?lang=hi"><i class="bi bi-check2 me-1"></i> हिंदी (Hindi)</a></li>
                    </ul>
                </div>
                <c:if test="${sessionScope.user != null && sessionScope.user.admin}">
                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="badge bg-warning text-dark text-decoration-none fw-bold">
                        <i class="bi bi-speedometer2"></i> Admin Portal
                    </a>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Main Navigation Bar -->
    <nav class="navbar navbar-expand-lg py-3">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/index.jsp">
                <i class="bi bi-bag-check-fill text-primary me-2 fs-3"></i>
                <span>ShopSphere</span>
                <span class="brand-badge">PRO</span>
            </a>

            <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarMain">
                <!-- Search Bar -->
                <form class="d-flex mx-auto my-2 my-lg-0 col-12 col-lg-5" action="${pageContext.request.contextPath}/products" method="get">
                    <div class="input-group">
                        <input type="text" name="q" class="form-control rounded-start-pill ps-3 border-end-0 bg-light"
                               placeholder="Search laptops, fashion, electronics..." value="${param.q}">
                        <button class="btn btn-outline-secondary border-start-0 rounded-end-pill pe-3 bg-light text-primary" type="submit">
                            <i class="bi bi-search"></i>
                        </button>
                    </div>
                </form>

                <!-- Navigation Links -->
                <ul class="navbar-nav align-items-center gap-lg-2">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/products">All Products</a>
                    </li>
                    
                    <!-- Syllabus Demos Dropdown -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            RTU Demos
                        </a>
                        <ul class="dropdown-menu shadow-sm">
                            <li><h6 class="dropdown-header">Syllabus Demonstrations</h6></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/jsp-demo.jsp"><i class="bi bi-filetype-java me-2"></i> JSP Elements &amp; Directives</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/jstl-sql-demo.jsp"><i class="bi bi-database me-2"></i> JSTL SQL Tags Demo</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/jstl-xml-demo.jsp"><i class="bi bi-filetype-xml me-2"></i> JSTL XML Tags Demo</a></li>
                        </ul>
                    </li>

                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <!-- Wishlist -->
                            <li class="nav-item">
                                <a class="nav-link position-relative" href="${pageContext.request.contextPath}/wishlist" title="Wishlist">
                                    <i class="bi bi-heart fs-5"></i>
                                </a>
                            </li>

                            <!-- Cart with Counter Badge -->
                            <li class="nav-item me-2">
                                <a class="nav-link position-relative" href="${pageContext.request.contextPath}/cart" title="Shopping Cart">
                                    <i class="bi bi-cart3 fs-5"></i>
                                    <span class="position-absolute top-1 start-100 translate-middle badge rounded-pill bg-primary">
                                        ${sessionScope.cartItemCount != null ? sessionScope.cartItemCount : 0}
                                    </span>
                                </a>
                            </li>

                            <!-- User Account Dropdown -->
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle d-flex align-items-center gap-2 fw-bold text-dark" href="#" role="button" data-bs-toggle="dropdown">
                                    <div class="rounded-circle bg-primary-subtle text-primary d-flex align-items-center justify-content-center" style="width: 32px; height: 32px;">
                                        <i class="bi bi-person-fill"></i>
                                    </div>
                                    <span>${sessionScope.user.name}</span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end shadow-sm">
                                    <li><h6 class="dropdown-header">Signed in as ${sessionScope.user.email}</h6></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile"><i class="bi bi-person me-2"></i> My Profile</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders"><i class="bi bi-box-seam me-2"></i> My Orders</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/wishlist"><i class="bi bi-heart me-2"></i> Wishlist</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right me-2"></i> Sign Out</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item ms-lg-2">
                                <a class="btn btn-outline-primary rounded-pill px-4 btn-sm" href="${pageContext.request.contextPath}/login.jsp">
                                    <i class="bi bi-box-arrow-in-right me-1"></i> Sign In
                                </a>
                            </li>
                            <li class="nav-item ms-lg-1">
                                <a class="btn btn-primary rounded-pill px-4 btn-sm" href="${pageContext.request.contextPath}/register.jsp">
                                    Register
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
</header>
