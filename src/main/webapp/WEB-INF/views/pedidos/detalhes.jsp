<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Mesa ${mesa.identificador}"/>
    <jsp:param name="active" value="dashboard"/>
</jsp:include>

<div class="page-header">
    <h1>Mesa ${mesa.identificador}</h1>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Voltar ao Dashboard</a>
</div>

<div class="mesa-info">
    <div class="mesa-info-item">
        <div class="label">Mesa</div>
        <div class="value">${mesa.identificador}</div>
    </div>
    <div class="mesa-info-item">
        <div class="label">Capacidade</div>
        <div class="value">${mesa.capacidade}</div>
    </div>
    <div class="mesa-info-item">
        <div class="label">Pessoas</div>
        <div class="value">${mesa.quantidadePessoas}</div>
    </div>
    <div class="mesa-info-item">
        <div class="label">Status</div>
        <div class="value">
            <span class="badge ${ocupada ? 'badge-ocupada' : 'badge-free'}">
                ${ocupada ? 'Ocupada' : 'Livre'}
            </span>
        </div>
    </div>
</div>

<c:if test="${!ocupada}">
    <c:choose>
        <c:when test="${empty produtos}">
            <div class="open-order-card">
                <h2>Abrir Pedido</h2>
                <p>Cadastre ao menos um produto antes de abrir pedidos.</p>
                <a href="${pageContext.request.contextPath}/produtos?action=form" class="btn btn-primary">Cadastrar Produto</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="open-order-card">
                <h2>Abrir Pedido</h2>
                <p>Esta mesa está livre. Informe a quantidade de pessoas para iniciar.</p>
                <form method="post" action="${pageContext.request.contextPath}/pedidos">
                    <input type="hidden" name="action" value="createOrder">
                    <input type="hidden" name="mesaId" value="${mesa.id}">
                    <div class="form-inline">
                        <div class="form-group">
                            <label for="quantidadePessoas">Quantidade de Pessoas</label>
                            <input type="number" id="quantidadePessoas" name="quantidadePessoas"
                                   class="form-control" min="1" max="${mesa.capacidade}" style="width: 160px;" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Abrir Pedido</button>
                    </div>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</c:if>

<c:if test="${ocupada}">
    <c:forEach var="pedido" items="${pedidos}" varStatus="status">
        <div class="order-card">
            <div class="order-header">
                <h2>Pedido #${pedido.id}</h2>
                <span class="text-secondary" style="font-size: 0.8125rem;">
                    <fmt:parseDate value="${pedido.criadoEm}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                </span>
            </div>

            <c:choose>
                <c:when test="${empty pedido.items}">
                    <p class="text-muted" style="padding: 1rem 0;">Nenhum item adicionado.</p>
                </c:when>
                <c:otherwise>
                    <table class="data-table" style="margin-bottom: 0; border: none;">
                        <thead>
                            <tr>
                                <th>Produto</th>
                                <th>Qtd</th>
                                <th>Preço Unit.</th>
                                <th>Subtotal</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${pedido.items}">
                                <tr>
                                    <td>${item.nomeProduto}</td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/pedidos" style="display:inline">
                                            <input type="hidden" name="action" value="updateItemQuantity">
                                            <input type="hidden" name="mesaId" value="${mesa.id}">
                                            <input type="hidden" name="itemId" value="${item.id}">
                                            <input type="number" name="quantidade" value="${item.quantidade}"
                                                   min="1" class="form-control qty-input"
                                                   onchange="this.form.submit()">
                                        </form>
                                    </td>
                                    <td>R$ <fmt:formatNumber value="${item.precoUnitario}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                    <td>R$ <fmt:formatNumber value="${item.subtotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                    <td style="text-align: right;">
                                        <form method="post" action="${pageContext.request.contextPath}/pedidos" style="display:inline">
                                            <input type="hidden" name="action" value="removeItem">
                                            <input type="hidden" name="mesaId" value="${mesa.id}">
                                            <input type="hidden" name="itemId" value="${item.id}">
                                            <button type="button" class="btn btn-danger btn-sm"
                                                    onclick="confirmSubmit(this.closest('form'), 'Remover Item', 'Deseja remover ${item.nomeProduto} do pedido?')">Remover</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>

            <div class="add-item-form">
                <form method="post" action="${pageContext.request.contextPath}/pedidos" class="form-inline">
                    <input type="hidden" name="action" value="addItem">
                    <input type="hidden" name="mesaId" value="${mesa.id}">
                    <input type="hidden" name="pedidoId" value="${pedido.id}">
                    <div class="form-group" style="flex: 1; min-width: 200px;">
                        <label for="produtoId_${pedido.id}">Produto</label>
                        <select id="produtoId_${pedido.id}" name="produtoId" class="form-control" required>
                            <option value="">Selecione um produto...</option>
                            <c:forEach var="produto" items="${produtos}">
                                <option value="${produto.id}">${produto.nome} - R$ <fmt:formatNumber value="${produto.preco}" type="number" minFractionDigits="2" maxFractionDigits="2"/></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="quantidade_${pedido.id}">Qtd</label>
                        <input type="number" id="quantidade_${pedido.id}" name="quantidade"
                               class="form-control" min="1" value="1" style="width: 80px;" required>
                    </div>
                    <button type="submit" class="btn btn-success btn-sm">Adicionar</button>
                </form>
            </div>
        </div>
    </c:forEach>

    <div class="order-actions">
        <form method="post" action="${pageContext.request.contextPath}/pedidos">
            <input type="hidden" name="action" value="pay">
            <input type="hidden" name="mesaId" value="${mesa.id}">
            <button type="button" class="btn btn-success"
                    onclick="confirmSubmit(this.closest('form'), 'Fechar Conta', 'Confirmar pagamento de todos os pedidos?', 'Confirmar Pagamento', 'btn-success')">Fechar Conta</button>
        </form>
        <form method="post" action="${pageContext.request.contextPath}/pedidos">
            <input type="hidden" name="action" value="cancel">
            <input type="hidden" name="mesaId" value="${mesa.id}">
            <button type="button" class="btn btn-danger"
                    onclick="confirmSubmit(this.closest('form'), 'Cancelar Pedidos', 'Tem certeza que deseja cancelar todos os pedidos desta mesa?')">Cancelar Pedidos</button>
        </form>
    </div>
</c:if>

<jsp:include page="../common/footer.jsp"/>
