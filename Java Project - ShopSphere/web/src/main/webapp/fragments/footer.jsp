<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<footer class="mt-auto py-5 text-white">
    <div class="container">
        <div class="row g-4">
            <div class="col-12 col-md-4">
                <h5 class="fw-bold d-flex align-items-center mb-3 text-white">
                    <i class="bi bi-bag-check-fill text-primary me-2"></i> ShopSphere
                </h5>
                <p class="text-secondary small">
                    An advanced multi-tiered Java enterprise e-commerce platform architected for RTU Advanced Java laboratory evaluation, college capstone demonstrations, and distributed enterprise programming.
                </p>
                <div class="d-flex gap-3 fs-5">
                    <a href="#" class="text-secondary" title="GitHub"><i class="bi bi-github"></i></a>
                    <a href="#" class="text-secondary" title="Documentation"><i class="bi bi-journal-code"></i></a>
                    <a href="#" class="text-secondary" title="Enterprise"><i class="bi bi-diagram-3"></i></a>
                </div>
            </div>

            <div class="col-6 col-md-2">
                <h6 class="text-uppercase fw-bold text-light mb-3 small">Storefront</h6>
                <ul class="list-unstyled small d-flex flex-column gap-2">
                    <li><a href="${pageContext.request.contextPath}/products">All Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart">Shopping Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders">Order Tracking</a></li>
                    <li><a href="${pageContext.request.contextPath}/wishlist">Wishlist</a></li>
                </ul>
            </div>

            <div class="col-6 col-md-3">
                <h6 class="text-uppercase fw-bold text-light mb-3 small">RTU Syllabus Modules</h6>
                <ul class="list-unstyled small d-flex flex-column gap-2">
                    <li><a href="${pageContext.request.contextPath}/jsp-demo.jsp">JSP Architecture &amp; Lifecycle</a></li>
                    <li><a href="${pageContext.request.contextPath}/jstl-sql-demo.jsp">JSTL SQL Tag Library</a></li>
                    <li><a href="${pageContext.request.contextPath}/jstl-xml-demo.jsp">JSTL XML Tag Library</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin-login.jsp">Swing &amp; Admin Dashboard</a></li>
                </ul>
            </div>

            <div class="col-12 col-md-3">
                <h6 class="text-uppercase fw-bold text-light mb-3 small">Architecture Stack</h6>
                <div class="d-flex flex-wrap gap-1">
                    <span class="badge bg-secondary">Jakarta Servlet 6.1</span>
                    <span class="badge bg-secondary">Tomcat 11</span>
                    <span class="badge bg-secondary">MySQL 8</span>
                    <span class="badge bg-secondary">JDBC Pool</span>
                    <span class="badge bg-secondary">Java Swing MVC</span>
                    <span class="badge bg-secondary">java.net Sockets</span>
                    <span class="badge bg-secondary">Java RMI</span>
                    <span class="badge bg-secondary">JSTL / EL</span>
                </div>
                <p class="text-secondary small mt-3">
                    Active Sessions: <span class="badge bg-primary">${applicationScope.activeSessionsCount != null ? applicationScope.activeSessionsCount : 1}</span>
                </p>
            </div>
        </div>

        <hr class="border-secondary my-4">

        <div class="d-flex flex-column flex-md-row justify-content-between align-items-center small text-secondary">
            <div>&copy; 2026 ShopSphere Enterprise. Built for RTU Advanced Java Practical Examination.</div>
            <div class="mt-2 mt-md-0">
                <span class="text-light">Connected Container:</span> Apache Tomcat 11 | Java SE 26
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap 5 Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<!-- ShopSphere Custom Client Script -->
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
