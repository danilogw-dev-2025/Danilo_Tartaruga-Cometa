<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Produto</title>
    <style>
        body { font-family: Arial, sans-serif; }
        h1 { color: #333; }
        label { display: inline-block; width: 180px; margin-top: 8px; }
        input { padding: 4px 8px; width: 250px; }
        button { margin-top: 15px; padding: 8px 16px; }
        a { text-decoration: none; color: #0066cc; }
    </style>
</head>
<body>

<h1>Editar Produto</h1>

<a href="listar-produtos">Voltar para lista de produtos</a>
<br><br>

<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>

<form method="post" action="produto-servlet">

    <input type="hidden" name="idProduto" value="${produto.idProduto}">

    <label for="codigoProduto">Código do Produto:</label>
    <input type="text" id="codigoProduto" name="codigoProduto" value="${produto.codigoProduto}" required /><br>

    <label for="nomeProduto">Nome do Produto:</label>
    <input type="text" id="nomeProduto" name="nomeProduto" value="${produto.nomeProduto}" required /><br>

    <label for="descricao">Descrição do Produto:</label>
    <input type="text" id="descricao" name="descricao" value="${produto.descricao}" required /><br>

    <label for="preco">Preço:</label>
    <input type="number" id="preco" name="preco" placeholder= "R$ 0,00" value="${produto.preco}" required /><br>

    <label for="quantidade">Quantidade:</label>
    <input type="number" id="quantidade" name="quantidade" value="${produto.quantidade}" required /><br>


    <button type="submit">Salvar</button>
</form>

</body>
</html>