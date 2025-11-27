package DAO;

import DatabaseConfig.ConnectionFactory;
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
}
