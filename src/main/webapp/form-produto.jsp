<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Produto</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input { padding: 5px; width: 300px; margin-bottom: 5px; }
        button { margin-top: 15px; padding: 10px 20px; cursor: pointer; background-color: #28a745; color: white; border: none; }
        a { text-decoration: none; color: #0066cc; }

        .erro-box { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 20px; border-radius: 4px; max-width: 500px; }
        .erro-borda { border: 2px solid red !important; background-color: #ffe6e6; }
    </style>
</head>
<body>

<h1>Cadastro de Produto</h1>

<a href="produto-servlet">Voltar para lista de produtos</a>
<br><br>

<%
    String msgErro = (String) request.getAttribute("erroMsg");
    if (msgErro != null && !msgErro.isEmpty()) {
%>
    <div class="erro-box">
        <strong>Atenção:</strong> <%= msgErro %>
    </div>
<%
    }
%>

<form method="post" action="produto-servlet" onsubmit="return validarProduto()">

    <input type="hidden" name="idProduto" value="${produto.idProduto}" />

    <label for="codigoProduto">Código do Produto:</label>
    <input type="text" id="codigoProduto" name="codigoProduto" value="${produto.codigoProduto}" required />

    <label for="nomeProduto">Nome do Produto:</label>
    <input type="text" id="nomeProduto" name="nomeProduto" value="${produto.nomeProduto}" required />

    <label for="descricao">Descrição do Produto:</label>
    <input type="text" id="descricao" name="descricao" value="${produto.descricao}" required />

    <label for="preco">Preço (R$):</label>
    <input type="number" id="preco" name="preco" step="0.01" placeholder="0.00" value="${produto.preco}" required />

    <label for="quantidade">Quantidade:</label>
    <input type="number" id="quantidade" name="quantidade" value="${produto.quantidade}" min="1" required />

    <br>
    <button type="submit">Salvar Produto</button>
</form>

<script>
    function validarProduto() {
        var inputQtd = document.getElementById("quantidade");
        var qtd = parseInt(inputQtd.value);

        inputQtd.classList.remove("erro-borda");

        if (qtd <= 0 || isNaN(qtd)) {
            alert("A quantidade deve ser maior que zero!");
            inputQtd.classList.add("erro-borda");
            inputQtd.focus();
            return false;
        }

        return true;
    }
</script>

</body>
</html>