<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menu Principal - Sistema de Entregas</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
        }
        h1 {
            color: #333;
        }
        .container {
            max-width: 600px;
            margin: 40px auto;
            background: #fff;
            padding: 20px 30px;
            border-radius: 8px;
            border: 1px solid #ddd;
        }
        .secao {
            margin-bottom: 20px;
        }
        .secao h2 {
            font-size: 18px;
            margin-bottom: 8px;
        }
        a {
            display: inline-block;
            margin: 4px 0;
            text-decoration: none;
            color: #0066cc;
        }
        a:hover {
            text-decoration: underline;
        }
        hr {
            margin: 20px 0;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Menu Principal</h1>

    <div class="secao">
        <h2>Clientes</h2>
        <a href="<%= request.getContextPath() %>/novo-cliente">➕ Cadastrar Cliente</a><br>
        <a href="<%= request.getContextPath() %>/listar-clientes">📋 Listar Clientes</a>
    </div>

    <hr>

    <div class="secao">
        <h2>Produtos</h2>
        <a href="<%= request.getContextPath() %>/novo-produto">➕ Cadastrar Produto</a><br>
        <a href="<%= request.getContextPath() %>/listar-produtos">📋 Listar Produtos</a>
    </div>

    <hr>

    <div class="secao">
        <h2>Entregas</h2>
        <a href="<%= request.getContextPath() %>/nova-entrega">➕ Cadastrar Entrega</a><br>
        <a href="<%= request.getContextPath() %>/listar-entregas">📋 Listar Entregas</a>
    </div>
</div>

</body>
</html>
