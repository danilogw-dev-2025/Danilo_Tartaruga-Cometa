<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Entrega" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="util.DataUtils" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Entregas</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        table { border-collapse: collapse; margin-top: 20px; width: 100%; max-width: 1400px; font-size: 0.9em; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background-color: #f2f2f2; }
        tr:nth-child(even) { background-color: #fafafa; }
        a { text-decoration: none; color: #0066cc; }
        .erro-box { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 20px; }
        .bloqueado { color: #999; font-size: 0.9em; font-style: italic; cursor: not-allowed; }
    </style>
</head>
<body>

<h1>Lista de Entregas</h1>
<a href="entrega-servlet?action=novo">Cadastrar nova entrega</a>
<br><br>
<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>

<%
    String msgErro = (String) request.getAttribute("erroMsg");
    if (msgErro != null && !msgErro.isEmpty()) {
%>
    <div class="erro-box"><strong>Atenção:</strong> <%= msgErro %></div>
<%
    }
%>

<%
    List<Entrega> lista = (List<Entrega>) request.getAttribute("listaEntregas");
%>

<table>
    <tr>
        <th>ID</th>
        <th>Rastreio</th>
        <th>Remetente</th>
        <th>Destinatário</th>
        <th>Produto</th>
        <th>Qtd.</th> <th>Frete</th>
        <th>Valor Final</th>
        <th>Envio</th>
        <th>Entrega</th>
        <th>Transp.</th>
        <th>Status</th>
        <th>Editar</th>
        <th>Excluir</th>
    </tr>

    <% if (lista != null && !lista.isEmpty()) {
           for (Entrega e : lista) {
               boolean isTravado = "REALIZADA".equals(e.getStatus()) || "CANCELADA".equals(e.getStatus());
    %>
               <tr>
                   <td><%= e.getIdEntrega() %></td>
                   <td><%= e.getCodigoPedido() %></td>
                   <td><%= e.getNomeRemetente() %></td>
                   <td><%= e.getNomeDestinatario() %></td>
                   <td><%= e.getNomeProduto() %></td>

                   <td><%= e.getQuantidadeProduto() %></td>

                   <td>R$ <%= e.getValorFrete() %></td>

                   <td style="font-weight: bold; background-color: #e8f5e9;">
                       R$ <%= e.getValorFinal() %>
                   </td>

                    <td><%= DataUtils.formatarDataBrasileira(e.getDataEnvio()) %></td>
                    <td><%= DataUtils.formatarDataBrasileira(e.getDataEntrega()) %></td>

                  <td><%= e.getTransportadora() %></td>

                   <td style="font-weight: bold; color: <%= "REALIZADA".equals(e.getStatus()) ? "green" : ("CANCELADA".equals(e.getStatus()) ? "red" : "orange") %>;">
                       <%= e.getStatus() %>
                   </td>

                   <td>
                       <% if (isTravado) { %>
                           <span class="bloqueado">Bloqueado</span>
                       <% } else { %>
                           <a href="<%= request.getContextPath() %>/entrega-servlet?action=editar&idEntrega=<%= e.getIdEntrega() %>">Editar</a>
                       <% } %>
                   </td>

                   <td>
                       <% if ("REALIZADA".equals(e.getStatus())) { %>
                           <span class="bloqueado">Bloqueado</span>
                       <% } else { %>
                           <a href="<%= request.getContextPath() %>/entrega-servlet?action=delete&idEntrega=<%= e.getIdEntrega() %>"
                              onclick="return confirm('Deseja realmente excluir?');">
                              Excluir
                           </a>
                       <% } %>
                   </td>
               </tr>
    <%     }
       } else { %>
           <tr>
               <td colspan="14">Nenhuma entrega cadastrada.</td>
           </tr>
    <% } %>
</table>

</body>
</html>