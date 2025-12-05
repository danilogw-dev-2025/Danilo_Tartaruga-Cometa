<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Produto</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input { padding: 5px; width: 300px; margin-bottom: 5px; }
        button { margin-top: 15px; padding: 10px 20px; cursor: pointer; background-color: #007bff; color: white; border: none; }
        button:hover { background-color: #0056b3; }
        a { text-decoration: none; color: #0066cc; }
    </style>
</head>
<body>

<h1>Editar Produto</h1>

<a href="produto-servlet">Voltar para lista de produtos</a>
<br><br>

<form method="post" action="produto-servlet">

    <input type="hidden" name="idProduto" value="${produto.idProduto}">

    <label for="codigoProduto">Código do Produto:</label>
    <input type="text" id="codigoProduto" name="codigoProduto" value="${produto.codigoProduto}" required />

    <label for="nomeProduto">Nome do Produto:</label>
    <input type="text" id="nomeProduto" name="nomeProduto" value="${produto.nomeProduto}" required />

    <label for="descricao">Descrição do Produto:</label>
    <input type="text" id="descricao" name="descricao" value="${produto.descricao}" required />

    <label for="preco">Preço (R$):</label>
    <input type="number" id="preco" name="preco" step="0.01" placeholder="0.00" value="${produto.preco}" required />

    <label for="quantidade">Quantidade:</label>
    <input type="number" id="quantidade" name="quantidade" value="${produto.quantidade}" required />

    <br>
    <button type="submit">Salvar Alterações</button>
</form>

</body>
</html>