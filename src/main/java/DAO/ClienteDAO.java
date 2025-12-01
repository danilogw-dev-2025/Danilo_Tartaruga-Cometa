package DAO;

import DatabaseConfig.ConnectionFactory;
import Model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void cadastrarCliente(Cliente cliente) {
        String sql = "INSERT INTO TB_CLIENTE (CODIGO_CLIENTE, NOME, CPF, DATA_NASCIMENTO, EMAIL) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCodigoCliente());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getCpf());
            stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataNascimento())); // String -> Date (yyyy-MM-dd)
            stmt.setString(5, cliente.getEmail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    public List<Cliente> listarClientes() {
        String sql = "SELECT * FROM TB_CLIENTE";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                java.sql.Date dataNasc = rs.getDate("DATA_NASCIMENTO");

                Cliente cliente = new Cliente(
                        rs.getLong("ID_CLIENTE"),
                        rs.getString("CODIGO_CLIENTE"),
                        rs.getString("NOME"),
                        rs.getString("CPF"),
                        dataNasc != null ? dataNasc.toString() : null,  // Conversão de Date para String
                        rs.getString("EMAIL")
                );

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage());
        }
        return clientes;
    }
}
