<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Cliente" %>
<%@ page import="Model.Produto" %>

<%
    List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
    List<Produto> listaProdutos = (List<Produto>) request.getAttribute("listaProdutos");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulário de Entrega</title>
</head>
<body>
<h1>Cadastro de Entrega</h1>

<form method="post" action="entrega-servlet">

    <!-- SELECT de CLIENTE -->
        <label for="id_cliente">Cliente:</label>
        <select id="id_cliente" name="id_cliente">
            <option value="">-- Selecione --</option>
            <% if (listaClientes != null) {
                   for (Cliente c : listaClientes) { %>
                       <option value="<%= c.getIdCliente() %>">
                           <%= c.getIdCliente() %> - <%= c.getNome() %>
                       </option>
            <%     }
               } %>
        </select>
        <br><br>

        <!-- SELECT de PRODUTO -->
            <label for="id_produto">Produto:</label>
            <select id="id_produto" name="id_produto">
                <option value="">-- Selecione --</option>
                <% if (listaProdutos != null) {
                       for (Produto p : listaProdutos) { %>
                           <option value="<%= p.getIdProduto() %>">
                               <%= p.getIdProduto() %> - <%= p.getNomeProduto() %>
                           </option>
                <%     }
                   } %>
            </select>
            <br><br>

    <label for="transportadora">Transportadora:</label>
        <input type="text" id="transportadora" name="transportadora" />
    <br><br>

    <label for="codigo_pedido">Codigo do pedido:</label>
        <input type="text" id="codigo_pedido" name="codigo_pedido" />
     <br><br>

      <label for="data_envio">Data de Envio:</label>
             <input type="date" id="data_envio" name="data_envio" />
      <br><br>

      <label for="data_entrega">Data de Entrega:</label>
              <input type="date" id="data_entrega" name="data_entrega" />
      <br><br>

      <label for="valor_frete">Valor do Frete:</label>
               <input type="number" id="valor_frete" name="valor_frete" min= "0.01" step="0.01" placeholder="R$ 0,00" />
      <br><br>
        <br><br>
        <input type="submit" value="Enviar" />
</form>

</body>
</html>
