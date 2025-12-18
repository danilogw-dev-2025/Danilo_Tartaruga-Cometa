<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Cliente - Sistema de Entregas</title>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

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


    <label>Nome:</label>
    <input type="text" name="nome" maxlength="70" value="${cliente.nome}" required />

    <label>Documento (CPF/CNPJ):</label>
    <input type="text"
           id="documento"
           name="documento"
           value="${cliente.documento}"
           required
           <%-- Lógica de bloqueio: se houver ID, o campo fica apenas leitura --%>
           ${(not empty cliente.idCliente and cliente.idCliente > 0) ? 'readonly' : ''}
           <%-- Estilo visual para indicar que está bloqueado --%>
           style="${(not empty cliente.idCliente and cliente.idCliente > 0) ? 'background-color: #e9ecef; cursor: not-allowed;' : ''}"
    />
    <small id="erroDocumento" style="color: #dc3545; display: none;"></small>

    <c:if test="${not empty cliente.idCliente and cliente.idCliente > 0}">
        <small style="color: #6c757d; display: block;">O documento não pode ser alterado após o cadastro.</small>
    </c:if>


    <label>CEP:</label>
    <input type="text" id="cep" name="cep" maxlength="9"
           onblur="buscarEndereco()"
           value="${cliente.cep}"  />
    <small id="erroCep" style="color: #dc3545; display: none;">CEP inválido ou não encontrado.</small>

    <label>Estado (UF):</label>
    <input type="text" id="estado" name="estado" maxlength="2" value="${cliente.estado}" required />

    <label>Cidade:</label>
    <input type="text" id="cidade" name="cidade" maxlength="50" value="${cliente.cidade}" required />

    <label>Bairro:</label>
    <input type="text" id="bairro" name="bairro" maxlength="50" value="${cliente.bairro}" required />

    <label>Rua/Logradouro:</label>
    <input type="text" id="rua" name="rua" maxlength="50"  value="${cliente.rua}" required />

    <label>Número para residência:</label>
    <input type="number" id="numeroCasa" name="numeroCasa" max="99999" value="${cliente.numeroCasa}" />

    <br>
    <button type="submit">Salvar</button>
</form>

<script>
    function somenteNumeros(valor) {
        return valor ? valor.toString().replace(/\D/g, "") : "";
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


    function validarCPF(cpf) {
        if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;
        let soma = 0;
        for (let i = 0; i < 9; i++) soma += parseInt(cpf.charAt(i)) * (10 - i);
        let d1 = (soma * 10) % 11;
        if (d1 === 10) d1 = 0;
        if (d1 !== parseInt(cpf.charAt(9))) return false;
        soma = 0;
        for (let i = 0; i < 10; i++) soma += parseInt(cpf.charAt(i)) * (11 - i);
        let d2 = (soma * 10) % 11;
        if (d2 === 10) d2 = 0;
        return d2 === parseInt(cpf.charAt(10));
    }

    function validarCNPJ(cnpj) {
        if (cnpj.length !== 14 || /^(\d)\1+$/.test(cnpj)) return false;
        let v1 = [5,4,3,2,9,8,7,6,5,4,3,2], v2 = [6,5,4,3,2,9,8,7,6,5,4,3,2];
        let calc = (s, v) => {
            let r = 0;
            for(let i=0; i<v.length; i++) r += s[i] * v[i];
            return r % 11 < 2 ? 0 : 11 - (r % 11);
        };
        return calc(cnpj, v1) == cnpj[12] && calc(cnpj, v2) == cnpj[13];
    }

    function atualizarMascaraDocumento() {
        const input = document.getElementById("documento");
        const isPF = document.getElementById("tipoPF").checked;
        let valor = somenteNumeros(input.value);

        limparErro();

        if (isPF) {
            input.maxLength = 14;
            if(valor.length > 0)
                input.value = valor.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
        } else {
            input.maxLength = 18;
            if(valor.length > 0)
                input.value = valor.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
        }
    }

    function validarAntesDeEnviar() {
        const input = document.getElementById("documento");
        const isPF = document.getElementById("tipoPF").checked;
        const valor = somenteNumeros(input.value);

        if (isPF) {
            if (!validarCPF(valor)) {
                mostrarErro("CPF inválido.");
                return false;
            }
        } else {
            if (!validarCNPJ(valor)) {
                mostrarErro("CNPJ inválido.");
                return false;
            }
        }
        input.value = valor; // Envia limpo
        return true;
    }


    function buscarEndereco() {
        var cepInput = document.getElementById('cep');
        var cep = somenteNumeros(cepInput.value);

        limparCamposEndereco();

        if (cep.length !== 8) return;

        Swal.fire({
            title: 'Buscando CEP...',
            text: 'Aguarde um momento.',
            allowOutsideClick: false,
            showConfirmButton: false,
            didOpen: () => { Swal.showLoading(); }
        });

        cepInput.value = cep.replace(/(\d{5})(\d{3})/, "$1-$2");

        var xhr = new XMLHttpRequest();
        xhr.open('GET', '<%= request.getContextPath() %>/BuscaCepServlet?cep=' + cep, true);

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                Swal.close();
                if (xhr.status === 200) {
                    try {
                        var jsonResponse = JSON.parse(xhr.responseText);
                        if (jsonResponse.erro || !jsonResponse.uf) {
                            document.getElementById('erroCep').innerText = 'CEP não encontrado.';
                            document.getElementById('erroCep').style.display = 'block';
                        } else {
                            preencherCamposEndereco(jsonResponse);
                        }
                    } catch (e) {
                        console.error("Erro no parse:", e);
                    }
                } else {
                    document.getElementById('erroCep').innerText = 'Erro na comunicação: ' + xhr.status;
                    document.getElementById('erroCep').style.display = 'block';
                }
            }
        };
        xhr.send();
    }

    function preencherCamposEndereco(dados) {
        document.getElementById('rua').value = dados.logradouro || "";
        document.getElementById('bairro').value = dados.bairro || "";
        document.getElementById('cidade').value = dados.localidade || "";
        document.getElementById('estado').value = dados.uf || "";
        document.getElementById('numeroCasa').focus();
    }

    function limparCamposEndereco() {
        document.getElementById('rua').value = '';
        document.getElementById('bairro').value = '';
        document.getElementById('cidade').value = '';
        document.getElementById('estado').value = '';
        document.getElementById('erroCep').style.display = 'none';
    }

    document.getElementById("documento").addEventListener("input", atualizarMascaraDocumento);
    document.getElementById("tipoPF").addEventListener("change", atualizarMascaraDocumento);
    document.getElementById("tipoPJ").addEventListener("change", atualizarMascaraDocumento);

    window.onload = atualizarMascaraDocumento;
</script>
</body>
</html>