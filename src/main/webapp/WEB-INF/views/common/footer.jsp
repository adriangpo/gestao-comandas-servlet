<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    </div>

    <div id="modal-overlay" class="modal-overlay" onclick="if(event.target===this)closeModal()">
        <div class="modal">
            <h3 id="modal-title" class="modal-title"></h3>
            <p id="modal-message" class="modal-message"></p>
            <div class="modal-actions">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">Voltar</button>
                <button type="button" id="modal-confirm-btn" class="btn btn-danger">Confirmar</button>
            </div>
        </div>
    </div>

    <script>
        var contextPath = '${pageContext.request.contextPath}';
        var modalCallback = null;

        function openModal(title, message, onConfirm, confirmText, confirmClass) {
            document.getElementById('modal-title').textContent = title;
            document.getElementById('modal-message').textContent = message;
            var btn = document.getElementById('modal-confirm-btn');
            btn.textContent = confirmText || 'Confirmar';
            btn.className = 'btn ' + (confirmClass || 'btn-danger');
            modalCallback = onConfirm;
            document.getElementById('modal-overlay').classList.add('active');
        }

        function closeModal() {
            document.getElementById('modal-overlay').classList.remove('active');
            modalCallback = null;
        }

        document.getElementById('modal-confirm-btn').addEventListener('click', function() {
            if (modalCallback) modalCallback();
            closeModal();
        });

        document.addEventListener('keydown', function(event) {
            if (event.key === 'Escape') closeModal();
        });

        function confirmSubmit(form, title, message, confirmText, confirmClass) {
            openModal(title, message, function() { form.submit(); }, confirmText, confirmClass);
        }
    </script>
</body>
</html>
