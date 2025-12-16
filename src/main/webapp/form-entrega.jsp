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

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"></script>

    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input, select { padding: 8px; width: 100%; max-width: 400px; margin-bottom: 5px; }
        button { margin-top: 15px; padding: 10px 20px; cursor: pointer; }
        .erro-box { background: #f8d7da; padding: 10px; margin-bottom: 20px; max-width: 400px; }
    </style>
</head>
<body>

<h1>Cadastro de Entrega</h1>
<a href="entrega-servlet">Voltar</a>
<br><br>

<%
    String msgErro = (String) request.getAttribute("erroMsg");
    if (msgErro != null && !msgErro.isEmpty()) {
%>
    <div class="erro-box"><strong>Atenção:</strong> <%= msgErro %></div>
<%
    }
%>

<form method="post" action="entrega-servlet" onsubmit="return validarEntrega()">

    <input type="hidden" name="idEntrega"
           value="<%= (entrega != null && entrega.getIdEntrega() != null) ? entrega.getIdEntrega() : "" %>" />

    <label>Remetente:</label>
    <select name="id_remetente" required>
        <option value="">-- Selecione --</option>
        <% for (Cliente c : listaClientes) { %>
            <option value="<%= c.getIdCliente() %>"
                <%= (entrega != null && c.getIdCliente().equals(entrega.getIdRemetente())) ? "selected" : "" %>>
                <%= c.getNome() %>
            </option>
        <% } %>
    </select>

    <label>Destinatário:</label>
    <select name="id_destinatario" required>
        <option value="">-- Selecione --</option>
        <% for (Cliente c : listaClientes) { %>
            <option value="<%= c.getIdCliente() %>"
                <%= (entrega != null && c.getIdCliente().equals(entrega.getIdDestinatario())) ? "selected" : "" %>>
                <%= c.getNome() %>
            </option>
        <% } %>
    </select>

    <label>Produto:</label>
    <select name="id_produto" required>
        <option value="">-- Selecione --</option>
        <% for (Produto p : listaProdutos) { %>
            <option value="<%= p.getIdProduto() %>"
                <%= (entrega != null && p.getIdProduto().equals(entrega.getIdProduto())) ? "selected" : "" %>>
                <%= p.getNomeProduto() %>
            </option>
        <% } %>
    </select>

    <label>Código do Pedido:</label>
    <input type="text" name="codigo_pedido"
           value="<%= entrega != null ? entrega.getCodigoPedido() : "" %>" required />

    <label>Transportadora:</label>
    <input type="text" name="transportadora"
           value="<%= entrega != null ? entrega.getTransportadora() : "" %>" required />

    <label>Data de Envio:</label>
    <input type="date" id="data_envio" name="data_envio"
           value="<%= entrega != null ? entrega.getDataEnvio() : "" %>" required />
    <button type="button" onclick="limparCampoData('data_envio')">✖</button>

    <label>Previsão de Entrega:</label>
    <input type="date" id="data_entrega" name="data_entrega"
           value="<%= entrega != null ? entrega.getDataEntrega() : "" %>" required />
    <button type="button" onclick="limparCampoData('data_entrega')">✖</button>

    <label>Valor do Frete:</label>
    <input type="text" id="valor_frete" name="valor_frete"
           value="<%= (entrega != null && entrega.getValorFrete() != null) ? entrega.getValorFrete() : "" %>" />

    <label>Status:</label>

    <% if (entrega == null) { %>
        <!-- CRIAÇÃO: Status fixo como PENDENTE -->
        <input type="hidden" name="status" value="PENDENTE" />
        <select disabled>
            <option selected>PENDENTE</option>
        </select>
    <% } else { %>
        <!-- EDIÇÃO: Pode alterar -->
        <select name="status" required>
            <option value="PENDENTE" <%= "PENDENTE".equals(entrega.getStatus()) ? "selected" : "" %>>PENDENTE</option>
            <option value="REALIZADA" <%= "REALIZADA".equals(entrega.getStatus()) ? "selected" : "" %>>REALIZADA</option>
            <option value="CANCELADA" <%= "CANCELADA".equals(entrega.getStatus()) ? "selected" : "" %>>CANCELADA</option>
        </select>
    <% } %>

    <button type="submit">Salvar Entrega</button>
</form>

<script>
function validarEntrega() {
    const input = document.getElementById("valor_frete");

    const valor = input.value
        .replace('R$', '')
        .replace(/\./g, '')
        .replace(',', '.')
        .trim();

    const frete = parseFloat(valor);

    if (isNaN(frete) || frete <= 0) {
        alert("Informe um valor de frete válido.");
        input.focus();
        return false;
    }
    return true;
}

function limparCampoData(id) {
    document.getElementById(id).value = '';
}

$(document).ready(function () {
    const input = $('#valor_frete');
    const valorServidor = input.val();

    input.mask('R$ #.##0,00', { reverse: true });

    if (valorServidor && valorServidor !== '0') {
        const formatado = parseFloat(valorServidor).toFixed(2).replace('.', ',');
        input.val('R$ ' + formatado);
    } else {
        input.val('');
    }
});
</script>

</body>
</html>
