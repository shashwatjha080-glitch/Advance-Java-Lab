<%@ tag description="Product Star Rating Component" pageEncoding="UTF-8"%>
<%@ attribute name="rating" required="true" type="java.lang.Double" description="Rating value between 1 and 5"%>
<%@ attribute name="showNumber" required="false" type="java.lang.Boolean" description="Whether to show numeric rating"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<span class="text-warning d-inline-flex align-items-center gap-1">
    <c:forEach var="i" begin="1" end="5">
        <c:choose>
            <c:when test="${rating >= i}">
                <i class="bi bi-star-fill"></i>
            </c:when>
            <c:when test="${rating >= (i - 0.5)}">
                <i class="bi bi-star-half"></i>
            </c:when>
            <c:otherwise>
                <i class="bi bi-star"></i>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${showNumber}">
        <span class="text-muted small fw-semibold ms-1">(${rating})</span>
    </c:if>
</span>
