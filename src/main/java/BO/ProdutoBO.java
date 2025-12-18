package BO;

import DAO.ProdutoDAO;
import Model.Produto;
import java.math.BigDecimal;

/**
 * Camada de Negócio (Business Object) para Gestão de Produtos.
 * 1. Validação Comercial: Impede a entrada de produtos sem nome ou com
 * preço/quantidade zerados ou negativos, garantindo a saúde do fluxo de caixa.
 * 2. Consistência de Dados: Assegura que campos obrigatórios (Nome, Preço, Qtd)
 * estejam presentes tanto na criação quanto na edição.
 * 3. Abstração de Persistência: Centraliza a decisão entre 'Insert' ou 'Update'
 * baseada na presença do ID, protegendo o DAO de chamadas ambíguas.
 * 4. Isolamento de Erros: Captura exceções de tempo de execução do Banco de Dados
 * e as converte em mensagens de erro controladas para a interface do usuário.
 */

public class ProdutoBO {

    private ProdutoDAO produtoDAO;

    public ProdutoBO() {
        this.produtoDAO = new ProdutoDAO();
    }

    public void salvar(Produto produto) throws Exception {

        if (produto.getNomeProduto() == null || produto.getNomeProduto().trim().isEmpty()) {
            throw new Exception("O nome do produto é obrigatório!");
        }

        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("O preço deve ser maior que R$ 0,00!");
        }

        if (produto.getQuantidade() == null || produto.getQuantidade() <= 0) {
            throw new Exception("A quantidade deve ser de pelo menos 1 unidade!");
        }

        try {

            if (produto.getIdProduto() != null && produto.getIdProduto() > 0) {
                produtoDAO.editarProdutos(produto);
            } else {
                produtoDAO.cadastrarProduto(produto);
            }

        } catch (RuntimeException e) {
            throw new Exception("Erro interno ao salvar produto: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        produtoDAO.deletarProduto(id);
    }
}