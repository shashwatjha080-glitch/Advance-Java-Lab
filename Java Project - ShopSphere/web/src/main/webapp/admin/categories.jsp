<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:useBean id="categoryDAO" class="com.shopsphere.dao.CategoryDAO" scope="page"/>

<c:if test="${empty categories}">
    <c:catch var="catEx">
        <c:set var="categories" value="${categoryDAO.findAll(false)}" scope="request"/>
    </c:catch>
</c:if>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories - ShopSphere Admin</title>
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
                    <h3 class="fw-extrabold text-dark m-0">Category Management</h3>
                    <small class="text-muted">Organize catalog departments and merchandise hierarchies</small>
                </div>
                <button type="button" class="btn btn-primary rounded-pill btn-sm px-3" data-bs-toggle="modal" data-bs-target="#addCategoryModal">
                    <i class="bi bi-plus-lg me-1"></i> Add Category
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

            <div class="card border-0 rounded-4 shadow-sm bg-white p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light">
                            <tr class="small text-muted">
                                <th>ID</th>
                                <th>Category Name</th>
                                <th>Description</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${categories}">
                                <tr>
                                    <td class="fw-bold">#${c.categoryId}</td>
                                    <td><strong class="text-dark">${c.name}</strong></td>
                                    <td class="text-muted small">${c.description}</td>
                                    <td>
                                        <span class="badge ${c.status ? 'bg-success' : 'bg-secondary'}">
                                            ${c.status ? 'Active' : 'Disabled'}
                                        </span>
                                    </td>
                                    <td class="text-end">
                                        <button class="btn btn-sm btn-outline-secondary rounded-circle me-1"
                                                onclick="populateCategoryModal('${c.categoryId}', '${c.name}', `${c.description}`, '${c.status}')"
                                                data-bs-toggle="modal" data-bs-target="#editCategoryModal" title="Edit">
                                            <i class="bi bi-pencil"></i>
                                        </button>
                                        <form action="${pageContext.request.contextPath}/admin/categories" method="post" class="d-inline"
                                              onsubmit="return confirm('Delete category? Products must be reassigned first.');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="categoryId" value="${c.categoryId}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger rounded-circle" title="Delete">
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

<!-- Add Category Modal -->
<div class="modal fade" id="addCategoryModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 border-0">
            <form action="${pageContext.request.contextPath}/admin/categories" method="post">
                <input type="hidden" name="action" value="create">
                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold">Create Category</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Category Name</label>
                        <input type="text" name="name" class="form-control rounded-3" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Description</label>
                        <textarea name="description" rows="3" class="form-control rounded-3"></textarea>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="status" id="addCatStatus" checked>
                        <label class="form-check-label small" for="addCatStatus">Active &amp; Visible in Navigation</label>
                    </div>
                </div>
                <div class="modal-footer border-0">
                    <button type="button" class="btn btn-light rounded-pill px-4" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary rounded-pill px-4">Create</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Edit Category Modal -->
<div class="modal fade" id="editCategoryModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content rounded-4 border-0">
            <form action="${pageContext.request.contextPath}/admin/categories" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" id="editCatId" name="categoryId">
                <div class="modal-header border-0 pb-0">
                    <h5 class="modal-title fw-bold">Edit Category</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Category Name</label>
                        <input type="text" id="editCatName" name="name" class="form-control rounded-3" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label small fw-semibold">Description</label>
                        <textarea id="editCatDesc" name="description" rows="3" class="form-control rounded-3"></textarea>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" id="editCatStatus" name="status">
                        <label class="form-check-label small" for="editCatStatus">Active &amp; Visible in Navigation</label>
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
function populateCategoryModal(id, name, desc, status) {
    document.getElementById('editCatId').value = id;
    document.getElementById('editCatName').value = name;
    document.getElementById('editCatDesc').value = desc;
    document.getElementById('editCatStatus').checked = (status === 'true');
}
</script>
</body>
</html>
