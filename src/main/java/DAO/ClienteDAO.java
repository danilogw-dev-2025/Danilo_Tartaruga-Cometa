package DAO;

import DatabaseConfig.ConnectionFactory;
import Model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Camada de Acesso a Dados (DAO) para a entidade Cliente.
 * 1. Controle Transacional: No cadastro, utiliza 'conn.setAutoCommit(false)' para
 * garantir que a inserção e a geração do 'CODIGO_CLIENTE' (Sigla + ID) ocorram como uma única operação atômica.
 * 2. Segurança: Implementa 'PreparedStatement' em todos os métodos para prevenir
 * ataques de SQL Injection.
 * 3. Integridade Referencial: O metodo 'possuiEntregasPendentes' atua como uma
 * trava de segurança antes da exclusão, verificando dependências na tabela de entregas.
 * 4. Mapeamento Objeto-Relacional (ORM Manual): Converte linhas do ResultSet
 * em instâncias da classe Model Cliente, desacoplando o SQL do restante do sistema.
 */

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

                    String sqlUpdate = "UPDATE TB_CLIENTE SET CODIGO_CLIENTE = ? WHERE ID_CLIENTE = ?";
                    try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        stmtUpdate.setString(1, codigoFinal);
                        stmtUpdate.setLong(2, idGerado);
                        stmtUpdate.executeUpdate();
                    }

                    cliente.setCodigoCliente(codigoFinal);
                    cliente.setIdCliente(idGerado);
                }

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
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

                    return cliente;
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar cliente por ID", e);
        }
    }


    public boolean editarCliente(Cliente cliente) {
        String sql = "UPDATE TB_CLIENTE SET  NOME = ?,  ESTADO = ?, CIDADE = ?, BAIRRO = ?, RUA = ?, NUMERO_CASA = ? , CEP = ? " +
                "WHERE ID_CLIENTE = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEstado());
            stmt.setString(3, cliente.getCidade());
            stmt.setString(4, cliente.getBairro());
            stmt.setString(5, cliente.getRua());
            stmt.setInt(6, cliente.getNumeroCasa());
            stmt.setString(7, cliente.getCep());
            stmt.setLong(8, cliente.getIdCliente());


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


    public boolean possuiEntregasPendentes(Long idCliente) {
        String sql = "SELECT COUNT(*) FROM TB_ENTREGA " +
                "WHERE (ID_REMETENTE = ? OR ID_DESTINATARIO = ?) " +
                "AND STATUS = 'PENDENTE'";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idCliente);
            stmt.setLong(2, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
