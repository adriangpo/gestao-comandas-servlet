<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Dashboard"/>
    <jsp:param name="active" value="dashboard"/>
</jsp:include>

<div class="page-header">
    <h1>Mesas</h1>
</div>

<c:choose>
    <c:when test="${empty mesas}">
        <div class="empty-state">
            <span class="empty-icon">&#127834;</span>
            <p>Nenhuma mesa cadastrada</p>
            <span class="empty-hint">Cadastre mesas para começar a gerenciar seus pedidos</span>
            <a href="${pageContext.request.contextPath}/mesas?action=form" class="btn btn-primary">Cadastrar Mesa</a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-grid">
            <c:forEach var="mesa" items="${mesas}" varStatus="status">
                <a href="${pageContext.request.contextPath}/pedidos?mesaId=${mesa.id}"
                   class="table-card ${mesa.ocupada ? 'ocupada' : ''}">
                    <div class="table-id">Mesa ${mesa.identificador}</div>
                    <div class="table-info">Capacidade: ${mesa.capacidade}</div>
                    <c:if test="${mesa.ocupada}">
                        <div class="table-info">${mesa.quantidadePessoas} pessoa(s)</div>
                    </c:if>
                    <span class="badge ${mesa.ocupada ? 'badge-ocupada' : 'badge-free'}">
                        ${mesa.ocupada ? 'Ocupada' : 'Livre'}
                    </span>
                </a>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="common/footer.jsp"/>
