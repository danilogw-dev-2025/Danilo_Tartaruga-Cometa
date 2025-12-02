package DAO;

import DatabaseConfig.ConnectionFactory;
import Model.Cliente;
import Model.Entrega;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public void cadastrarEntrega(Entrega entrega) {
        String sql = "INSERT INTO TB_ENTREGA (" +
                "CODIGO_PEDIDO, ID_CLIENTE, ID_PRODUTO, " +
                "DATA_ENVIO, DATA_ENTREGA, TRANSPORTADORA, VALOR_FRETE" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entrega.getCodigoPedido());
            stmt.setLong(2, entrega.getIdCliente());
            stmt.setLong(3, entrega.getIdProduto());

            // Convertendo String -> Date (espera formato yyyy-MM-dd)
            stmt.setDate(4, Date.valueOf(entrega.getDataEnvio()));
            stmt.setDate(5, Date.valueOf(entrega.getDataEntrega()));

            stmt.setString(6, entrega.getTransportadora());
            stmt.setBigDecimal(7, entrega.getValorFrete());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar entrega: " + e.getMessage(), e);
        }
    }

    public List<Entrega> listarEntregas() {
        String sql = "SELECT * FROM TB_ENTREGA";
        List<Entrega> entregas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Date dataEnvio = rs.getDate("DATA_ENVIO");
                Date dataEntrega = rs.getDate("DATA_ENTREGA");
                BigDecimal valorFrete = rs.getBigDecimal("VALOR_FRETE");

                Entrega entrega = new Entrega(
                        rs.getLong("ID_ENTREGA"),
                        rs.getLong("ID_CLIENTE"),
                        rs.getLong("ID_PRODUTO"),
                        rs.getString("CODIGO_PEDIDO"),
                        dataEnvio != null ? dataEnvio.toString() : null,   // DATE -> String
                        dataEntrega != null ? dataEntrega.toString() : null,
                        rs.getString("TRANSPORTADORA"),
                        valorFrete
                );

                entregas.add(entrega);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar entregas: " + e.getMessage(), e);
        }

        return entregas;
    }

    public Entrega buscarPorId(Long idEntrega) {
        String sql = "SELECT ID_ENTREGA, ID_CLIENTE, ID_PRODUTO, CODIGO_PEDIDO, DATA_ENVIO, DATA_ENTREGA, TRANSPORTADORA, VALOR_FRETE " +
                "FROM TB_ENTREGA WHERE ID_ENTREGA = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idEntrega);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Entrega entrega = new Entrega();

                    entrega.setIdEntrega(rs.getLong("ID_ENTREGA"));
                    entrega.setIdCliente(rs.getLong("ID_CLIENTE"));
                    entrega.setIdProduto(rs.getLong("ID_PRODUTO"));
                    entrega.setCodigoPedido(rs.getString("CODIGO_PEDIDO"));

                    entrega.setDataEnvio(rs.getDate("DATA_ENVIO") != null ? rs.getDate("DATA_ENVIO").toString() : null);
                    entrega.setDataEntrega(rs.getDate("DATA_ENTREGA") != null ? rs.getDate("DATA_ENTREGA").toString() : null);

                    entrega.setTransportadora(rs.getString("TRANSPORTADORA"));
                    entrega.setValorFrete(rs.getBigDecimal("VALOR_FRETE"));

                    return entrega;
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar Entrega por ID", e);
        }
    }



    public boolean editarEntrega(Entrega entrega) {
        String sql = "UPDATE TB_ENTREGA SET CODIGO_PEDIDO = ?, DATA_ENVIO = ?, DATA_ENTREGA = ?, TRANSPORTADORA = ?, VALOR_FRETE = ? " +
                "WHERE ID_ENTREGA = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entrega.getCodigoPedido());


            // Trata a data como String, Convertendo para java.sql.Date
            if (entrega.getDataEnvio() != null && !entrega.getDataEnvio().isEmpty()) {
                stmt.setDate(2, java.sql.Date.valueOf(entrega.getDataEnvio()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            // Trata a data como String, Convertendo para java.sql.Date
            if (entrega.getDataEntrega() != null && !entrega.getDataEntrega().isEmpty()) {
                stmt.setDate(3, java.sql.Date.valueOf(entrega.getDataEntrega()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }

            stmt.setString(4, entrega.getTransportadora());
            stmt.setBigDecimal(5, entrega.getValorFrete());
            stmt.setLong(6, entrega.getIdEntrega());


            int linhasAfetadas = stmt.executeUpdate();

            // Se nenhuma linha for atualizada, retorna false
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar Entrega: " + e.getMessage(), e);
        }

    }
    public boolean deletarEntrega(Long idEntrega) {
        String sql = "DELETE FROM TB_ENTREGA WHERE id_entrega = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idEntrega);
            return  stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir Entrega: " + e.getMessage(), e);
        }
    }
}
