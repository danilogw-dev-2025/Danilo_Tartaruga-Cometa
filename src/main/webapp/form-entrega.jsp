<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Cliente" %>
<%@ page import="Model.Produto" %>
<%@ page import="Model.Entrega" %>

<%
    List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
    List<Produto> listaProdutos = (List<Produto>) request.getAttribute("listaProdutos");
    Entrega entrega = (Entrega) request.getAttribute("entrega");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulário de Entrega</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input, select { padding: 8px; width: 100%; max-width: 400px; margin-bottom: 5px; border: 1px solid #ccc; border-radius: 4px; }
        button { margin-top: 15px; padding: 10px 20px; cursor: pointer; background-color: #28a745; color: white; border: none; border-radius: 4px; font-size: 16px; }
        .erro-box { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 20px; max-width: 400px; }
    </style>
</head>
<body>

<h1>Cadastro de Entrega</h1>
<a href="entrega-servlet">Voltar para lista</a>
<br><br>

<%
    String msgErro = (String) request.getAttribute("erroMsg");
    if (msgErro != null && !msgErro.isEmpty()) {
%>
    <div class="erro-box"><strong>Atenção:</strong> <%= msgErro %></div>
<%
    }
%>

<form method="post" action="entrega-servlet">
    <input type="hidden" name="idEntrega" value="<%= (entrega != null && entrega.getIdEntrega() != null) ? entrega.getIdEntrega() : "" %>" />

    <label for="id_remetente">Remetente (Quem envia):</label>
    <select id="id_remetente" name="id_remetente" required>
        <option value="">-- Selecione --</option>
        <% if (listaClientes != null) {
               for (Cliente c : listaClientes) {
                   boolean isSelected = (entrega != null && entrega.getIdRemetente() != null && entrega.getIdRemetente().equals(c.getIdCliente()));
        %>
               <option value="<%= c.getIdCliente() %>" <%= isSelected ? "selected" : "" %>>
                   <%= c.getNome() %>
               </option>
        <%     }
           } %>
    </select>

    <label for="id_destinatario">Destinatário (Quem recebe):</label>
    <select id="id_destinatario" name="id_destinatario" required>
        <option value="">-- Selecione --</option>
        <% if (listaClientes != null) {
               for (Cliente c : listaClientes) {
                   boolean isSelected = (entrega != null && entrega.getIdDestinatario() != null && entrega.getIdDestinatario().equals(c.getIdCliente()));
        %>
               <option value="<%= c.getIdCliente() %>" <%= isSelected ? "selected" : "" %>>
                   <%= c.getNome() %>
               </option>
        <%     }
           } %>
    </select>

    <label for="id_produto">Produto:</label>
    <select id="id_produto" name="id_produto" required>
        <option value="">-- Selecione --</option>
        <% if (listaProdutos != null) {
               for (Produto p : listaProdutos) {
                   boolean isSelected = (entrega != null && entrega.getIdProduto() != null && entrega.getIdProduto().equals(p.getIdProduto()));
        %>
               <option value="<%= p.getIdProduto() %>" <%= isSelected ? "selected" : "" %>>
                   <%= p.getNomeProduto() %>
               </option>
        <%     }
           } %>
    </select>

    <label for="codigo_pedido">Codigo do pedido:</label>
    <input type="text" id="codigo_pedido" name="codigo_pedido" value="<%= (entrega != null && entrega.getCodigoPedido() != null) ? entrega.getCodigoPedido() : "" %>" required />

    <label for="transportadora">Transportadora:</label>
    <input type="text" id="transportadora" name="transportadora" value="<%= (entrega != null && entrega.getTransportadora() != null) ? entrega.getTransportadora() : "" %>" required />

    <label for="data_envio">Data de Envio:</label>
    <input type="date" id="data_envio" name="data_envio" value="<%= (entrega != null && entrega.getDataEnvio() != null) ? entrega.getDataEnvio() : "" %>" required />

    <label for="data_entrega">Data de Entrega:</label>
    <input type="date" id="data_entrega" name="data_entrega" value="<%= (entrega != null && entrega.getDataEntrega() != null) ? entrega.getDataEntrega() : "" %>" required />

    <label for="valor_frete">Valor do Frete (R$):</label>
    <input type="number" id="valor_frete" name="valor_frete" min="0.01" step="0.01" placeholder="0.00" value="<%= (entrega != null && entrega.getValorFrete() != null) ? entrega.getValorFrete() : "" %>" required />

    <label for="status">Status da Entrega:</label>
    <select id="status" name="status" required>
        <% String st = (entrega != null && entrega.getStatus() != null) ? entrega.getStatus() : ""; %>
        <option value="PENDENTE" <%= "PENDENTE".equals(st) ? "selected" : "" %>>PENDENTE</option>
        <option value="REALIZADA" <%= "REALIZADA".equals(st) ? "selected" : "" %>>REALIZADA</option>
        <option value="CANCELADA" <%= "CANCELADA".equals(st) ? "selected" : "" %>>CANCELADA</option>
    </select>

    <br>
    <button type="submit">Salvar Entrega</button>
</form>

</body>
</html>