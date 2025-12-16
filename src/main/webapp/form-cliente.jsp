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


    <label>CEP:</label>
    <input type="text" id="cep" name="cep" maxlength="9"
           onblur="buscarEndereco()"
           value="${cliente.cep}" required />
    <small id="erroCep" style="color: #dc3545; display: none;">CEP inválido ou não encontrado.</small>

    <label>Estado (UF):</label>
    <input type="text" id="estado" name="estado" value="${cliente.estado}" required readonly/>

    <label>Cidade:</label>
    <input type="text" id="cidade" name="cidade" value="${cliente.cidade}" required readonly/>

    <label>Bairro:</label>
    <input type="text" id="bairro" name="bairro" value="${cliente.bairro}" required readonly/>

    <label>Rua/Logradouro:</label>
    <input type="text" id="rua" name="rua" value="${cliente.rua}" required readonly/>

    <label>Número:</label>
    <input type="number" id="numeroCasa" name="numeroCasa" value="${cliente.numeroCasa}" />

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

    /* ================= CEP (Integração AJAX) ================= */

        function limparCamposEndereco() {
            document.getElementById('rua').value = '';
            document.getElementById('bairro').value = '';
            document.getElementById('cidade').value = '';
            document.getElementById('estado').value = '';
            document.getElementById('erroCep').style.display = 'none';
        }

        function preencherCamposEndereco(dados) {
            document.getElementById('rua').value = dados.logradouro;
            document.getElementById('bairro').value = dados.bairro;
            document.getElementById('cidade').value = dados.localidade;
            document.getElementById('estado').value = dados.uf;

            // Foca no campo número (o único que não é preenchido pela API)
            document.getElementById('numeroCasa').focus();
        }

       function buscarEndereco() {
               // ... (código para obter o CEP e abrir o xhr)
               var cepInput = document.getElementById('cep');
               var cep = somenteNumeros(cepInput.value);

               limparCamposEndereco();

               if (cep.length !== 8) {
                   // ... (código de validação de CEP)
                   return;
               }

               cepInput.value = cep.replace(/(\d{5})(\d{3})/, "$1-$2");

               var xhr = new XMLHttpRequest();
               xhr.open('GET', '<%= request.getContextPath() %>' + '/BuscaCepServlet?cep=' + cep, true);

               document.getElementById('rua').value = '... buscando endereço ...';

               xhr.onreadystatechange = function() {
                   if (xhr.readyState === 4) {
                       if (xhr.status === 200) {
                           try {
                               var jsonResponse = JSON.parse(xhr.responseText);

                               // **VERIFICAÇÃO ADICIONAL DE INTEGRIDADE DO JSON**
                               // Se o JSON contém 'erro' ou não tem 'uf' (básico para endereço), consideramos não encontrado.
                               if (jsonResponse.erro || !jsonResponse.uf) {
                                   limparCamposEndereco();
                                   document.getElementById('erroCep').innerText = 'CEP não encontrado.';
                                   document.getElementById('erroCep').style.display = 'block';
                               } else {
                                   // 4. Preenche os campos do formulário com sucesso
                                   preencherCamposEndereco(jsonResponse);
                               }
                           } catch (e) {
                               // 💥 ESTE É O BLOCO QUE ESTÁ SENDO EXECUTADO
                               limparCamposEndereco();
                               document.getElementById('erroCep').innerText = 'Erro ao processar a resposta do servidor. Verifique o console.';
                               document.getElementById('erroCep').style.display = 'block';
                               console.error("Erro no JSON.parse. Resposta bruta:", xhr.responseText, "Erro:", e); // MUITO IMPORTANTE
                           }
                       } else {
                           // ... (código para erro de comunicação HTTP)
                           limparCamposEndereco();
                           document.getElementById('erroCep').innerText = 'Erro na comunicação com o servidor: ' + xhr.status;
                           document.getElementById('erroCep').style.display = 'block';
                       }
                   }
               };

               xhr.send();
           }
    </script>


</body>
</html>