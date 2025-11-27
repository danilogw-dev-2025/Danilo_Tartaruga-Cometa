package DAO;

import DatabaseConfig.ConnectionFactory;
import Model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {

    public void cadastrarEndereco(Endereco endereco) {
        String sql = "INSERT INTO TB_ENDERECO (ID_CLIENTE, ESTADO, CIDADE, BAIRRO, RUA, CEP, NUMERO_CASA) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, endereco.getIdCliente());
            stmt.setString(2, endereco.getEstado());
            stmt.setString(3, endereco.getCidade());
            stmt.setString(4, endereco.getBairro());
            stmt.setString(5, endereco.getRua());
            stmt.setString(6, endereco.getCep());
            stmt.setInt(7, endereco.getNumeroCasa());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar endereco: " + e.getMessage(), e);
        }
    }

    public List<Endereco> listarEnderecos() {
        String sql = "SELECT * FROM TB_ENDERECO";
        List<Endereco> enderecos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Endereco endereco = new Endereco(
                        rs.getLong("ID_ENDERECO"),
                        rs.getLong("ID_CLIENTE"),
                        rs.getString("ESTADO"),
                        rs.getString("CIDADE"),
                        rs.getString("BAIRRO"),
                        rs.getString("RUA"),
                        rs.getString("CEP"),
                        rs.getInt("NUMERO_CASA")
                );

                enderecos.add(endereco);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar enderecos: " + e.getMessage(), e);
        }

        return enderecos;
    }
}
