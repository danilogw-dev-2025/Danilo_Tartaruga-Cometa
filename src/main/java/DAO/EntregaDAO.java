package DAO;

import DatabaseConfig.ConnectionFactory;
import Model.Entrega;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public void cadastrarEntrega(Entrega entrega) {
        String sql = "INSERT INTO TB_ENTREGA (" +
                "CODIGO_PEDIDO, ID_REMETENTE, ID_DESTINATARIO, ID_PRODUTO, " +
                "DATA_ENVIO, DATA_ENTREGA, TRANSPORTADORA, VALOR_FRETE, STATUS" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entrega.getCodigoPedido());
            stmt.setLong(2, entrega.getIdRemetente());
            stmt.setLong(3, entrega.getIdDestinatario());
            stmt.setLong(4, entrega.getIdProduto());
            stmt.setDate(5, Date.valueOf(entrega.getDataEnvio()));
            stmt.setDate(6, Date.valueOf(entrega.getDataEntrega()));
            stmt.setString(7, entrega.getTransportadora());
            stmt.setBigDecimal(8, entrega.getValorFrete());
            stmt.setString(9, entrega.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar: " + e.getMessage(), e);
        }
    }

    public List<Entrega> listarEntregas() {
        String sql = "SELECT E.*, " +
                "R.NOME AS NOME_REMETENTE, " +
                "D.NOME AS NOME_DESTINATARIO, " +
                "P.NOME_PRODUTO, P.PRECO_PRODUTO, P.QUANTIDADE " +
                "FROM TB_ENTREGA E " +
                "INNER JOIN TB_CLIENTE R ON E.ID_REMETENTE = R.ID_CLIENTE " +
                "INNER JOIN TB_CLIENTE D ON E.ID_DESTINATARIO = D.ID_CLIENTE " +
                "INNER JOIN TB_PRODUTO P ON E.ID_PRODUTO = P.ID_PRODUTO " +
                "ORDER BY E.ID_ENTREGA DESC";

        List<Entrega> entregas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Entrega entrega = new Entrega(
                        rs.getLong("ID_ENTREGA"),
                        rs.getLong("ID_REMETENTE"),
                        rs.getLong("ID_DESTINATARIO"),
                        rs.getLong("ID_PRODUTO"),
                        rs.getString("CODIGO_PEDIDO"),
                        rs.getDate("DATA_ENVIO").toString(),
                        rs.getDate("DATA_ENTREGA").toString(),
                        rs.getString("TRANSPORTADORA"),
                        rs.getBigDecimal("VALOR_FRETE"),
                        rs.getString("STATUS")
                );

                entrega.setNomeRemetente(rs.getString("NOME_REMETENTE"));
                entrega.setNomeDestinatario(rs.getString("NOME_DESTINATARIO"));
                entrega.setNomeProduto(rs.getString("NOME_PRODUTO"));

                // Preenche a Quantidade e Calcula Total
                BigDecimal preco = rs.getBigDecimal("PRECO_PRODUTO");
                long qtd = rs.getLong("QUANTIDADE");

                entrega.setQuantidadeProduto(qtd);

                if (preco != null) {
                    entrega.setValorTotalProduto(preco.multiply(new BigDecimal(qtd)));
                } else {
                    entrega.setValorTotalProduto(BigDecimal.ZERO);
                }

                entregas.add(entrega);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar: " + e.getMessage(), e);
        }
        return entregas;
    }

    public Entrega buscarPorId(Long id) {
        String sql = "SELECT * FROM TB_ENTREGA WHERE ID_ENTREGA = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Entrega(
                            rs.getLong("ID_ENTREGA"),
                            rs.getLong("ID_REMETENTE"),
                            rs.getLong("ID_DESTINATARIO"),
                            rs.getLong("ID_PRODUTO"),
                            rs.getString("CODIGO_PEDIDO"),
                            rs.getDate("DATA_ENVIO").toString(),
                            rs.getDate("DATA_ENTREGA").toString(),
                            rs.getString("TRANSPORTADORA"),
                            rs.getBigDecimal("VALOR_FRETE"),
                            rs.getString("STATUS")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar: " + e.getMessage(), e);
        }
    }

    public void editarEntrega(Entrega entrega) {
        String sql = "UPDATE TB_ENTREGA SET CODIGO_PEDIDO=?, ID_REMETENTE=?, ID_DESTINATARIO=?, " +
                "ID_PRODUTO=?, DATA_ENVIO=?, DATA_ENTREGA=?, TRANSPORTADORA=?, VALOR_FRETE=?, STATUS=? WHERE ID_ENTREGA=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entrega.getCodigoPedido());
            stmt.setLong(2, entrega.getIdRemetente());
            stmt.setLong(3, entrega.getIdDestinatario());
            stmt.setLong(4, entrega.getIdProduto());
            stmt.setDate(5, Date.valueOf(entrega.getDataEnvio()));
            stmt.setDate(6, Date.valueOf(entrega.getDataEntrega()));
            stmt.setString(7, entrega.getTransportadora());
            stmt.setBigDecimal(8, entrega.getValorFrete());
            stmt.setString(9, entrega.getStatus());
            stmt.setLong(10, entrega.getIdEntrega());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao editar: " + e.getMessage(), e);
        }
    }

    public void deletarEntrega(Long id) {
        String sql = "DELETE FROM TB_ENTREGA WHERE ID_ENTREGA = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}