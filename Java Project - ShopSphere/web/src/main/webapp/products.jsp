<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="productDAO" class="com.shopsphere.dao.ProductDAO" scope="page"/>
<jsp:useBean id="categoryDAO" class="com.shopsphere.dao.CategoryDAO" scope="page"/>

<c:if test="${empty products}">
    <%-- If directly opened without ProductServlet, fetch default products --%>
    <c:set var="categories" value="${categoryDAO.findAll(true)}" scope="request"/>
    <c:set var="products" value="${productDAO.findAll(null, null, null, 0, 12)}" scope="request"/>
    <c:set var="totalProducts" value="${productDAO.countAll(null, null)}" scope="request"/>
    <c:set var="currentPage" value="1" scope="request"/>
    <c:set var="totalPages" value="1" scope="request"/>
</c:if>

<c:set var="pageTitle" value="Products Catalog - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />
<jsp:include page="/fragments/alerts.jsp" />

<main class="py-4">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">Catalog</li>
            </ol>
        </nav>

        <div class="row g-4">
            <!-- Sidebar Filters -->
            <div class="col-lg-3">
                <div class="bg-white p-4 rounded-4 shadow-sm border border-light">
                    <h5 class="fw-bold mb-3 text-dark">Filter By</h5>
                    
                    <!-- Categories -->
                    <div class="mb-4">
                        <h6 class="fw-bold small text-muted text-uppercase mb-2">Category</h6>
                        <div class="d-flex flex-column gap-2">
                            <a href="${pageContext.request.contextPath}/products?q=${searchQuery}&sort=${sortBy}" 
                               class="text-decoration-none ${empty selectedCategory ? 'fw-bold text-primary' : 'text-secondary'} small">
                                <i class="bi bi-chevron-right me-1"></i> All Categories
                            </a>
                            <c:forEach var="cat" items="${categories}">
                                <a href="${pageContext.request.contextPath}/products?category=${cat.categoryId}&q=${searchQuery}&sort=${sortBy}" 
                                   class="text-decoration-none ${selectedCategory == cat.categoryId ? 'fw-bold text-primary' : 'text-secondary'} small">
                                    <i class="bi bi-chevron-right me-1"></i> ${cat.name}
                                </a>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Reset Filters -->
                    <div class="d-grid mt-4">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline-secondary btn-sm rounded-pill">
                            Reset All Filters
                        </a>
                    </div>
                </div>
            </div>

            <!-- Products Content Area -->
            <div class="col-lg-9">
                <!-- Header Toolbar: Title, Count & Sort -->
                <div class="bg-white p-3 rounded-4 shadow-sm border border-light d-flex flex-column flex-md-row justify-content-between align-items-center mb-4 gap-3">
                    <div>
                        <h4 class="fw-bold m-0 text-dark">
                            <c:choose>
                                <c:when test="${not empty searchQuery}">Search Results for "${searchQuery}"</c:when>
                                <c:otherwise>Product Catalog</c:otherwise>
                            </c:choose>
                        </h4>
                        <span class="text-muted small">Showing ${products.size()} of ${totalProducts} products</span>
                    </div>

                    <!-- Sort Selector Form -->
                    <form action="${pageContext.request.contextPath}/products" method="get" class="d-flex align-items-center gap-2">
                        <c:if test="${not empty selectedCategory}">
                            <input type="hidden" name="category" value="${selectedCategory}">
                        </c:if>
                        <c:if test="${not empty searchQuery}">
                            <input type="hidden" name="q" value="${searchQuery}">
                        </c:if>
                        <label for="sortSelect" class="small fw-semibold text-muted text-nowrap">Sort By:</label>
                        <select name="sort" id="sortSelect" class="form-select form-select-sm rounded-pill px-3" onchange="this.form.submit()">
                            <option value="" ${empty sortBy ? 'selected' : ''}>Newest Arrivals</option>
                            <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                            <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                            <option value="discount" ${sortBy == 'discount' ? 'selected' : ''}>Biggest Discounts</option>
                            <option value="name_asc" ${sortBy == 'name_asc' ? 'selected' : ''}>Product Name</option>
                        </select>
                    </form>
                </div>

                <!-- Products Grid -->
                <c:choose>
                    <c:when test="${empty products}">
                        <div class="bg-white p-5 rounded-4 text-center shadow-sm">
                            <div class="display-1 text-muted mb-3"><i class="bi bi-search"></i></div>
                            <h4 class="fw-bold">No Products Found</h4>
                            <p class="text-muted mb-4">We couldn't find any products matching your selected criteria.</p>
                            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary rounded-pill px-4">
                                Browse All Products
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
                            <c:forEach var="p" items="${products}">
                                <div class="col">
                                    <tags:productCard product="${p}" />
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Pagination Controls -->
                        <c:if test="${totalPages > 1}">
                            <nav aria-label="Page navigation" class="mt-5">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
                                        <a class="page-link rounded-start-pill" href="${pageContext.request.contextPath}/products?page=${currentPage - 1}&category=${selectedCategory}&q=${searchQuery}&sort=${sortBy}">Previous</a>
                                    </li>
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/products?page=${i}&category=${selectedCategory}&q=${searchQuery}&sort=${sortBy}">${i}</a>
                                        </li>
                                    </c:forEach>
                                    <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
                                        <a class="page-link rounded-end-pill" href="${pageContext.request.contextPath}/products?page=${currentPage + 1}&category=${selectedCategory}&q=${searchQuery}&sort=${sortBy}">Next</a>
                                    </li>
                                </ul>
                            </nav>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
