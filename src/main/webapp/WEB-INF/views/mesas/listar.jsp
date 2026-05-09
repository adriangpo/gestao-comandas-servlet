<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Mesas"/>
    <jsp:param name="active" value="mesas"/>
</jsp:include>

<div class="page-header">
    <h1>Mesas</h1>
    <a href="${pageContext.request.contextPath}/mesas?action=form" class="btn btn-primary">Nova Mesa</a>
</div>

<c:choose>
    <c:when test="${empty mesas}">
        <div class="empty-state">
            <span class="empty-icon">&#129383;</span>
            <p>Nenhuma mesa cadastrada</p>
            <span class="empty-hint">Configure as mesas do seu estabelecimento</span>
            <a href="${pageContext.request.contextPath}/mesas?action=form" class="btn btn-primary">Nova Mesa</a>
        </div>
    </c:when>
    <c:otherwise>
        <table class="data-table" id="mesas-table">
            <thead>
                <tr>
                    <th style="width: 32px;"></th>
                    <th>Identificador</th>
                    <th>Capacidade</th>
                    <th>Status</th>
                    <th style="text-align: right">Ações</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="mesa" items="${mesas}">
                    <tr data-id="${mesa.id}" draggable="true">
                        <td class="drag-handle">
                            <svg width="10" height="16" viewBox="0 0 10 16" fill="currentColor">
                                <circle cx="2" cy="2" r="1.5"/><circle cx="8" cy="2" r="1.5"/>
                                <circle cx="2" cy="8" r="1.5"/><circle cx="8" cy="8" r="1.5"/>
                                <circle cx="2" cy="14" r="1.5"/><circle cx="8" cy="14" r="1.5"/>
                            </svg>
                        </td>
                        <td>Mesa ${mesa.identificador}</td>
                        <td>${mesa.capacidade} pessoas</td>
                        <td>
                            <span class="badge ${mesa.ocupada ? 'badge-ocupada' : 'badge-free'}">
                                ${mesa.ocupada ? 'Ocupada' : 'Livre'}
                            </span>
                        </td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/mesas?action=form&id=${mesa.id}" class="btn btn-ghost btn-sm">Editar</a>
                            <c:if test="${!mesa.ocupada}">
                                <form method="post" action="${pageContext.request.contextPath}/mesas" style="display:inline">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${mesa.id}">
                                    <button type="button" class="btn btn-danger btn-sm"
                                            onclick="confirmSubmit(this.closest('form'), 'Excluir Mesa', 'Tem certeza que deseja excluir esta mesa?')">Excluir</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <script>
        (function() {
            var tbody = document.querySelector('#mesas-table tbody');
            if (!tbody) return;

            var dragRow = null;
            var canDrag = false;

            tbody.querySelectorAll('.drag-handle').forEach(function(handle) {
                handle.addEventListener('mousedown', function() { canDrag = true; });
            });

            document.addEventListener('mouseup', function() { canDrag = false; });

            tbody.querySelectorAll('tr').forEach(function(row) {
                row.addEventListener('dragstart', function(event) {
                    if (!canDrag) { event.preventDefault(); return; }
                    dragRow = this;
                    this.classList.add('dragging');
                    event.dataTransfer.effectAllowed = 'move';
                    event.dataTransfer.setData('text/plain', '');
                });

                row.addEventListener('dragend', function() {
                    this.classList.remove('dragging');
                    if (dragRow) saveOrder();
                    dragRow = null;
                });

                row.addEventListener('dragover', function(event) {
                    event.preventDefault();
                    if (!dragRow || this === dragRow) return;
                    var rect = this.getBoundingClientRect();
                    var midY = rect.top + rect.height / 2;
                    if (event.clientY < midY) {
                        tbody.insertBefore(dragRow, this);
                    } else {
                        tbody.insertBefore(dragRow, this.nextSibling);
                    }
                });
            });

            function saveOrder() {
                var rows = tbody.querySelectorAll('tr');
                var formData = new URLSearchParams();
                formData.append('action', 'reorder');
                rows.forEach(function(row) {
                    formData.append('ids', row.dataset.id);
                });
                fetch(contextPath + '/mesas', { method: 'POST', body: formData });
            }
        })();
        </script>
    </c:otherwise>
</c:choose>

<jsp:include page="../common/footer.jsp"/>
