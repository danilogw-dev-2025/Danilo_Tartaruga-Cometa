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
        .input-erro {
                border: 1px solid #dc3545;
                background-color: #fff5f5;
            }
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

    <label>Documento (CPF/CNPJ):</label>
    <input type="text" id="documento" name="documento" value="${cliente.documento}" required />
    <small id="erroDocumento" style="color: #dc3545; display: none;"></small>


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
    function somenteNumeros(valor) {
        return valor.replace(/\D/g, "");
    }

    function mostrarErro(msg) {
        const erro = document.getElementById("erroDocumento");
        const input = document.getElementById("documento");

        erro.innerText = msg;
        erro.style.display = "block";
        input.classList.add("input-erro");
    }

    function limparErro() {
        const erro = document.getElementById("erroDocumento");
        const input = document.getElementById("documento");

        erro.innerText = "";
        erro.style.display = "none";
        input.classList.remove("input-erro");
    }

    /* ================= CPF ================= */
    function validarCPF(cpf) {
        if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

        let soma = 0;
        for (let i = 0; i < 9; i++) {
            soma += parseInt(cpf.charAt(i)) * (10 - i);
        }
        let digito1 = (soma * 10) % 11;
        if (digito1 === 10) digito1 = 0;
        if (digito1 !== parseInt(cpf.charAt(9))) return false;

        soma = 0;
        for (let i = 0; i < 10; i++) {
            soma += parseInt(cpf.charAt(i)) * (11 - i);
        }
        let digito2 = (soma * 10) % 11;
        if (digito2 === 10) digito2 = 0;

        return digito2 === parseInt(cpf.charAt(10));
    }

    /* ================= CNPJ ================= */
    function validarCNPJ(cnpj) {
        if (cnpj.length !== 14 || /^(\d)\1+$/.test(cnpj)) return false;

        let tamanho = cnpj.length - 2;
        let numeros = cnpj.substring(0, tamanho);
        let digitos = cnpj.substring(tamanho);
        let soma = 0;
        let pos = tamanho - 7;

        for (let i = tamanho; i >= 1; i--) {
            soma += numeros.charAt(tamanho - i) * pos--;
            if (pos < 2) pos = 9;
        }

        let resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado !== parseInt(digitos.charAt(0))) return false;

        tamanho++;
        numeros = cnpj.substring(0, tamanho);
        soma = 0;
        pos = tamanho - 7;

        for (let i = tamanho; i >= 1; i--) {
            soma += numeros.charAt(tamanho - i) * pos--;
            if (pos < 2) pos = 9;
        }

        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        return resultado === parseInt(digitos.charAt(1));
    }

    /* ================= MÁSCARA ================= */
    function aplicarMascaraCPF(valor) {
        return valor
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    }

    function aplicarMascaraCNPJ(valor) {
        return valor
            .replace(/(\d{2})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1/$2")
            .replace(/(\d{4})(\d{1,2})$/, "$1-$2");
    }

    function atualizarMascaraDocumento() {
        const input = document.getElementById("documento");
        const isPF = document.getElementById("tipoPF").checked;
        let valor = somenteNumeros(input.value);

        limparErro();

        if (isPF) {
            input.maxLength = 14;
            input.value = aplicarMascaraCPF(valor);
        } else {
            input.maxLength = 18;
            input.value = aplicarMascaraCNPJ(valor);
        }
    }

    /* ================= SUBMIT ================= */
    function validarAntesDeEnviar() {
        const input = document.getElementById("documento");
        const isPF = document.getElementById("tipoPF").checked;
        const valor = somenteNumeros(input.value);

        if (isPF) {
            if (!validarCPF(valor)) {
                mostrarErro("CPF inválido. Verifique os dígitos.");
                return false;
            }
        } else {
            if (!validarCNPJ(valor)) {
                mostrarErro("CNPJ inválido. Verifique os dígitos.");
                return false;
            }
        }

        // envia apenas números
        input.value = valor;
        return true;
    }

    document.getElementById("documento").addEventListener("input", atualizarMascaraDocumento);
    document.getElementById("tipoPF").addEventListener("change", atualizarMascaraDocumento);
    document.getElementById("tipoPJ").addEventListener("change", atualizarMascaraDocumento);

    window.onload = atualizarMascaraDocumento;
</script>


</body>
</html>