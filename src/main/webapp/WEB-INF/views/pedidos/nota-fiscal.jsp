<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Nota Fiscal"/>
    <jsp:param name="active" value="dashboard"/>
</jsp:include>

<div class="page-header no-print">
    <h1>Nota Fiscal</h1>
    <div style="display: flex; gap: 0.75rem;">
        <button onclick="window.print()" class="btn btn-primary">Imprimir</button>
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Voltar ao Dashboard</a>
    </div>
</div>

<div class="receipt-wrapper">
    <div class="receipt">
        <div class="receipt-header">
            <h2>Gestão de Comandas</h2>
            <span class="receipt-label">Nota Fiscal</span>
        </div>

        <div class="receipt-info">
            <p>
                <span class="info-label">Mesa</span>
                <span>${mesa.identificador}</span>
            </p>
            <jsp:useBean id="now" class="java.util.Date"/>
            <p>
                <span class="info-label">Data</span>
                <span><fmt:formatDate value="${now}" pattern="dd/MM/yyyy HH:mm"/></span>
            </p>
        </div>

        <table class="receipt-table">
            <thead>
                <tr>
                    <th>Item</th>
                    <th class="text-right">Qtd</th>
                    <th class="text-right">Unit.</th>
                    <th class="text-right">Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${items}">
                    <tr>
                        <td>${item.nomeProduto}</td>
                        <td class="text-right">${item.quantidade}</td>
                        <td class="text-right"><fmt:formatNumber value="${item.precoUnitario}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td class="text-right"><fmt:formatNumber value="${item.subtotal}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="receipt-total">
            <span>TOTAL</span>
            <span class="total-value">R$ <fmt:formatNumber value="${total}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
        </div>

        <div class="receipt-footer">
            <p class="thanks">Obrigado pela preferência!</p>
            <p>Gestão de Comandas</p>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp"/>
