<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${empty produto ? 'Novo Produto' : 'Editar Produto'}"/>
    <jsp:param name="active" value="produtos"/>
</jsp:include>

<div class="page-header">
    <h1>${empty produto ? 'Novo Produto' : 'Editar Produto'}</h1>
</div>

<div class="card" style="max-width: 560px;">
    <form method="post" action="${pageContext.request.contextPath}/produtos">
        <input type="hidden" name="action" value="save">
        <c:if test="${not empty produto}">
            <input type="hidden" name="id" value="${produto.id}">
        </c:if>

        <div class="form-group">
            <label for="nome">Nome</label>
            <input type="text" id="nome" name="nome" class="form-control"
                   value="${produto.nome}" placeholder="Nome do produto" required>
        </div>

        <div class="form-group">
            <label for="preco">Preço (R$)</label>
            <input type="number" id="preco" name="preco" class="form-control"
                   step="0.01" min="0" value="${empty produto ? '' : produto.preco}" placeholder="0.00" required>
        </div>

        <div class="form-group">
            <label for="descricao">Descrição</label>
            <textarea id="descricao" name="descricao" class="form-control"
                      placeholder="Descrição do produto (opcional)">${produto.descricao}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a href="${pageContext.request.contextPath}/produtos" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>

<jsp:include page="../common/footer.jsp"/>
