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
            stmt.setLong(5, produto.getQuantidade());

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
                String codigoProduto = rs.getString("CODIGO_PRODUTO").trim(); // <-- String, sem parseInt
                String nomeProduto = rs.getString("NOME_PRODUTO");
                String descricao = rs.getString("DESCRICAO");
                BigDecimal preco = rs.getBigDecimal("PRECO_PRODUTO");
                Long quantidade = rs.getLong("QUANTIDADE");

                Produto produto = new Produto(
                        codigoProduto,
                        nomeProduto,
                        descricao,
                        preco,
                        quantidade
                );
                produto.setIdProduto(idProduto);

                produtos.add(produto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }

        return produtos;
    }

    public Produto buscarPorId(Long idProduto) {

        String sql = "SELECT ID_PRODUTO, CODIGO_PRODUTO, NOME_PRODUTO, DESCRICAO, PRECO_PRODUTO, QUANTIDADE " +
                "FROM TB_PRODUTO " +
                "WHERE ID_PRODUTO = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Produto produto = new Produto();

                    produto.setIdProduto(rs.getLong("ID_PRODUTO"));
                    produto.setCodigoProduto(rs.getString("CODIGO_PRODUTO"));
                    produto.setNomeProduto(rs.getString("NOME_PRODUTO"));
                    produto.setDescricao(rs.getString("DESCRICAO"));
                    produto.setPreco(rs.getBigDecimal("PRECO_PRODUTO"));
                    produto.setQuantidade(rs.getLong("QUANTIDADE"));

                    return produto;
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto por ID", e);
        }
    }

    public boolean editarProdutos(Produto produto) {
        String sql = "UPDATE TB_PRODUTO SET CODIGO_PRODUTO = ?, NOME_PRODUTO = ?, " +
                "DESCRICAO = ?, PRECO_PRODUTO = ?, QUANTIDADE = ? " +
                "WHERE ID_PRODUTO = ?";


        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getCodigoProduto());
            stmt.setString(2, produto.getNomeProduto());
            stmt.setString(3, produto.getDescricao());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setLong(5, produto.getQuantidade());
            stmt.setLong(6, produto.getIdProduto());


            int linhasAfetadas = stmt.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Produto: " + e.getMessage(), e);
        }
    }

    public boolean deletarProduto(Long idProduto) {
        String sql = "DELETE FROM TB_PRODUTO WHERE id_produto = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProduto);
            return  stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir Produto: " + e.getMessage(), e);
        }
    }


}
