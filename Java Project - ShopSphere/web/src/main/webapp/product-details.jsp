<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="productDAO" class="com.shopsphere.dao.ProductDAO" scope="page"/>
<jsp:useBean id="reviewDAO" class="com.shopsphere.dao.ReviewDAO" scope="page"/>

<c:if test="${empty product && not empty param.id}">
    <c:catch var="ex">
        <c:set var="product" value="${productDAO.findById(param.id)}" scope="request"/>
        <c:set var="reviews" value="${reviewDAO.findByProductId(param.id)}" scope="request"/>
        <c:set var="averageRating" value="${reviewDAO.getAverageRating(param.id)}" scope="request"/>
        <c:set var="reviewCount" value="${reviews.size()}" scope="request"/>
        <c:set var="relatedProducts" value="${productDAO.findFeatured(4)}" scope="request"/>
    </c:catch>
</c:if>

<c:set var="pageTitle" value="${product != null ? product.name : 'Product Details'} - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/products" class="text-decoration-none text-muted">Catalog</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">${product.name}</li>
            </ol>
        </nav>

        <c:choose>
            <c:when test="${empty product}">
                <div class="alert alert-warning text-center p-5 rounded-4 shadow-sm">
                    <h4>Product Not Found</h4>
                    <p>The product you are looking for does not exist or has been removed.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4">Browse Catalog</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row g-5">
                    <!-- Product Image Section -->
                    <div class="col-lg-6">
                        <div class="bg-white p-4 rounded-4 shadow-sm border border-light text-center">
                            <img src="${product.imageUrl}" alt="${product.name}" class="img-fluid rounded-3" style="max-height: 420px; object-fit: contain;"
                                 onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80';">
                        </div>
                    </div>

                    <!-- Product Specs & Purchase Section -->
                    <div class="col-lg-6">
                        <div class="d-flex align-items-center justify-content-between mb-2">
                            <span class="badge bg-primary-subtle text-primary fw-bold px-3 py-2 rounded-pill">
                                ${product.categoryName != null ? product.categoryName : 'Enterprise Product'}
                            </span>
                            <span class="text-muted small">Brand: <strong class="text-dark">${product.brand}</strong></span>
                        </div>

                        <h2 class="fw-extrabold text-dark mb-2">${product.name}</h2>

                        <!-- Ratings Component -->
                        <div class="d-flex align-items-center gap-2 mb-3">
                            <tags:rating rating="${averageRating != null ? averageRating : 5.0}" showNumber="true" />
                            <span class="text-muted small">|</span>
                            <a href="#reviewsSection" class="text-muted small text-decoration-none">${reviewCount} Reviews</a>
                        </div>

                        <!-- Pricing Breakdown -->
                        <div class="bg-light p-3 rounded-3 mb-4">
                            <div class="d-flex align-items-baseline gap-3">
                                <span class="fs-2 fw-extrabold text-primary">₹<fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00"/></span>
                                <c:if test="${product.discount > 0}">
                                    <span class="text-muted text-decoration-line-through fs-5">₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
                                    <span class="badge bg-danger rounded-pill px-3 py-2 fw-bold">Save ${product.discount}% (₹<fmt:formatNumber value="${product.savingsAmount}" pattern="#,##0.00"/>)</span>
                                </c:if>
                            </div>
                            <small class="text-success fw-semibold"><i class="bi bi-patch-check-fill"></i> Inclusive of all taxes &amp; import duties</small>
                        </div>

                        <!-- Stock Status -->
                        <div class="mb-4">
                            <c:choose>
                                <c:when test="${product.stock > 10}">
                                    <span class="badge bg-success-subtle text-success px-3 py-2 rounded-pill fw-semibold">
                                        <i class="bi bi-check-circle-fill me-1"></i> In Stock (${product.stock} units available)
                                    </span>
                                </c:when>
                                <c:when test="${product.stock > 0}">
                                    <span class="badge bg-warning-subtle text-warning-emphasis px-3 py-2 rounded-pill fw-semibold">
                                        <i class="bi bi-clock-history me-1"></i> Only ${product.stock} left in stock - order soon!
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-danger-subtle text-danger px-3 py-2 rounded-pill fw-semibold">
                                        <i class="bi bi-x-circle-fill me-1"></i> Currently Out of Stock
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Description -->
                        <div class="mb-4">
                            <h6 class="fw-bold text-dark">About this item</h6>
                            <p class="text-secondary leading-relaxed">${product.description}</p>
                        </div>

                        <!-- Add to Cart & Buy Now Form -->
                        <c:if test="${product.stock > 0}">
                            <form action="${pageContext.request.contextPath}/cart" method="post" class="mb-4">
                                <input type="hidden" name="productId" value="${product.productId}">
                                <div class="row g-3 align-items-center mb-3">
                                    <div class="col-auto">
                                        <label for="quantityInput" class="col-form-label fw-bold small">Quantity:</label>
                                    </div>
                                    <div class="col-3">
                                        <input type="number" id="quantityInput" name="quantity" class="form-control rounded-pill text-center"
                                               value="1" min="1" max="${product.stock}">
                                    </div>
                                </div>
                                <div class="d-flex flex-wrap gap-3">
                                    <button type="submit" name="action" value="add" class="btn btn-primary rounded-pill px-4 py-2 fw-bold flex-grow-1">
                                        <i class="bi bi-cart-plus me-1"></i> Add to Cart
                                    </button>
                                    <button type="submit" name="action" value="add" onclick="this.form.redirect.value='cart'" class="btn btn-dark rounded-pill px-4 py-2 fw-bold">
                                        <i class="bi bi-lightning-charge me-1"></i> Buy Now
                                    </button>
                                    <input type="hidden" name="redirect" value="same">
                                </div>
                            </form>
                        </c:if>

                        <!-- Wishlist Action -->
                        <form action="${pageContext.request.contextPath}/wishlist" method="post" class="d-inline">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.productId}">
                            <button type="submit" class="btn btn-outline-secondary btn-sm rounded-pill px-3">
                                <i class="bi bi-heart me-1 text-danger"></i> Add to Wishlist
                            </button>
                        </form>
                    </div>
                </div>

                <!-- Customer Reviews Section -->
                <div id="reviewsSection" class="mt-5 pt-4 border-top">
                    <div class="row g-4">
                        <div class="col-lg-5">
                            <div class="bg-white p-4 rounded-4 shadow-sm border border-light">
                                <h4 class="fw-bold mb-3">Write a Customer Review</h4>
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user}">
                                        <form action="${pageContext.request.contextPath}/add-review" method="post">
                                            <input type="hidden" name="productId" value="${product.productId}">
                                            <div class="mb-3">
                                                <label class="form-label small fw-semibold">Overall Rating</label>
                                                <select name="rating" class="form-select rounded-pill" required>
                                                    <option value="5">⭐⭐⭐⭐⭐ (5 Stars - Exceptional)</option>
                                                    <option value="4">⭐⭐⭐⭐ (4 Stars - Very Good)</option>
                                                    <option value="3">⭐⭐⭐ (3 Stars - Average)</option>
                                                    <option value="2">⭐⭐ (2 Stars - Below Expectations)</option>
                                                    <option value="1">⭐ (1 Star - Poor)</option>
                                                </select>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label small fw-semibold">Your Experience</label>
                                                <textarea name="reviewText" rows="3" class="form-control rounded-3" placeholder="What did you like or dislike about this product?" required></textarea>
                                            </div>
                                            <button type="submit" class="btn btn-primary btn-sm rounded-pill px-4">Submit Review</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="p-3 bg-light rounded-3 text-center">
                                            <p class="small text-muted mb-2">Have you used this product? Share your thoughts with other customers.</p>
                                            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline-primary btn-sm rounded-pill px-3">
                                                Sign In to Review
                                            </a>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <!-- Review List -->
                        <div class="col-lg-7">
                            <h4 class="fw-bold mb-3">Customer Feedback (${reviewCount})</h4>
                            <c:choose>
                                <c:when test="${empty reviews}">
                                    <p class="text-muted">No reviews yet for this product. Be the first to write one!</p>
                                </c:when>
                                <c:otherwise>
                                    <div class="d-flex flex-column gap-3">
                                        <c:forEach var="rev" items="${reviews}">
                                            <div class="bg-white p-3 rounded-4 shadow-sm border border-light">
                                                <div class="d-flex justify-content-between align-items-center mb-1">
                                                    <strong class="text-dark"><i class="bi bi-person-circle text-primary me-1"></i> ${rev.userName}</strong>
                                                    <tags:rating rating="${rev.rating * 1.0}" />
                                                </div>
                                                <p class="text-secondary small mb-1">${rev.reviewText}</p>
                                                <span class="text-muted" style="font-size: 0.75rem;"><fmt:formatDate value="${rev.createdAt}" pattern="MMM dd, yyyy" /></span>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <!-- Related Products -->
                <c:if test="${not empty relatedProducts}">
                    <div class="mt-5 pt-4 border-top">
                        <h4 class="fw-bold mb-4">You May Also Like</h4>
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-4 g-4">
                            <c:forEach var="rel" items="${relatedProducts}">
                                <div class="col">
                                    <tags:productCard product="${rel}" />
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
