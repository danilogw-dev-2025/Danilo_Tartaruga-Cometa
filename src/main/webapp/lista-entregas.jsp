<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Entrega" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Entregas</title>
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

<h1>Lista de Entregas</h1>
<a href="nova-entrega">Cadastrar nova entrega</a>
<br><br>
<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>
<%
    List<Entrega> lista = (List<Entrega>) request.getAttribute("listaEntregas");
%>

<table>
    <tr>
        <th>ID</th>
        <th>Código Pedido</th>
        <th>ID Cliente</th>
        <th>ID Produto</th>
        <th>Data Envio</th>
        <th>Data Entrega</th>
        <th>Transportadora</th>
        <th>Valor Frete</th>
    </tr>

    <% if (lista != null && !lista.isEmpty()) {
           for (Entrega e : lista) { %>
               <tr>
                   <td><%= e.getIdEntrega() %></td>
                   <td><%= e.getCodigoPedido() %></td>
                   <td><%= e.getIdCliente() %></td>
                   <td><%= e.getIdProduto() %></td>
                   <td><%= e.getDataEnvio() %></td>
                   <td><%= e.getDataEntrega() %></td>
                   <td><%= e.getTransportadora() %></td>
                   <td><%= e.getValorFrete() %></td>
               </tr>
    <%     }
       } else { %>
           <tr>
               <td colspan="8">Nenhuma entrega cadastrada.</td>
           </tr>
    <% } %>
</table>

</body>
</html>
