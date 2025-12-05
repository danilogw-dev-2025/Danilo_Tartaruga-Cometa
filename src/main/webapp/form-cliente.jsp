<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Cliente</title>
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; }
        h1 { color: #333; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input[type="text"], input[type="number"] { padding: 5px; width: 300px; margin-bottom: 5px; }
        button { margin-top: 15px; padding: 10px 20px; cursor: pointer; background-color: #007bff; color: white; border: none; }

        .erro-box { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 20px; }
    </style>
</head>
<body>

<h1>Cadastro de Cliente</h1>
<a href="cliente-servlet">Voltar para lista</a>
<br><br>

<%
    String msgErro = (String) request.getAttribute("erroMsg");
    if (msgErro != null && !msgErro.isEmpty()) {
%>
    <div class="erro-box"><strong>Erro:</strong> <%= msgErro %></div>
<%
    }
%>

<form method="post" action="cliente-servlet" onsubmit="return validarAntesDeEnviar()">

    <input type="hidden" name="idCliente" value="${cliente.idCliente}">

    <div style="margin-bottom: 10px;">
        <span style="font-weight: bold;">Tipo:</span>
        <input type="radio" id="tipoPF" name="tipoPessoa" value="PF" checked> Física (11 n°)
        <input type="radio" id="tipoPJ" name="tipoPessoa" value="PJ"> Jurídica (14 n°)
    </div>

    <label>Código:</label>
    <input type="text" name="codigoCliente" value="${cliente.codigoCliente}" required />

    <label>Nome:</label>
    <input type="text" name="nome" value="${cliente.nome}" required />

    <label>Documento (Somente Números):</label>
    <input type="number" id="documento" name="documento" value="${cliente.documento}" required />

    <label>Estado:</label>
    <input type="text" name="estado" value="${cliente.estado}" required />

    <label>Cidade:</label>
    <input type="text" name="cidade" value="${cliente.cidade}" required />

    <label>Bairro:</label>
    <input type="text" name="bairro" value="${cliente.bairro}" required />

    <label>Rua:</label>
    <input type="text" name="rua" value="${cliente.rua}" required />

    <label>Número:</label>
    <input type="number" name="numeroCasa" value="${cliente.numeroCasa}" />

    <br>
    <button type="submit">Salvar</button>
</form>

<script>
    function validarAntesDeEnviar() {
        var doc = document.getElementById("documento").value;
        var numero = document.getElementsByName("numeroCasa")[0].value;
        var isPF = document.getElementById("tipoPF").checked;

        // 1. Validação de Tamanho do Documento
        if (isPF && doc.length !== 11) {
            alert("Para Pessoa Física, o CPF deve ter exatamente 11 números.");
            return false;
        }
        if (!isPF && doc.length !== 14) {
            alert("Para Pessoa Jurídica, o CNPJ deve ter exatamente 14 números.");
            return false;
        }

        if (isPF) {
            if (numero === "" || numero === "0") {
                alert("Erro: Para Pessoa Física, o número da casa é obrigatório.");
                return false; // Bloqueia envio
            }
        }

        return true;
    }

    window.onload = function() {
        var doc = document.getElementById("documento").value;
        if (doc.length > 11) {
            document.getElementById("tipoPJ").checked = true;
        }
    };
</script>

</body>
</html>