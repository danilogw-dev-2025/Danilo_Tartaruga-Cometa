<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Cliente</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input { padding: 5px; width: 300px; margin-bottom: 5px; }
        button { margin-top: 15px; padding: 10px 20px; cursor: pointer; background-color: #007bff; color: white; border: none; }
        a { text-decoration: none; color: #0066cc; }
    </style>
</head>
<body>

<h1>Editar Cliente</h1>

<a href="listar-clientes">Voltar para lista de clientes</a>
<br><br>

<form method="post" action="cliente-servlet">

    <input type="hidden" name="idCliente" value="${cliente.idCliente}">

    <label for="codigoCliente">Código do Cliente:</label>
    <input type="text" id="codigoCliente" name="codigoCliente" value="${cliente.codigoCliente}" required />

    <label for="nome">Nome / Razão Social:</label>
    <input type="text" id="nome" name="nome" value="${cliente.nome}" required />

    <label for="documento">Documento (CPF ou CNPJ):</label>
    <input type="text" id="documento" name="documento" value="${cliente.documento}" required />

    <label for="estado">Estado:</label>
    <input type="text" id="estado" name="estado" value="${cliente.estado}" required />

    <label for="cidade">Cidade:</label>
    <input type="text" id="cidade" name="cidade" value="${cliente.cidade}" required />

    <label for="bairro">Bairro:</label>
    <input type="text" id="bairro" name="bairro" value="${cliente.bairro}" required />

    <label for="rua">Rua:</label>
    <input type="text" id="rua" name="rua" value="${cliente.rua}" required />

    <label for="numeroCasa">Número da Casa:</label>
    <input type="number" id="numeroCasa" name="numeroCasa" value="${cliente.numeroCasa}" />

    <br>
    <button type="submit">Salvar Alterações</button>

</form>

</body>
</html>