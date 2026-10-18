<%@ tag description="Product Card Display Component" pageEncoding="UTF-8"%>
<%@ attribute name="product" required="true" type="com.shopsphere.model.Product" description="The product instance"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="card h-100 product-card shadow-sm border-0 position-relative">
    <c:if test="${product.discount > 0}">
        <span class="badge bg-danger position-absolute top-0 start-0 m-3 rounded-pill px-3 py-2 fw-semibold">
            -${product.discount}% OFF
        </span>
    </c:if>
    <div class="product-img-wrapper overflow-hidden position-relative bg-light rounded-top text-center p-3">
        <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}">
            <img src="${product.imageUrl}" alt="${product.name}" class="img-fluid rounded product-thumbnail"
                 onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80';">
        </a>
    </div>
    <div class="card-body d-flex flex-column p-3">
        <div class="text-muted small text-uppercase fw-semibold mb-1">${product.brand}</div>
        <h6 class="card-title mb-2">
            <a href="${pageContext.request.contextPath}/product-details?id=${product.productId}" class="text-decoration-none text-dark fw-bold product-title-link">
                ${product.name}
            </a>
        </h6>
        
        <div class="mt-auto pt-2">
            <div class="d-flex align-items-baseline gap-2 mb-3">
                <span class="fs-5 fw-bolder text-primary">₹<fmt:formatNumber value="${product.discountedPrice}" pattern="#,##0.00"/></span>
                <c:if test="${product.discount > 0}">
                    <span class="text-muted text-decoration-line-through small">₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
                </c:if>
            </div>
            
            <c:choose>
                <c:when test="${product.stock > 0}">
                    <form action="${pageContext.request.contextPath}/cart" method="post" class="d-grid">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productId}">
                        <input type="hidden" name="quantity" value="1">
                        <input type="hidden" name="redirect" value="same">
                        <button type="submit" class="btn btn-outline-primary btn-sm rounded-pill py-2 fw-semibold d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-cart-plus"></i> Add to Cart
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <button class="btn btn-secondary btn-sm rounded-pill py-2 w-100" disabled>
                        Out of Stock
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
