<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="x" uri="jakarta.tags.xml" %>

<c:set var="pageTitle" value="RTU Syllabus Demo: JSTL XML Tags - ShopSphere" scope="request"/>
<jsp:include page="/fragments/header.jsp" />
<jsp:include page="/fragments/navbar.jsp" />

<main class="py-5">
    <div class="container">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">JSTL XML Demonstration</li>
            </ol>
        </nav>

        <div class="card border-0 rounded-4 shadow-sm bg-white p-4 p-md-5">
            <div class="d-flex align-items-center gap-3 mb-4">
                <div class="rounded-circle bg-warning-subtle text-warning-emphasis p-3 fs-3">
                    <i class="bi bi-filetype-xml"></i>
                </div>
                <div>
                    <h3 class="fw-extrabold text-dark m-0">RTU Syllabus: JSTL XML Tag Library</h3>
                    <small class="text-muted">Demonstration of <code>&lt;x:parse&gt;</code>, <code>&lt;x:out&gt;</code>, and XPath expressions</small>
                </div>
            </div>

            <!-- Inlined XML Feed for Demonstration -->
            <c:set var="catalogXml">
                <catalog>
                    <item id="101">
                        <name>ShopSphere ProBook 15 G4</name>
                        <category>Electronics</category>
                        <price>74999.00</price>
                        <status>InStock</status>
                    </item>
                    <item id="102">
                        <name>Aura ANC Wireless Headphones</name>
                        <category>Electronics</category>
                        <price>6499.00</price>
                        <status>InStock</status>
                    </item>
                    <item id="103">
                        <name>Classic Oxford Slim-Fit Shirt</name>
                        <category>Fashion</category>
                        <price>1799.00</price>
                        <status>Limited</status>
                    </item>
                </catalog>
            </c:set>

            <x:parse xml="${catalogXml}" var="parsedDoc" />

            <div class="row g-4">
                <div class="col-lg-6">
                    <h6 class="fw-bold text-dark mb-2">Raw XML Document Feed:</h6>
                    <pre class="bg-dark text-success p-3 rounded-4 small"><code>&lt;catalog&gt;
  &lt;item id="101"&gt;
    &lt;name&gt;ShopSphere ProBook 15 G4&lt;/name&gt;
    &lt;category&gt;Electronics&lt;/category&gt;
    &lt;price&gt;74999.00&lt;/price&gt;
  &lt;/item&gt;
  ...
&lt;/catalog&gt;</code></pre>
                </div>
                <div class="col-lg-6">
                    <h6 class="fw-bold text-dark mb-2">Rendered Output via XPath (<code>&lt;x:forEach select="$parsedDoc/catalog/item"&gt;</code>):</h6>
                    <div class="table-responsive">
                        <table class="table table-bordered table-striped small">
                            <thead class="table-light">
                                <tr>
                                    <th>Item ID</th>
                                    <th>Name</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                </tr>
                            </thead>
                            <tbody>
                                <x:forEach select="$parsedDoc/catalog/item">
                                    <tr>
                                        <td><x:out select="@id" /></td>
                                        <td><strong><x:out select="name" /></strong></td>
                                        <td><span class="badge bg-light text-dark border"><x:out select="category" /></span></td>
                                        <td>₹<x:out select="price" /></td>
                                    </tr>
                                </x:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/fragments/footer.jsp" />
