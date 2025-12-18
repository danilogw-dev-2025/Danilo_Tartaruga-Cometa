<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Produto" %>
<%@ page import="java.math.BigDecimal" %>

<%
    List<Produto> lista = (List<Produto>) request.getAttribute("listarProduto");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Produtos</title>

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
            text-decoration: none; border-radius: 4px; display: inline-block;
        }
        .btn-novo:hover { background-color: #0056b3; }
    </style>
</head>
<body>

<h1>Lista de Produtos</h1>

<a href="form-produto.jsp" class="btn-novo">Cadastrar novo Produto</a>
<br><br>
<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>

<table>
    <tr>
        <th>Código</th>
        <th>Nome</th>
        <th>Descrição</th>
        <th>Preço Unit.</th>
        <th>Qtd.</th>
        <th>Valor Total (Estoque)</th>
        <th>Editar</th>
        <th>Excluir</th>
    </tr>

    <% if (lista != null && !lista.isEmpty()) {
           for (Produto p : lista) {
               BigDecimal total = BigDecimal.ZERO;
               if (p.getPreco() != null) {
                   total = p.getPreco().multiply(new BigDecimal(p.getQuantidade()));
               }
    %>
        <tr>
            <td><%= p.getCodigoProduto() %></td>
            <td><%= p.getNomeProduto() %></td>
            <td><%= p.getDescricao() %></td>
            <td>R$ <%= p.getPreco() %></td>
            <td><%= p.getQuantidade() %></td>

            <td style="font-weight: bold;">R$ <%= total %></td>

            <td>
                <% if (p.isEmUso()) { %>
                    <span style="color: gray; cursor: not-allowed;" title="Produto vinculado a entregas">Bloqueado</span>
                <% } else { %>
                    <a href="<%= request.getContextPath() %>/produto-servlet?action=editar&idProduto=<%= p.getIdProduto() %>">Editar</a>
                <% } %>
            </td>

            <td>
                <% if (p.isEmUso()) { %>
                    <span style="color: #ccc; font-style: italic;">🔒 Em uso</span>
                <% } else { %>
                    <a href="#"
                       class="btn-excluir"
                       data-id="<%= p.getIdProduto() %>"
                       data-nome="<%= p.getNomeProduto() %>"
                       style="color: #cc0000;">
                       Excluir
                    </a>
                <% } %>
            </td>
        </tr>
    <%   }
       } else { %>
        <tr>
            <td colspan="8">Nenhum Produto registrado.</td>
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
            title: 'Excluir produto?',
            text: "Deseja realmente excluir o produto " + nome + "? Esta ação não poderá ser desfeita.",
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
               text: "Produto Deletado da Base de Dados.",
               icon: "success",
               confirmButtonText: "Ok"
              }).then(() => {
                window.location.href = '<%= request.getContextPath() %>/produto-servlet?action=delete&idProduto=' + id;
            });
        }
      });
    });
});
</script>
<%@ include file="alerta_toast.jsp" %>
</body>
</html>