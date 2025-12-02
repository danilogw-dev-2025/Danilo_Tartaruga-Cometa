<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Cliente</title>
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

<h1>Editar Cliente</h1>

<a href="listar-clientes">Voltar para lista de clientes</a>
<br><br>
<a href="<%= request.getContextPath() %>/menu">Voltar ao Menu Principal</a>
<br><br>

<form method="post" action="cliente-servlet">

    <input type="hidden" name="idCliente" value="${cliente.idCliente}">

    <label for="codigoCliente">Código do Cliente:</label>
    <input type="text" id="codigoCliente" name="codigoCliente" value="${cliente.codigoCliente}" required /><br>

    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" value="${cliente.nome}" required /><br>

    <label for="cpf">CPF:</label>
    <input type="text" id="cpf" name="cpf" value="${cliente.cpf}" required /><br>

    <label for="dataNascimento">Data de Nascimento:</label>
    <input type="date" id="dataNascimento" name="dataNascimento" value="${cliente.dataNascimento}" required /><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="${cliente.email}" required /><br>


    <button type="submit">Salvar</button>


</form>

</body>
</html>
