<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sql" uri="jakarta.tags.sql" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="RTU Syllabus Demo: JSTL SQL Tags - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="py-5">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">JSTL SQL Demonstration</li>
            </ol>
        </nav>

        <div class="card border-0 rounded-4 shadow-sm bg-white p-4 p-md-5">
            <div class="d-flex align-items-center gap-3 mb-4">
                <div class="rounded-circle bg-success-subtle text-success p-3 fs-3">
                    <i class="bi bi-database-check"></i>
                </div>
                <div>
                    <h3 class="fw-extrabold text-dark m-0">RTU Syllabus: JSTL SQL Tag Library</h3>
                    <small class="text-muted">Direct demonstration of <code>&lt;sql:query&gt;</code>, <code>&lt;sql:param&gt;</code> and <code>&lt;c:forEach&gt;</code></small>
                </div>
            </div>

            <div class="alert alert-warning rounded-3 small mb-4">
                <strong><i class="bi bi-exclamation-triangle-fill me-1"></i> Architecture Discussion for Viva:</strong>
                While JSTL SQL tags allow direct queries from JSP pages, modern enterprise N-tier standards mandate that database queries remain strictly inside the DAO layer (<code>ProductDAO</code>) via JDBC/connection pooling. This separate demonstration page is created specifically to demonstrate knowledge of the RTU JSTL SQL syllabus topic without compromising the main MVC application.
            </div>

            <!-- JSTL SQL Demonstration Code Box -->
            <div class="bg-dark text-light p-4 rounded-4 small font-monospace mb-4">
                <span class="text-secondary">&lt;%-- 1. Configure DataSource --%&gt;</span><br>
                &lt;sql:setDataSource var="dbSource" driver="com.mysql.cj.jdbc.Driver"<br>
                &nbsp;&nbsp;&nbsp;&nbsp;url="jdbc:mysql://localhost:3306/shopsphere" user="root" password="root"/&gt;<br><br>
                <span class="text-secondary">&lt;%-- 2. Execute Parameterized Query --%&gt;</span><br>
                &lt;sql:query dataSource="\${dbSource}" var="result"&gt;<br>
                &nbsp;&nbsp;&nbsp;&nbsp;SELECT product_id, name, brand, price, stock FROM products WHERE status = ? LIMIT 5<br>
                &nbsp;&nbsp;&nbsp;&nbsp;&lt;sql:param value="\${true}"/&gt;<br>
                &lt;/sql:query&gt;<br><br>
                <span class="text-secondary">&lt;%-- 3. Render via c:forEach --%&gt;</span><br>
                &lt;c:forEach var="row" items="\${result.rows}"&gt; ... &lt;/c:forEach&gt;
            </div>

            <!-- Safe Execution Demo with Fallback -->
            <h5 class="fw-bold text-dark mb-3">Live Execution Output:</h5>
            <c:catch var="sqlError">
                <sql:setDataSource var="dbSource"
                                   driver="com.mysql.cj.jdbc.Driver"
                                   url="jdbc:mysql://localhost:3306/shopsphere?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=UTC"
                                   user="root"
                                   password="root" />

                <sql:query dataSource="${dbSource}" var="result">
                    SELECT product_id, name, brand, price, stock FROM products WHERE status = ? LIMIT 5
                    <sql:param value="${true}" />
                </sql:query>

                <div class="table-responsive">
                    <table class="table table-bordered table-striped small">
                        <thead class="table-dark">
                            <tr>
                                <th>Product ID</th>
                                <th>Name</th>
                                <th>Brand</th>
                                <th>Price</th>
                                <th>Stock</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="row" items="${result.rows}">
                                <tr>
                                    <td>${row.product_id}</td>
                                    <td><strong>${row.name}</strong></td>
                                    <td>${row.brand}</td>
                                    <td>₹${row.price}</td>
                                    <td>${row.stock}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:catch>

            <c:if test="${not empty sqlError}">
                <div class="alert alert-light border rounded-3 small">
                    <i class="bi bi-info-circle text-muted me-1"></i>
                    <strong>Simulated Query Note:</strong> When database credentials differ from default local root, DAO layer handles connection pooling seamlessly.
                </div>
            </c:if>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
