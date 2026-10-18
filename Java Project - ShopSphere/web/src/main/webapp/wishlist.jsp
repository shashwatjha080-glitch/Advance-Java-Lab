<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="wishlistDAO" class="com.shopsphere.dao.WishlistDAO" scope="page"/>

<c:if test="${empty wishlistItems && not empty sessionScope.user}">
    <c:catch var="wEx">
        <c:set var="wishlistItems" value="${wishlistDAO.getWishlistItems(sessionScope.user.userId)}" scope="request"/>
    </c:catch>
</c:if>

<c:set var="pageTitle" value="My Wishlist - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">My Wishlist</li>
            </ol>
        </nav>

        <h3 class="fw-extrabold mb-4 text-dark d-flex align-items-center gap-2">
            <i class="bi bi-heart-fill text-danger"></i> Saved Wishlist Items
        </h3>

        <c:choose>
            <c:when test="${empty wishlistItems}">
                <div class="bg-white p-5 rounded-4 text-center shadow-sm border border-light">
                    <div class="display-1 text-muted mb-3"><i class="bi bi-heart"></i></div>
                    <h4 class="fw-bold">Your Wishlist is Empty</h4>
                    <p class="text-muted mb-4">Explore our catalog and click the heart icon on any product to save it here for later.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4">
                        Discover Products
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
                    <c:forEach var="item" items="${wishlistItems}">
                        <div class="col">
                            <div class="card h-100 product-card shadow-sm border-0 position-relative">
                                <div class="product-img-wrapper overflow-hidden position-relative bg-light rounded-top text-center p-3">
                                    <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}">
                                        <img src="${item.product.imageUrl}" alt="${item.product.name}" class="img-fluid rounded product-thumbnail"
                                             onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80';">
                                    </a>
                                </div>
                                <div class="card-body d-flex flex-column p-3">
                                    <div class="text-muted small text-uppercase fw-semibold mb-1">${item.product.brand}</div>
                                    <h6 class="card-title mb-2">
                                        <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="text-decoration-none text-dark fw-bold">
                                            ${item.product.name}
                                        </a>
                                    </h6>
                                    
                                    <div class="mt-auto pt-2">
                                        <div class="d-flex align-items-baseline gap-2 mb-3">
                                            <span class="fs-5 fw-bolder text-primary">₹<fmt:formatNumber value="${item.product.discountedPrice}" pattern="#,##0.00"/></span>
                                        </div>
                                        
                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="mb-2">
                                            <input type="hidden" name="action" value="add">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <input type="hidden" name="quantity" value="1">
                                            <input type="hidden" name="redirect" value="cart">
                                            <button type="submit" class="btn btn-primary btn-sm rounded-pill w-100 fw-semibold">
                                                <i class="bi bi-cart-plus me-1"></i> Move to Cart
                                            </button>
                                        </form>

                                        <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="productId" value="${item.productId}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm rounded-pill w-100">
                                                <i class="bi bi-trash3 me-1"></i> Remove
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
