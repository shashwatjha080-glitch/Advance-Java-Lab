<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="productDAO" class="com.shopsphere.dao.ProductDAO" scope="page"/>
<jsp:useBean id="categoryDAO" class="com.shopsphere.dao.CategoryDAO" scope="page"/>

<c:set var="pageTitle" value="ShopSphere - Modern E-Commerce Platform" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Hero Banner -->
        <div class="hero-banner shadow-sm mb-5">
            <div class="row align-items-center position-relative" style="z-index: 2;">
                <div class="col-lg-7">
                    <span class="badge bg-primary bg-opacity-50 text-white rounded-pill px-3 py-2 mb-3 fw-bold">
                        <i class="bi bi-stars me-1"></i> Spring 2026 Collection
                    </span>
                    <h1 class="display-4 fw-extrabold mb-3 text-white">Next-Gen Tech &amp; Signature Living</h1>
                    <p class="lead text-light text-opacity-75 mb-4">
                        Experience lightning-fast shopping with real-time stock availability, seamless checkout, and verified customer reviews.
                    </p>
                    <div class="d-flex flex-wrap gap-3">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4 py-2 fs-6">
                            Explore Catalog <i class="bi bi-arrow-right ms-1"></i>
                        </a>
                        <a href="#featured" class="btn btn-outline-light rounded-pill px-4 py-2 fs-6">
                            View Deals
                        </a>
                    </div>
                </div>
                <div class="col-lg-5 d-none d-lg-block text-center">
                    <img src="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80"
                         alt="Featured Headphones" class="img-fluid rounded-4 shadow-lg" style="max-height: 280px;">
                </div>
            </div>
        </div>

        <!-- Category Quick Filter Pills -->
        <div class="mb-5">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h4 class="fw-bold m-0 text-dark">Explore by Category</h4>
                <a href="${pageContext.request.contextPath}/products" class="text-primary text-decoration-none fw-semibold small">
                    View All <i class="bi bi-chevron-right"></i>
                </a>
            </div>
            <div class="d-flex flex-wrap gap-2">
                <a href="${pageContext.request.contextPath}/products" class="category-pill ${empty param.category ? 'active' : ''}">
                    <i class="bi bi-grid-fill"></i> All Departments
                </a>
                <c:catch var="catEx">
                    <c:forEach var="cat" items="${categoryDAO.findAll(true)}">
                        <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}" class="category-pill ${param.category == cat.categoryId ? 'active' : ''}">
                            <i class="bi bi-tag-fill text-primary"></i> ${cat.name}
                        </a>
                    </c:forEach>
                </c:catch>
            </div>
        </div>

        <!-- Featured Products Section -->
        <section id="featured" class="mb-5">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h3 class="fw-bold text-dark m-0">Top Offers &amp; Trending Gear</h3>
                    <p class="text-muted small m-0">High performance gear selected for enterprise developers and creators</p>
                </div>
                <a href="${pageContext.request.contextPath}/products?sort=discount" class="btn btn-sm btn-outline-primary rounded-pill px-3">
                    See More Offers
                </a>
            </div>

            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
                <c:catch var="prodEx">
                    <c:forEach var="p" items="${productDAO.findFeatured(8)}">
                        <div class="col">
                            <tags:productCard product="${p}" />
                        </div>
                    </c:forEach>
                </c:catch>
            </div>
        </section>

        <!-- Value Proposition Banners -->
        <div class="row g-4 my-5 py-3 bg-white rounded-4 shadow-sm border border-light">
            <div class="col-md-3 d-flex align-items-center gap-3 px-4">
                <div class="fs-1 text-primary"><i class="bi bi-truck"></i></div>
                <div>
                    <h6 class="fw-bold mb-1">Express Delivery</h6>
                    <p class="text-muted small mb-0">Free shipping on orders over ₹999</p>
                </div>
            </div>
            <div class="col-md-3 d-flex align-items-center gap-3 px-4">
                <div class="fs-1 text-primary"><i class="bi bi-shield-check"></i></div>
                <div>
                    <h6 class="fw-bold mb-1">Secure Payments</h6>
                    <p class="text-muted small mb-0">Encrypted mock gateway processing</p>
                </div>
            </div>
            <div class="col-md-3 d-flex align-items-center gap-3 px-4">
                <div class="fs-1 text-primary"><i class="bi bi-arrow-repeat"></i></div>
                <div>
                    <h6 class="fw-bold mb-1">30-Day Returns</h6>
                    <p class="text-muted small mb-0">Hassle-free replacement policy</p>
                </div>
            </div>
            <div class="col-md-3 d-flex align-items-center gap-3 px-4">
                <div class="fs-1 text-primary"><i class="bi bi-headset"></i></div>
                <div>
                    <h6 class="fw-bold mb-1">24/7 Support</h6>
                    <p class="text-muted small mb-0">Dedicated academic assistance</p>
                </div>
            </div>
        </div>

    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
