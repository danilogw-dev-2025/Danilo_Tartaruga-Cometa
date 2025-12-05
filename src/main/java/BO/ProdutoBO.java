package BO;

import DAO.ProdutoDAO;
import Model.Produto;
import java.math.BigDecimal;

public class ProdutoBO {

    private ProdutoDAO produtoDAO;

    public ProdutoBO() {
        this.produtoDAO = new ProdutoDAO();
    }

    public void salvar(Produto produto) throws Exception {

        if (produto.getNomeProduto() == null || produto.getNomeProduto().trim().isEmpty()) {
            throw new Exception("O nome do produto é obrigatório!");
        }

        if (produto.getCodigoProduto() == null || produto.getCodigoProduto().trim().isEmpty()) {
            throw new Exception("O código do produto é obrigatório!");
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

            String mensagemErro = e.getMessage().toLowerCase();

            if (mensagemErro.contains("duplicate key") || mensagemErro.contains("unique constraint")) {

                if (mensagemErro.contains("codigo_produto")) {
                    throw new Exception("Erro: Já existe um produto cadastrado com este Código (" + produto.getCodigoProduto() + ").");
                }
            }

            throw new Exception("Erro interno ao salvar produto: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        produtoDAO.deletarProduto(id);
    }
}