<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Produto" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Produtos</title>
    <style>
        body { font-family: Arial, sans-serif; }
        h1 { color: #333; }
        table { border-collapse: collapse; margin-top: 20px; width: 100%; max-width: 900px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; }
        th { background-color: #f2f2f2; text-align: left; }
        tr:nth-child(even) { background-color: #fafafa; }
        a { text-decoration: none; color: #0066cc; }
    </style>
</head>
<body>

<h1>Lista de Produtos</h1>

<a href="novo-produto">Cadastrar novo Produto</a>
<br><br>
<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>

<%
    List<Produto> lista = (List<Produto>) request.getAttribute("listarProduto");
%>

<table>
    <tr>
        <th>ID</th>
        <th>Código Produto</th>
        <th>Nome do Produto</th>
        <th>Descrição</th>
        <th>Preço</th>
        <th>Quantidade</th>
    </tr>

    <% if (lista != null && !lista.isEmpty()) {
           for (Produto c : lista) { %>
        <tr>
            <td><%= c.getIdProduto() %></td>
            <td><%= c.getCodigoProduto() %></td>
            <td><%= c.getNomeProduto() %></td>
            <td><%= c.getDescricao() %></td>
            <td><%= c.getPreco() %></td>
            <td><%= c.getQuantidade() %></td>
        </tr>
    <%   }
       } else { %>
        <tr>
            <td colspan="6">Nenhum Produto registrado.</td>
        </tr>
    <% } %>
</table>

</body>
</html>
