<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Cliente" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Clientes</title>
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

<h1>Lista de Clientes</h1>

<a href="novo-cliente">Cadastrar novo Cliente</a>
<br><br>

<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>

<%
    List<Cliente> lista = (List<Cliente>) request.getAttribute("listarCliente");
%>

<table>
    <tr>
        <th>ID</th>
        <th>Código Cliente</th>
        <th>Nome</th>
        <th>CPF</th>
        <th>Data de Nascimento</th>
        <th>Email</th>
        <th>Editar</th>
        <th>Excluir</th>
    </tr>

    <% if (lista != null && !lista.isEmpty()) {
           for (Cliente c : lista) { %>
        <tr>
            <td><%= c.getIdCliente() %></td>
            <td><%= c.getCodigoCliente() %></td>
            <td><%= c.getNome() %></td>
            <td><%= c.getCpf() %></td>
            <td><%= c.getDataNascimento() %></td>
            <td><%= c.getEmail() %></td>
            <td>
                <a href="<%= request.getContextPath() %>/cliente-servlet?action=editar&id=<%= c.getIdCliente() %>">Editar</a>
                </td>

                <td>
              <a href="<%= request.getContextPath() %>/cliente-servlet?action=delete&idCliente=<%= c.getIdCliente() %>"
                 onclick="return confirm('Deseja realmente excluir?');">
                 Excluir
              </a>
            </td>

        </tr>
    <%   }
       } else { %>
        <tr>
            <td colspan="8">Nenhum cliente registrado.</td>
        </tr>
    <% } %>
</table>

</body>
</html>
