<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Produtos"/>
    <jsp:param name="active" value="produtos"/>
</jsp:include>

<div class="page-header">
    <h1>Produtos</h1>
    <a href="${pageContext.request.contextPath}/produtos?action=form" class="btn btn-primary">Novo Produto</a>
</div>

<c:choose>
    <c:when test="${empty produtos}">
        <div class="empty-state">
            <span class="empty-icon">&#127859;</span>
            <p>Nenhum produto cadastrado</p>
            <span class="empty-hint">Adicione pratos e bebidas ao seu cardápio</span>
            <a href="${pageContext.request.contextPath}/produtos?action=form" class="btn btn-primary">Novo Produto</a>
        </div>
    </c:when>
    <c:otherwise>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Preço</th>
                    <th>Descrição</th>
                    <th>Status</th>
                    <th style="text-align: right">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="produto" items="${produtos}">
                    <tr class="${!produto.ativo ? 'inactive-row' : ''}">
                        <td>${produto.nome}</td>
                        <td>R$ <fmt:formatNumber value="${produto.preco}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td>${produto.descricao}</td>
                        <td>
                            <span class="badge ${produto.ativo ? 'badge-active' : 'badge-inactive'}">
                                ${produto.ativo ? 'Ativo' : 'Inativo'}
                            </span>
                        </td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/produtos?action=form&id=${produto.id}" class="btn btn-ghost btn-sm">Editar</a>
                            <c:choose>
                                <c:when test="${produto.ativo}">
                                    <form method="post" action="${pageContext.request.contextPath}/produtos" style="display:inline">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${produto.id}">
                                        <button type="submit" class="btn btn-danger btn-sm">Desativar</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <form method="post" action="${pageContext.request.contextPath}/produtos" style="display:inline">
                                        <input type="hidden" name="action" value="reactivate">
                                        <input type="hidden" name="id" value="${produto.id}">
                                        <button type="submit" class="btn btn-success btn-sm">Reativar</button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>

<jsp:include page="../common/footer.jsp"/>
