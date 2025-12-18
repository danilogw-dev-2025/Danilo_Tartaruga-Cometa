<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script>
    // 1. Configuração do Toast
    const Toast = Swal.mixin({
        toast: true,
        position: 'bottom-end',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true
    });

    // 2. Verifica os parâmetros da URL
    const urlParams = new URLSearchParams(window.location.search);
    const status = urlParams.get('status');

    if (status === 'sucesso') {
        Toast.fire({
            icon: 'success',
            title: 'Cadastro realizado com sucesso!'
        });
    } else if (status === 'erro') {
        Toast.fire({
            icon: 'error',
            title: 'Erro ao processar a solicitação.'
        });
    }

    // 3. LIMPEZA: Remove o "?status=..." da URL para não repetir o alerta no refresh
    if (status) {
        const novaUrl = window.location.pathname;
        window.history.replaceState({}, document.title, novaUrl);
    }
</script>