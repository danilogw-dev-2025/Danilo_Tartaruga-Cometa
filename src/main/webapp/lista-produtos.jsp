<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Produto" %>
<%@ page import="java.math.BigDecimal" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Produtos</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        table { border-collapse: collapse; margin-top: 20px; width: 100%; max-width: 1000px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #fafafa; }
        a { text-decoration: none; color: #0066cc; }
    </style>
</head>
<body>

<h1>Lista de Produtos</h1>

<a href="form-produto.jsp">Cadastrar novo Produto</a>
<br><br>
<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>

<%
    List<Produto> lista = (List<Produto>) request.getAttribute("listarProduto");
%>

<table>
    <tr>
        <th>Código</th>
        <th>Nome</th>
        <th>Descrição</th>
        <th>Preço Unit.</th>
        <th>Qtd.</th>
        <th>Valor Total (Estoque)</th> <th>Editar</th>
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
                    <a href="<%= request.getContextPath() %>/produto-servlet?action=delete&idProduto=<%= p.getIdProduto() %>"
                       onclick="return confirm('Deseja realmente excluir?');">Excluir</a>
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

</body>
</html>