<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${empty mesa ? 'Nova Mesa' : 'Editar Mesa'}"/>
    <jsp:param name="active" value="mesas"/>
</jsp:include>

<div class="page-header">
    <h1>${empty mesa ? 'Nova Mesa' : 'Editar Mesa'}</h1>
</div>

<div class="card" style="max-width: 560px;">
    <form method="post" action="${pageContext.request.contextPath}/mesas">
        <input type="hidden" name="action" value="save">
        <c:if test="${not empty mesa}">
            <input type="hidden" name="id" value="${mesa.id}">
        </c:if>

        <div class="form-group">
            <label for="identificador">Identificador</label>
            <input type="text" id="identificador" name="identificador" class="form-control"
                   value="${mesa.identificador}" placeholder="Ex: 1, 2A, VIP1" required>
        </div>

        <div class="form-group">
            <label for="capacidade">Capacidade</label>
            <input type="number" id="capacidade" name="capacidade" class="form-control"
                   min="1" value="${mesa.capacidade}" placeholder="Número de lugares" required>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a href="${pageContext.request.contextPath}/mesas" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>

<jsp:include page="../common/footer.jsp"/>
