package DAO;

import DatabaseConfig.ConnectionFactory;
import Model.Entrega;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public void cadastrarEntrega(Entrega entrega) {
        // SQLs preparados
        String sqlCheckEstoque = "SELECT QUANTIDADE FROM TB_PRODUTO WHERE ID_PRODUTO = ?";

        // Incluí todas as colunas necessárias, incluindo DATA_ENTREGA e TRANSPORTADORA que faltavam
        String sqlInsertEntrega = "INSERT INTO TB_ENTREGA (ID_REMETENTE, ID_DESTINATARIO, ID_PRODUTO, " +
                "QTD_PEDIDA, DATA_ENVIO, DATA_ENTREGA, TRANSPORTADORA, VALOR_FRETE, STATUS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID_ENTREGA";

        String sqlUpdateEstoque = "UPDATE TB_PRODUTO SET QUANTIDADE = QUANTIDADE - ? WHERE ID_PRODUTO = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false); // Início da transação

            try {
                // 1. Verificar estoque
                int estoqueAtual = 0;
                try (PreparedStatement stmtEstoque = conn.prepareStatement(sqlCheckEstoque)) {
                    stmtEstoque.setLong(1, entrega.getIdProduto());
                    ResultSet rs = stmtEstoque.executeQuery();
                    if (rs.next()) estoqueAtual = rs.getInt("QUANTIDADE");
                }

                if (estoqueAtual < entrega.getQtdPedida()) {
                    throw new RuntimeException("Estoque insuficiente! Disponível: " + estoqueAtual);
                }

                // 2. Inserir Entrega e obter o ID gerado
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
                    stmtIns.setString(9, "PENDENTE"); // Certifique-se de que o 9º parâmetro existe

                    ResultSet rs = stmtIns.executeQuery();
                    if (rs.next()) idGerado = rs.getLong(1);
                }

                // 3. Gerar e Gravar Código de Rastreio (ENC...)
                int anoAtual = LocalDate.now().getYear();
                String codigoRastreio = String.format("ENC%d%05d", anoAtual, idGerado);

                String sqlUpdateCodigo = "UPDATE TB_ENTREGA SET CODIGO_PEDIDO = ? WHERE ID_ENTREGA = ?";
                try (PreparedStatement stmtCod = conn.prepareStatement(sqlUpdateCodigo)) {
                    stmtCod.setString(1, codigoRastreio);
                    stmtCod.setLong(2, idGerado);
                    stmtCod.executeUpdate();
                }

                // 4. Baixa no Estoque do Produto
                try (PreparedStatement stmtUpdEstoque = conn.prepareStatement(sqlUpdateEstoque)) {
                    stmtUpdEstoque.setInt(1, entrega.getQtdPedida());
                    stmtUpdEstoque.setLong(2, entrega.getIdProduto());
                    stmtUpdEstoque.executeUpdate();
                }

                // Atualiza o objeto para retorno
                entrega.setCodigoPedido(codigoRastreio);
                entrega.setIdEntrega(idGerado);

                conn.commit(); // Salva todas as operações se nada falhou

            } catch (Exception e) {
                conn.rollback(); // Cancela tudo se houver qualquer erro
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao processar entrega: " + e.getMessage());
        }
    }

    public List<Entrega> listarEntregas() {
        String sql = "SELECT E.*, R.NOME AS NOME_REMETENTE, D.NOME AS NOME_DESTINATARIO, " +
                "P.NOME_PRODUTO, P.PRECO_PRODUTO, P.QUANTIDADE, E.QTD_PEDIDA " + // Adicionado P.QUANTIDADE aqui
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

                // Preenche a Quantidade e Calcula Total
                BigDecimal preco = rs.getBigDecimal("PRECO_PRODUTO");
                int qtdEnviada = rs.getInt("QTD_PEDIDA");
                if (preco != null) {
                    // Multiplica o preço pela quantidade da entrega, não pelo estoque total
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
                "ID_PRODUTO=?, DATA_ENVIO=?, DATA_ENTREGA=?, TRANSPORTADORA=?, VALOR_FRETE=?, STATUS=? WHERE ID_ENTREGA=?";

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
            stmt.setLong(9, entrega.getIdEntrega());

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
        // SQL para descobrir qual produto e qual quantidade devolver
        String sqlBusca = "SELECT ID_PRODUTO, QTD_PEDIDA FROM TB_ENTREGA WHERE ID_ENTREGA = ?";
        // SQL para devolver os itens ao estoque do produto
        String sqlDevolverEstoque = "UPDATE TB_PRODUTO SET QUANTIDADE = QUANTIDADE + ? WHERE ID_PRODUTO = ?";
        // SQL para deletar a entrega de fato
        String sqlDelete = "DELETE FROM TB_ENTREGA WHERE ID_ENTREGA = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false); // Inicia transação (Tudo ou Nada)

            try {
                long idProduto = 0;
                int qtdParaDevolver = 0;

                // 1. Busca os dados da entrega antes de apagá-la
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

                conn.commit(); // Confirma as duas operações no banco
            } catch (Exception e) {
                conn.rollback(); // Se algo falhar, desfaz a devolução e a deleção
                throw new RuntimeException("Erro ao deletar e devolver estoque: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}