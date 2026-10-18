<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- 
  ========================================================================
  RTU ADVANCED JAVA SYLLABUS DEMONSTRATION: JSP ELEMENTS & LIFECYCLE
  Covers:
    1. JSP Declarations (<%! ... %>)
    2. JSP Scriptlets (<% ... %>)
    3. JSP Expressions (<%= ... %>)
    4. JSP Comments (<%-- ... --%>)
    5. JSP Directives (page, taglib, include)
    6. Expression Language (EL)
  ========================================================================
--%>

<%!
    // 1. JSP Declaration: Defines class-level member variables and methods
    private int pageVisitCounter = 0;

    public String getFormattedGreeting(String username) {
        return "Welcome to RTU Advanced Java Demonstration, " + username + "!";
    }
%>

<%
    // 2. JSP Scriptlet: Executed inside the generated _jspService() method
    pageVisitCounter++;
    String currentServerTime = new java.util.Date().toString();
    String studentName = (session.getAttribute("user") != null) ?
            ((com.shopsphere.model.User) session.getAttribute("user")).getName() : "Guest Student";
    String greeting = getFormattedGreeting(studentName);
    request.setAttribute("demoStudent", studentName);
%>

<c:set var="pageTitle" value="RTU Syllabus Demo: JSP Elements - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="py-5">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">RTU JSP Demonstration</li>
            </ol>
        </nav>

        <div class="card border-0 rounded-4 shadow-sm bg-white p-4 p-md-5">
            <div class="d-flex align-items-center gap-3 mb-4">
                <div class="rounded-circle bg-primary-subtle text-primary p-3 fs-3">
                    <i class="bi bi-mortarboard-fill"></i>
                </div>
                <div>
                    <h3 class="fw-extrabold text-dark m-0">RTU Syllabus: JSP Elements &amp; Lifecycle</h3>
                    <small class="text-muted">Direct practical demonstration of declarations, scriptlets, expressions &amp; EL</small>
                </div>
            </div>

            <!-- Theory explanation -->
            <div class="alert alert-info rounded-3 small mb-4">
                <strong><i class="bi bi-info-circle me-1"></i> Academic Note:</strong>
                In modern enterprise Java applications (N-Tier architecture), business logic is strictly encapsulated in Servlets and DAOs.
                JSP Expression Language (EL) and JSTL are preferred for views. This page explicitly implements classic JSP scriptlet elements to fulfill RTU syllabus viva demonstration criteria.
            </div>

            <div class="row g-4">
                <!-- 1. Declarations -->
                <div class="col-md-6">
                    <div class="card h-100 bg-light border-0 rounded-4 p-4">
                        <h5 class="fw-bold text-primary mb-2">1. JSP Declaration (<code>&lt;%! ... %&gt;</code>)</h5>
                        <p class="small text-muted">Declares static or instance variables and helper methods within the compiled Servlet class body.</p>
                        <div class="bg-dark text-light p-3 rounded-3 small font-monospace mb-3">
&lt;%!
    private int pageVisitCounter = 0;
    public String getFormattedGreeting(String u) {
        return "Welcome, " + u + "!";
    }
%!&gt;
                        </div>
                        <div class="badge bg-primary text-white p-2 text-start">
                            Output from Declaration Method:<br>
                            <strong><%= greeting %></strong>
                        </div>
                    </div>
                </div>

                <!-- 2. Expressions -->
                <div class="col-md-6">
                    <div class="card h-100 bg-light border-0 rounded-4 p-4">
                        <h5 class="fw-bold text-primary mb-2">2. JSP Expression (<code>&lt;%= ... %&gt;</code>)</h5>
                        <p class="small text-muted">Evaluates an expression and writes the result directly to the HTTP output stream (<code>out.print()</code>).</p>
                        <div class="bg-dark text-light p-3 rounded-3 small font-monospace mb-3">
Total Hits (Declaration state): &lt;%= pageVisitCounter %&gt;<br>
Server Time: &lt;%= new java.util.Date() %&gt;
                        </div>
                        <ul class="list-unstyled small mb-0">
                            <li>Page Hit Counter: <span class="badge bg-success"><%= pageVisitCounter %></span></li>
                            <li>Current Server Timestamp: <code><%= currentServerTime %></code></li>
                        </ul>
                    </div>
                </div>

                <!-- 3. Scriptlets -->
                <div class="col-md-6">
                    <div class="card h-100 bg-light border-0 rounded-4 p-4">
                        <h5 class="fw-bold text-primary mb-2">3. JSP Scriptlet (<code>&lt;% ... %&gt;</code>)</h5>
                        <p class="small text-muted">Java statements inserted directly inside the generated Servlet <code>_jspService()</code> method.</p>
                        <div class="bg-dark text-light p-3 rounded-3 small font-monospace mb-3">
&lt;%
    for(int i = 1; i &lt;= 3; i++) {
        out.println("&lt;span class='badge bg-secondary me-1'&gt;Iteration #" + i + "&lt;/span&gt;");
    }
%&gt;
                        </div>
                        <div>
                            <%
                                for(int i = 1; i <= 3; i++) {
                                    out.println("<span class='badge bg-secondary me-1'>Iteration #" + i + "</span>");
                                }
                            %>
                        </div>
                    </div>
                </div>

                <!-- 4. Expression Language (EL) Comparison -->
                <div class="col-md-6">
                    <div class="card h-100 bg-light border-0 rounded-4 p-4">
                        <h5 class="fw-bold text-primary mb-2">4. Expression Language (<code>\${...}</code>)</h5>
                        <p class="small text-muted">Modern declarative syntax providing clean access to scoped attributes without scriptlets.</p>
                        <div class="bg-dark text-light p-3 rounded-3 small font-monospace mb-3">
Student Name: \${requestScope.demoStudent}<br>
App Title: \${applicationScope.appName}
                        </div>
                        <ul class="list-unstyled small mb-0">
                            <li>EL Extracted Student: <strong class="text-primary">${requestScope.demoStudent}</strong></li>
                            <li>EL Extracted App Name: <strong class="text-primary">${applicationScope.appName}</strong></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
