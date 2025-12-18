package DAO;

import DatabaseConfig.ConnectionFactory;
import Model.Entrega;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de Acesso a Dados (DAO) para Logística e Inventário.
 * 1. Integridade Transacional: Utiliza transações JDBC para garantir que a
 * criação da entrega e a baixa no estoque ocorram como uma operação única.
 * 2. Inteligência de Rastreio: Implementa lógica pós-insert para gerar e
 * gravar o código 'ENC' (Encomenda) baseado no ID serial gerado pelo banco.
 * 3. Consultas com Join: O metodo 'listarEntregas' realiza múltiplos INNER JOINs
 * para consolidar dados de Clientes e Produtos, reduzindo a carga no Servlet.
 * 4. Gestão de Devolução: O metodo 'deletarEntregaComDevolucao' garante que
 * itens de pedidos cancelados ou excluídos retornem automaticamente ao saldo do produto.
 * 5. Ajuste de Diferencial: Implementa 'ajustarQuantidadeEstoque' para sincronizar
 * flutuações de quantidade durante edições sem corromper o estoque total.
 */

public class EntregaDAO {

    public void cadastrarEntrega(Entrega entrega) {

        String sqlCheckEstoque = "SELECT QUANTIDADE FROM TB_PRODUTO WHERE ID_PRODUTO = ?";

        String sqlInsertEntrega = "INSERT INTO TB_ENTREGA (ID_REMETENTE, ID_DESTINATARIO, ID_PRODUTO, " +
                "QTD_PEDIDA, DATA_ENVIO, DATA_ENTREGA, TRANSPORTADORA, VALOR_FRETE, STATUS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID_ENTREGA";

        String sqlUpdateEstoque = "UPDATE TB_PRODUTO SET QUANTIDADE = QUANTIDADE - ? WHERE ID_PRODUTO = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try {

                int estoqueAtual = 0;
                try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlCheckEstoque)) {
                    stmtEstoque.setLong(1, entrega.getIdProduto());
                    ResultSet rs = stmtEstoque.executeQuery();
                    if (rs.next()) estoqueAtual = rs.getInt("QUANTIDADE");
                }

                if (estoqueAtual < entrega.getQtdPedida()) {
                    throw new RuntimeException("Estoque insuficiente! Disponível: " + estoqueAtual);
                }

                long idGerado = 0;
                try (PreparedStatement stmtIns = conn.prepareStatement(sqlInsertEntrega)) {
                    stmtIns.setLong(1, entrega.getIdRemetente());
                    stmtIns.setLong(2, entrega.getIdDestinatario());
                    stmtIns.setLong(3, entrega.getIdProduto());
                    stmtIns.setInt(4, entrega.getQtdPedida());
                    stmtIns.setDate(5, Date.valueOf(entrega.getDataEnvio()));
                    stmtIns.setDate(6, Date.valueOf(entrega.getDataEntrega()));
                    stmtIns.setString(7, entrega.getTransportadora());
                    stmtIns.setBigDecimal(8, entrega.getValorFrete());
                    stmtIns.setString(9, "PENDENTE");

                    ResultSet rs = stmtIns.executeQuery();
                    if (rs.next()) idGerado = rs.getLong(1);
                }

                //  Gerar e Gravar Código de Rastreio (ENC...)
                int anoAtual = LocalDate.now().getYear();
                String codigoRastreio = String.format("ENC%d%05d", anoAtual, idGerado);

                String sqlUpdateCodigo = "UPDATE TB_ENTREGA SET CODIGO_PEDIDO = ? WHERE ID_ENTREGA = ?";
                try (PreparedStatement stmtCod = conn.prepareStatement(sqlUpdateCodigo)) {
                    stmtCod.setString(1, codigoRastreio);
                    stmtCod.setLong(2, idGerado);
                    stmtCod.executeUpdate();
                }

                //  Baixa no Estoque do Produto
                try (PreparedStatement stmtUpdEstoque = conn.prepareStatement(sqlUpdateEstoque)) {
                    stmtUpdEstoque.setInt(1, entrega.getQtdPedida());
                    stmtUpdEstoque.setLong(2, entrega.getIdProduto());
                    stmtUpdEstoque.executeUpdate();
                }

                entrega.setCodigoPedido(codigoRastreio);
                entrega.setIdEntrega(idGerado);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao processar entrega: " + e.getMessage());
        }
    }

    public List<Entrega> listarEntregas() {
        String sql = "SELECT E.*, R.NOME AS NOME_REMETENTE, D.NOME AS NOME_DESTINATARIO, " +
                "P.NOME_PRODUTO, P.PRECO_PRODUTO, P.QUANTIDADE, E.QTD_PEDIDA " +
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
                        rs.getString("STATUS"),
                        rs.getInt("QTD_PEDIDA")
                );

                entrega.setNomeRemetente(rs.getString("NOME_REMETENTE"));
                entrega.setNomeDestinatario(rs.getString("NOME_DESTINATARIO"));
                entrega.setNomeProduto(rs.getString("NOME_PRODUTO"));
                entrega.setQtdPedida(rs.getInt("QTD_PEDIDA"));

                BigDecimal preco = rs.getBigDecimal("PRECO_PRODUTO");
                int qtdEnviada = rs.getInt("QTD_PEDIDA");
                if (preco != null) {
                    entrega.setValorTotalProduto(preco.multiply(new BigDecimal(qtdEnviada)));
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
                            rs.getString("STATUS"),
                            rs.getInt("QTD_PEDIDA")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar: " + e.getMessage(), e);
        }
    }

    public void editarEntrega(Entrega entrega) {
        String sql = "UPDATE TB_ENTREGA SET  ID_REMETENTE=?, ID_DESTINATARIO=?, " +
                "ID_PRODUTO=?, DATA_ENVIO=?, DATA_ENTREGA=?, TRANSPORTADORA=?, VALOR_FRETE=?, STATUS=?, QTD_PEDIDA=? WHERE ID_ENTREGA=?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, entrega.getIdRemetente());
            stmt.setLong(2, entrega.getIdDestinatario());
            stmt.setLong(3, entrega.getIdProduto());
            stmt.setDate(4, Date.valueOf(entrega.getDataEnvio()));
            stmt.setDate(5, Date.valueOf(entrega.getDataEntrega()));
            stmt.setString(6, entrega.getTransportadora());
            stmt.setBigDecimal(7, entrega.getValorFrete());
            stmt.setString(8, entrega.getStatus());
            stmt.setInt(9, entrega.getQtdPedida());
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

    public void deletarEntregaComDevolucao(Long id) {

        String sqlBusca = "SELECT ID_PRODUTO, QTD_PEDIDA FROM TB_ENTREGA WHERE ID_ENTREGA = ?";
        String sqlDevolverEstoque = "UPDATE TB_PRODUTO SET QUANTIDADE = QUANTIDADE + ? WHERE ID_PRODUTO = ?";
        String sqlDelete = "DELETE FROM TB_ENTREGA WHERE ID_ENTREGA = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try {
                long idProduto = 0;
                int qtdParaDevolver = 0;

                try (PreparedStatement stmtBusca = conn.prepareStatement(sqlBusca)) {
                    stmtBusca.setLong(1, id);
                    try (ResultSet rs = stmtBusca.executeQuery()) {
                        if (rs.next()) {
                            idProduto = rs.getLong("ID_PRODUTO");
                            qtdParaDevolver = rs.getInt("QTD_PEDIDA");
                        }
                    }
                }

                // 2. Devolve a quantidade ao estoque do produto
                if (idProduto > 0) {
                    try (PreparedStatement stmtUpd = conn.prepareStatement(sqlDevolverEstoque)) {
                        stmtUpd.setInt(1, qtdParaDevolver);
                        stmtUpd.setLong(2, idProduto);
                        stmtUpd.executeUpdate();
                    }
                }

                // 3. Deleta o registro da entrega
                try (PreparedStatement stmtDel = conn.prepareStatement(sqlDelete)) {
                    stmtDel.setLong(1, id);
                    stmtDel.executeUpdate();
                }

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Erro ao deletar e devolver estoque: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void atualizarStatusECancelarEstoque(Long idEntrega) {
        String sqlBusca = "SELECT ID_PRODUTO, QTD_PEDIDA FROM TB_ENTREGA WHERE ID_ENTREGA = ?";
        String sqlUpdateEstoque = "UPDATE TB_PRODUTO SET QUANTIDADE = QUANTIDADE + ? WHERE ID_PRODUTO = ?";
        String sqlUpdateStatus = "UPDATE TB_ENTREGA SET STATUS = 'CANCELADA' WHERE ID_ENTREGA = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int qtdParaDevolver = 0;
                long idProduto = 0;

                try (PreparedStatement ps = conn.prepareStatement(sqlBusca)) {
                    ps.setLong(1, idEntrega);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        idProduto = rs.getLong("ID_PRODUTO");
                        qtdParaDevolver = rs.getInt("QTD_PEDIDA");
                    }
                }

                // Devolve ao estoque
                try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdateEstoque)) {
                    psUpd.setInt(1, qtdParaDevolver);
                    psUpd.setLong(2, idProduto);
                    psUpd.executeUpdate();
                }

                // Atualiza status
                try (PreparedStatement psStatus = conn.prepareStatement(sqlUpdateStatus)) {
                    psStatus.setLong(1, idEntrega);
                    psStatus.executeUpdate();
                }

                conn.commit();
            } catch (Exception e) { conn.rollback(); throw e; }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void ajustarQuantidadeEstoque(Long idProduto, int diferenca) {
        // Se a diferença for 1 (aumento), o banco faz: estoque = estoque - 1
        String sql = "UPDATE TB_PRODUTO SET QUANTIDADE = QUANTIDADE - ? WHERE ID_PRODUTO = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, diferenca);
            ps.setLong(2, idProduto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao ajustar estoque na edição: " + e.getMessage());
        }
    }

}