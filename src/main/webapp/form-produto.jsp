<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.mask/1.14.16/jquery.mask.min.js"></script>

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
    <input type="text" name="codigoProduto" value="${produto.codigoProduto != null ? produto.codigoProduto : 'Gerado automaticamente'}" readonly
        style="background-color: #eee;" />

    <label for="nomeProduto">Nome do Produto:</label>
    <input type="text" id="nomeProduto" name="nomeProduto" value="${produto.nomeProduto}" required />

    <label for="descricao">Descrição do Produto:</label>
    <input type="text" id="descricao" name="descricao" value="${produto.descricao}" required />

    <label for="preco">Preço (R$):</label>
    <input type="text" id="preco" name="preco" placeholder="0,00" value="${produto.preco}" />


    <label for="quantidade">Quantidade:</label>
    <input type="number" id="quantidade" name="quantidade" value="${produto.quantidade}" min="1" required />

    <br>
    <button type="submit">Salvar Produto</button>
</form>

<script>
    // Função para formatar um número (como 123.45) para a máscara (123,45)
    function formatarValorParaMascara(valor) {
        if (!valor || valor === 0) return '0,00';

        // Converte o valor para string com duas casas decimais
        // Ex: 123.45 -> "123.45"
        var str = parseFloat(valor).toFixed(2);

        // Troca o ponto por vírgula decimal
        str = str.replace('.', ',');

        return str;
    }

    $(document).ready(function () {
        const inputPreco = $('#preco');
        let valorServidor = inputPreco.val();

        // Aplica máscara primeiro
        inputPreco.mask('R$ #.##0,00', {
            reverse: true
        });

        // Se for edição (valor vindo do servidor)
        if (valorServidor && valorServidor !== '0') {
            let formatado = parseFloat(valorServidor)
                .toFixed(2)
                .replace('.', ',');

            inputPreco.val('R$ ' + formatado);
        } else {
            // Novo cadastro → campo realmente vazio
            inputPreco.val('');
        }
    });



   function validarProduto() {
       const inputPreco = document.getElementById("preco");
       const inputQtd = document.getElementById("quantidade");

       const precoLimpo = inputPreco.value
           .replace('R$', '')
           .replace(/\./g, '')
           .replace(',', '.')
           .trim();

       const preco = parseFloat(precoLimpo);
       const qtd = parseInt(inputQtd.value);

       inputPreco.classList.remove("erro-borda");
       inputQtd.classList.remove("erro-borda");

       if (isNaN(preco) || preco <= 0) {
           alert("Informe um preço válido maior que zero.");
           inputPreco.classList.add("erro-borda");
           inputPreco.focus();
           return false;
       }

       if (isNaN(qtd) || qtd <= 0) {
           alert("A quantidade deve ser maior que zero.");
           inputQtd.classList.add("erro-borda");
           inputQtd.focus();
           return false;
       }

       return true;
   }

</script>

</body>
</html>