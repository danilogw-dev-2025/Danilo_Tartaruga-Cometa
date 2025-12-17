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

        String sqlInsert = "INSERT INTO TB_CLIENTE (NOME, DOCUMENTO, ESTADO, CIDADE, BAIRRO, RUA, NUMERO_CASA, CEP) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID_CLIENTE";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {

                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getDocumento());
                stmt.setString(3, cliente.getEstado());
                stmt.setString(4, cliente.getCidade());
                stmt.setString(5, cliente.getBairro());
                stmt.setString(6, cliente.getRua());
                stmt.setInt(7, cliente.getNumeroCasa());
                stmt.setString(8, cliente.getCep());

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    long idGerado = rs.getLong(1);

                    // Lógica da Sigla + ID (ex: ABC00001)
                    String sigla = cliente.gerarSiglaDoNome();
                    String codigoFinal = String.format("%s%05d", sigla, idGerado);

                    // Update para gravar o código automático
                    String sqlUpdate = "UPDATE TB_CLIENTE SET CODIGO_CLIENTE = ? WHERE ID_CLIENTE = ?";
                    try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        stmtUpdate.setString(1, codigoFinal);
                        stmtUpdate.setLong(2, idGerado);
                        stmtUpdate.executeUpdate();
                    }

                    // Atualiza o objeto para o Servlet poder usar depois
                    cliente.setCodigoCliente(codigoFinal);
                    cliente.setIdCliente(idGerado);
                }

                conn.commit(); 

            } catch (SQLException e) {
                conn.rollback(); // Se falhar o update, desfaz o insert
                throw e;
            }
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
                Cliente cliente = new Cliente(
                        rs.getLong("ID_CLIENTE"),
                        rs.getString("CODIGO_CLIENTE"),
                        rs.getString("NOME"),
                        rs.getString("DOCUMENTO"),
                        rs.getString("ESTADO"),
                        rs.getString("CIDADE"),
                        rs.getString("BAIRRO"),
                        rs.getString("RUA"),
                        rs.getInt("NUMERO_CASA"),
                        rs.getString("CEP")
                );

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public Cliente buscarPorId(Long idCliente) {
        String sql = "SELECT ID_CLIENTE, CODIGO_CLIENTE, NOME, DOCUMENTO, ESTADO, CIDADE, BAIRRO, RUA, NUMERO_CASA, CEP " +
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
                    cliente.setDocumento(rs.getString("DOCUMENTO"));
                    cliente.setEstado(rs.getString("ESTADO"));
                    cliente.setCidade(rs.getString("CIDADE"));
                    cliente.setBairro(rs.getString("BAIRRO"));
                    cliente.setRua(rs.getString("RUA"));
                    cliente.setNumeroCasa(rs.getInt("NUMERO_CASA"));
                    cliente.setCep(rs.getString("CEP"));

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
        String sql = "UPDATE TB_CLIENTE SET CODIGO_CLIENTE = ?, NOME = ?, DOCUMENTO = ?, ESTADO = ?, CIDADE = ?, BAIRRO = ?, RUA = ?, NUMERO_CASA = ? , CEP = ? " +
                "WHERE ID_CLIENTE = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCodigoCliente());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getDocumento());
            stmt.setString(4, cliente.getEstado());
            stmt.setString(5, cliente.getCidade());
            stmt.setString(6, cliente.getBairro());
            stmt.setString(7, cliente.getRua());
            stmt.setInt(8, cliente.getNumeroCasa());
            stmt.setString(9, cliente.getCep());
            stmt.setLong(10, cliente.getIdCliente());


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
