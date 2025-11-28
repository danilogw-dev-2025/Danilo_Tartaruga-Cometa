package DAO;

import DatabaseConfig.ConnectionFactory;
import Model.Produto;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void cadastrarProduto(Produto produto) {
        String sql = "INSERT INTO TB_PRODUTO (" +
                "CODIGO_PRODUTO, NOME_PRODUTO, DESCRICAO, PRECO_PRODUTO, QUANTIDADE" +
                ") VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // CODIGO_PRODUTO é CHAR(30) no banco -> converto o int para String
            stmt.setString(1, String.valueOf(produto.getCodigoProduto()));
            stmt.setString(2, produto.getNomeProduto());
            stmt.setString(3, produto.getDescricao());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setInt(5, produto.getQuantidade());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar produto: " + e.getMessage(), e);
        }
    }

    public List<Produto> listarProdutos() {
        String sql = "SELECT * FROM TB_PRODUTO";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Long idProduto = rs.getLong("ID_PRODUTO");

                // CODIGO_PRODUTO é CHAR(30) -> converto para int (assumindo que só tem número)
                String codigoProdutoStr = rs.getString("CODIGO_PRODUTO").trim();
                int codigoProduto = Integer.parseInt(codigoProdutoStr);

                String nomeProduto = rs.getString("NOME_PRODUTO");
                String descricao = rs.getString("DESCRICAO");
                BigDecimal preco = rs.getBigDecimal("PRECO_PRODUTO");
                int quantidade = rs.getInt("QUANTIDADE");

                Produto produto = new Produto(
                        codigoProduto,
                        nomeProduto,
                        descricao,
                        preco,
                        quantidade
                );
                // setar o ID gerado pelo banco
                produto.setIdProduto(idProduto);

                produtos.add(produto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }

        return produtos;
    }
}
