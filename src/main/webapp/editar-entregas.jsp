<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Cliente" %>
<%@ page import="Model.Produto" %>
<%@ page import="Model.Entrega" %>

<%
    // Recupera os objetos enviados pelo Servlet
    List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
    List<Produto> listaProdutos = (List<Produto>) request.getAttribute("listaProdutos");
    Entrega entrega = (Entrega) request.getAttribute("entrega");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Entrega</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input, select { padding: 5px; width: 300px; margin-bottom: 5px; }
        button { margin-top: 15px; padding: 10px 20px; cursor: pointer; background-color: #007bff; color: white; border: none; }
        button:hover { background-color: #0056b3; }
        a { text-decoration: none; color: #0066cc; }
    </style>
</head>
<body>

<h1>Editar Entrega</h1>

<a href="entrega-servlet">Voltar para lista de Entregas</a>
<br><br>

<form method="post" action="entrega-servlet">

    <input type="hidden" name="idEntrega" value="${entrega.idEntrega}">

    <label for="id_cliente">Cliente:</label>
    <select id="id_cliente" name="id_cliente" required>
        <option value="">-- Selecione --</option>
        <% if (listaClientes != null) {
               for (Cliente c : listaClientes) {
                   boolean isSelected = (entrega.getIdCliente() != null && entrega.getIdCliente().equals(c.getIdCliente()));
        %>
               <option value="<%= c.getIdCliente() %>" <%= isSelected ? "selected" : "" %>>
                   <%= c.getNome() %> (Doc: <%= c.getDocumento() %>)
               </option>
        <%     }
           } %>
    </select>

    <label for="id_produto">Produto:</label>
    <select id="id_produto" name="id_produto" required>
        <option value="">-- Selecione --</option>
        <% if (listaProdutos != null) {
               for (Produto p : listaProdutos) {
                   boolean isSelected = (entrega.getIdProduto() != null && entrega.getIdProduto().equals(p.getIdProduto()));
        %>
               <option value="<%= p.getIdProduto() %>" <%= isSelected ? "selected" : "" %>>
                   <%= p.getNomeProduto() %>
               </option>
        <%     }
           } %>
    </select>

    <label for="status">Status da Entrega:</label>
    <select id="status" name="status" required>
        <%
           // Pega o status atual para saber qual marcar como selected
           String st = (entrega != null && entrega.getStatus() != null) ? entrega.getStatus() : "";
        %>
        <option value="PENDENTE" <%= "PENDENTE".equals(st) ? "selected" : "" %>>PENDENTE</option>
        <option value="REALIZADA" <%= "REALIZADA".equals(st) ? "selected" : "" %>>REALIZADA</option>
        <option value="CANCELADA" <%= "CANCELADA".equals(st) ? "selected" : "" %>>CANCELADA</option>
    </select>

    <label for="transportadora">Transportadora:</label>
    <input type="text" id="transportadora" name="transportadora" value="${entrega.transportadora}" required />

    <label for="codigo_pedido">Codigo do pedido:</label>
    <input type="text" id="codigo_pedido" name="codigo_pedido" value="${entrega.codigoPedido}" required />

    <label for="data_envio">Data de Envio:</label>
    <input type="date" id="data_envio" name="data_envio" value="${entrega.dataEnvio}" required />

    <label for="data_entrega">Data de Entrega:</label>
    <input type="date" id="data_entrega" name="data_entrega" value="${entrega.dataEntrega}" required />

    <label for="valor_frete">Valor do Frete (R$):</label>
    <input type="number" id="valor_frete" name="valor_frete" min="0.01" step="0.01" placeholder="R$ 0,00" value="${entrega.valorFrete}" required />

    <br>
    <button type="submit">Salvar Alterações</button>
</form>

</body>
</html>