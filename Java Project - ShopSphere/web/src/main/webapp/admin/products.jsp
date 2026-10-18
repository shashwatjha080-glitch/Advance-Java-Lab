<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:useBean id="productService" class="com.shopsphere.service.ProductService" scope="page"/>

<c:if test="${empty products}">
    <c:catch var="pEx">
        <c:set var="products" value="${productService.getAllProductsForAdmin()}" scope="request"/>
        <c:set var="categories" value="${productService.getAllCategories()}" scope="request"/>
    </c:catch>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Products - ShopSphere Admin</title>
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
                    <h3 class="fw-extrabold text-dark m-0">Catalog &amp; Product Management</h3>
                    <small class="text-muted">Create, modify, restock and monitor inventory</small>
                </div>
                <button type="button" class="btn btn-primary rounded-pill btn-sm px-3" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    <i class="bi bi-plus-lg me-1"></i> Add New Product
                </button>
            </div>

            <!-- Flash Alerts -->
            <c:if test="${not empty sessionScope.flashSuccess}">
                <div class="alert alert-success alert-dismissible fade show rounded-3 mb-3">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.flashSuccess}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="flashSuccess" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.flashError}">
                <div class="alert alert-danger alert-dismissible fade show rounded-3 mb-3">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> ${sessionScope.flashError}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="flashError" scope="session"/>
            </c:if>

            <!-- Products Table -->
            <div class="card border-0 rounded-4 shadow-sm bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr class="small text-muted">
                                <th>ID</th>
                                <th>Product</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Discount</th>
                                <th>Stock</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${products}">
                                <tr>
                                    <td class="fw-bold">#${p.productId}</td>
                                    <td>
                                        <div class="d-flex align-items-center gap-3">
                                            <img src="${p.imageUrl}" alt="${p.name}" class="rounded" style="width: 48px; height: 48px; object-fit: cover;"
                                                 onerror="this.onerror=null; this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80';">
                                            <div>
                                                <strong class="d-block text-dark small">${p.name}</strong>
                                                <small class="text-muted">${p.brand}</small>
                                            </div>
                                        </div>
                                    </td>
                                    <td><span class="badge bg-light text-dark border">${p.categoryName}</span></td>
                                    <td class="fw-bold">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.discount > 0}"><span class="badge bg-danger-subtle text-danger">-${p.discount}%</span></c:when>
                                            <c:otherwise><span class="text-muted small">0%</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <span class="badge ${p.stock <= 10 ? 'bg-danger-subtle text-danger' : 'bg-success-subtle text-success'} px-2 py-1">
                                            ${p.stock} in stock
                                        </span>
                                    </td>
                                    <td>
                                        <span class="badge ${p.status ? 'bg-success' : 'bg-secondary'}">
                                            ${p.status ? 'Active' : 'Disabled'}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <button class="btn btn-sm btn-outline-secondary rounded-circle me-1"
                                                onclick="populateEditModal('${p.productId}', '${p.name}', '${p.brand}', '${p.categoryId}', '${p.price}', '${p.discount}', '${p.stock}', '${p.imageUrl}', '${p.status}', `${p.description}`)"
                                                data-bs-toggle="modal" data-bs-target="#editProductModal" title="Edit Product">
                                            <i class="bi bi-pencil"></i>
                                        </button>
                                        <form action="${pageContext.request.contextPath}/admin/products" method="post" class="d-inline"
                                              onsubmit="return confirm('Are you sure you want to delete this product?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="productId" value="${p.productId}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger rounded-circle" title="Delete Product">
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

<!-- Add Product Modal -->
<div class="modal fade" id="addProductModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content rounded-4 border-0">
            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold">Add New Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label small fw-semibold">Product Name</label>
                            <input type="text" name="name" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-semibold">Brand</label>
                            <input type="text" name="brand" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Category</label>
                            <select name="categoryId" class="form-select rounded-3" required>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.categoryId}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-semibold">Price (₹)</label>
                            <input type="number" step="0.01" name="price" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-semibold">Discount (%)</label>
                            <input type="number" step="0.01" name="discount" value="0.00" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-semibold">Initial Stock</label>
                            <input type="number" name="stock" value="10" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label small fw-semibold">Image URL</label>
                            <input type="url" name="imageUrl" class="form-control rounded-3" placeholder="https://..." required>
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-semibold">Description</label>
                            <textarea name="description" rows="3" class="form-control rounded-3" required></textarea>
                        </div>
                        <div class="col-12">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" name="status" id="addStatus" checked>
                                <label class="form-check-label small" for="addStatus">Active &amp; Visible in Storefront</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4">Create Product</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Edit Product Modal -->
<div class="modal fade" id="editProductModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content rounded-4 border-0">
            <form action="${pageContext.request.contextPath}/admin/products" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" id="editProductId" name="productId">
                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold">Edit Product</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="row g-3">
                        <div class="col-md-8">
                            <label class="form-label small fw-semibold">Product Name</label>
                            <input type="text" id="editName" name="name" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-semibold">Brand</label>
                            <input type="text" id="editBrand" name="brand" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small fw-semibold">Category</label>
                            <select id="editCategoryId" name="categoryId" class="form-select rounded-3" required>
                                <c:forEach var="c" items="${categories}">
                                    <option value="${c.categoryId}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-semibold">Price (₹)</label>
                            <input type="number" step="0.01" id="editPrice" name="price" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-semibold">Discount (%)</label>
                            <input type="number" step="0.01" id="editDiscount" name="discount" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label small fw-semibold">Stock Quantity</label>
                            <input type="number" id="editStock" name="stock" class="form-control rounded-3" required>
                        </div>
                        <div class="col-md-8">
                            <label class="form-label small fw-semibold">Image URL</label>
                            <input type="url" id="editImageUrl" name="imageUrl" class="form-control rounded-3" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label small fw-semibold">Description</label>
                            <textarea id="editDescription" name="description" rows="3" class="form-control rounded-3" required></textarea>
                        </div>
                        <div class="col-12">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="editStatus" name="status">
                                <label class="form-check-label small" for="editStatus">Active &amp; Visible in Storefront</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4">Save Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
function populateEditModal(id, name, brand, catId, price, discount, stock, img, status, desc) {
    document.getElementById('editProductId').value = id;
    document.getElementById('editName').value = name;
    document.getElementById('editBrand').value = brand;
    document.getElementById('editCategoryId').value = catId;
    document.getElementById('editPrice').value = price;
    document.getElementById('editDiscount').value = discount;
    document.getElementById('editStock').value = stock;
    document.getElementById('editImageUrl').value = img;
    document.getElementById('editDescription').value = desc;
    document.getElementById('editStatus').checked = (status === 'true');
}
</script>
</body>
</html>
