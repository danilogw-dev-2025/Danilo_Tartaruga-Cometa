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

    public Cliente buscarPorId(Long idCliente) {
        String sql = "SELECT ID_CLIENTE, CODIGO_CLIENTE, NOME, CPF, DATA_NASCIMENTO, EMAIL " +
                "FROM TB_CLIENTE " +
                "WHERE ID_CLIENTE = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();

                    cliente.setIdCliente(rs.getLong("ID_CLIENTE"));
                    cliente.setCodigoCliente(rs.getString("CODIGO_CLIENTE"));
                    cliente.setNome(rs.getString("NOME"));
                    cliente.setCpf(rs.getString("CPF"));

                    // Conversão de java.sql.Date -> String
                    java.sql.Date dataSql = rs.getDate("DATA_NASCIMENTO");
                    String dataString = (dataSql != null) ? dataSql.toString() : null;
                    cliente.setDataNascimento(dataString);

                    cliente.setEmail(rs.getString("EMAIL"));

                    return cliente; // <-- retorna o objeto aqui
                } else {
                    return null; // <-- não encontrado
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID", e);
        }
    }


    public boolean editarCliente(Cliente cliente) {
        String sql = "UPDATE TB_CLIENTE SET CODIGO_CLIENTE = ?, NOME = ?, CPF = ?, DATA_NASCIMENTO = ?, EMAIL = ? " +
                "WHERE ID_CLIENTE = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCodigoCliente());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getCpf());

            // Trata a data como String, Convertendo para java.sql.Date
            if (cliente.getDataNascimento() != null && !cliente.getDataNascimento().isEmpty()) {
                stmt.setDate(4, java.sql.Date.valueOf(cliente.getDataNascimento()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setString(5, cliente.getEmail());
            stmt.setLong(6, cliente.getIdCliente());

            int linhasAfetadas = stmt.executeUpdate();

            // Se nenhuma linha for atualizada, retorna false
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }



    public boolean deletarCliente(Long idCliente) {
        String sql = "DELETE FROM TB_CLIENTE WHERE id_cliente = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idCliente);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage(), e);
        }
    }



}
