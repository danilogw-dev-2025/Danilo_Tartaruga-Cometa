package BO;

import DAO.ClienteDAO;
import DAO.EntregaDAO;
import DAO.ProdutoDAO;
import Model.Cliente;
import Model.Entrega;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EntregaBO {

    private EntregaDAO entregaDAO;

    public EntregaBO() {
        this.entregaDAO = new EntregaDAO();
    }

    public void salvar(Entrega entrega) throws Exception {

        if (entrega.getValorFrete() == null || entrega.getValorFrete().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("O valor do frete deve ser maior que zero!");
        }

        if (entrega.getDataEnvio() == null || entrega.getDataEnvio().isEmpty()) {
            throw new Exception("A data de envio é obrigatória!");
        }
        if (entrega.getDataEntrega() == null || entrega.getDataEntrega().isEmpty()) {
            throw new Exception("A data de entrega é obrigatória!");
        }

        LocalDate envio = LocalDate.parse(entrega.getDataEnvio());
        LocalDate chegada = LocalDate.parse(entrega.getDataEntrega());

        if (chegada.isBefore(envio)) {
            throw new Exception("Data inválida! A entrega não pode acontecer antes do envio.");
        }

        if (entrega.getIdRemetente().equals(entrega.getIdDestinatario())) {
            throw new Exception("Erro: O Remetente e o Destinatário não podem ser a mesma pessoa!");
        }

        if (entrega.getStatus() == null || entrega.getStatus().isEmpty()) {
            entrega.setStatus("PENDENTE");
        }

        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente remetente = clienteDAO.buscarPorId(entrega.getIdRemetente());
        Cliente destinatario = clienteDAO.buscarPorId(entrega.getIdDestinatario());

        if (remetente != null && destinatario != null) {
            boolean mesmoEndereco = remetente.getCep().equals(destinatario.getCep()) &&
                                    remetente.getNumeroCasa() == destinatario.getNumeroCasa();
            if (mesmoEndereco) {
                throw new Exception("Erro: O endereço do Destinatário não pode ser igual ao do Remetente!");
            }
        }

        if (entrega.getStatus() == null || entrega.getStatus().isEmpty()) {
            entrega.setStatus("PENDENTE");
        }

        // 3. Bloco de Salvamento Único com Inteligência de Estoque
        try {
            if (entrega.getIdEntrega() != null && entrega.getIdEntrega() > 0) {
                // 1. Busca como a entrega ESTÁ no banco antes de alterar
                Entrega antiga = entregaDAO.buscarPorId(entrega.getIdEntrega());

                if (antiga != null) {
                    // Bloqueio de segurança
                    if ("REALIZADA".equals(antiga.getStatus()) || "CANCELADA".equals(antiga.getStatus())) {
                        throw new Exception("Erro: Não é permitido alterar uma entrega finalizada.");
                    }

                    // CENÁRIO: Mudança de Quantidade (Inteligência de Estoque)
                    // Se a nova qtd é 2 e a antiga era 1, a diferença é 1.
                    int diferenca = entrega.getQtdPedida() - antiga.getQtdPedida();

                    if (diferenca != 0) {
                        // Se for aumento (diferença positiva), checa se tem no estoque
                        if (diferenca > 0) {
                            ProdutoDAO produtoDAO = new ProdutoDAO();
                            Model.Produto p = produtoDAO.buscarPorId(entrega.getIdProduto());
                            if (p.getQuantidade() < diferenca) {
                                throw new Exception("Estoque insuficiente! Você precisa de mais " + diferenca + " unidade(s), mas só há " + p.getQuantidade() + " disponível.");
                            }
                        }
                        // Sincroniza o estoque no banco
                        entregaDAO.ajustarQuantidadeEstoque(entrega.getIdProduto(), diferenca);
                    }
                }

                // 2. Só agora salva os novos dados da entrega (como a nova quantidade)
                entregaDAO.editarEntrega(entrega);


            } else {
                // --- NOVO CADASTRO ---
                entregaDAO.cadastrarEntrega(entrega);
            }

        } catch (RuntimeException e) {
            // Tratamento de erros de banco (Unique Constraints, etc)
            String msg = e.getMessage().toLowerCase();
            if (msg.contains("codigo_pedido")) {
                throw new Exception("Erro: Já existe uma entrega com este Código de Rastreio.");
            }
            throw new Exception("Erro interno ao salvar: " + e.getMessage());
        }
    }

    public void deletar(Long id) throws Exception {
        Entrega entregaNoBanco = entregaDAO.buscarPorId(id);
        if (entregaNoBanco == null) throw new Exception("Entrega não encontrada.");

        if ("REALIZADA".equals(entregaNoBanco.getStatus())) {
            throw new Exception("Erro: Não é permitido excluir uma entrega REALIZADA.");
        }

        // Altere para o método que você criou com inteligência de estoque:
        entregaDAO.deletarEntregaComDevolucao(id);
    }
}