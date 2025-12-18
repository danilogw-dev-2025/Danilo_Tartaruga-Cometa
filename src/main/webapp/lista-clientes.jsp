<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Cliente" %>

<%
    List<Cliente> lista = (List<Cliente>) request.getAttribute("listarCliente");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Clientes</title>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        table { border-collapse: collapse; margin-top: 20px; width: 100%; max-width: 1000px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #fafafa; }
        a { text-decoration: none; color: #0066cc; }
        .btn-novo {
            background-color: #007bff; color: white; padding: 10px 15px;
            text-decoration: none; border-radius: 4px;
        }
        .btn-novo:hover { background-color: #0056b3; }
    </style>
</head>
<body>

<h1>Lista de Clientes</h1>

<a href="form-cliente.jsp" class="btn-novo">Cadastrar novo Cliente</a>
<br><br>

<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>

<table>
    <tr>
        <th>Cód. Cliente</th>
        <th>Nome</th>
        <th>Tipo</th>
        <th>Documento</th>
        <th>Estado</th>
        <th>Cidade</th>
        <th>Rua</th>
        <th>Nº para residência</th>
        <th>Editar</th>
        <th>Excluir</th>
    </tr>

    <% if (lista != null && !lista.isEmpty()) {
           for (Cliente c : lista) { %>
        <tr>

            <td><%= c.getCodigoCliente() %></td>
            <td><%= c.getNome() %></td>

            <td style="font-weight: bold; font-size: 0.9em;">
                <%= c.getTipoPessoa() %>
            </td>

            <td><%= c.getDocumento() %></td>
            <td><%= c.getEstado() %></td>
            <td><%= c.getCidade() %></td>
            <td><%= c.getRua() %></td>
            <td><%= c.getNumeroCasa() %></td>

            <td>
                <a href="<%= request.getContextPath() %>/cliente-servlet?action=editar&id=<%= c.getIdCliente() %>">Editar</a>
            </td>

            <td>
                <% if (c.isBloqueadoParaExclusao()) { %>
                    <span style="color: #999; cursor: not-allowed;" title="Bloqueado: Este cliente possui entregas pendentes">
                        🔒 Bloqueado
                    </span>
                <% } else { %>
                    <a href="#"
                       class="btn-excluir"
                       data-id="<%= c.getIdCliente() %>"
                       data-nome="<%= c.getNome() %>"
                       style="color: #cc0000;">
                       Excluir
                    </a>
                <% } %>
            </td>

        </tr>
    <%   }
       } else { %>
        <tr>
            <td colspan="10">Nenhum cliente registrado.</td>
        </tr>
    <% } %>
</table>

<script>
$(document).ready(function() {
    $('.btn-excluir').on('click', function(e) {
        e.preventDefault();

        const id = $(this).data('id');
        const nome = $(this).data('nome');

        Swal.fire({
            title: 'Confirmar exclusão',
            text: "Deseja realmente excluir o cliente " + nome + "?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Sim, excluir!',
            cancelButtonText: 'Cancelar',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: "Deletado!",
                    text: "Cliente Deletado da Base de Dados.",
                    icon: "success",
                    confirmButtonText: "Ok"
                }).then(() => {
                    window.location.href = '<%= request.getContextPath() %>/cliente-servlet?action=delete&id=' + id;
                });
            }
        });
    });
});
</script>
<%@ include file="alerta_toast.jsp" %>
</body>
</html>