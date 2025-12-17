package BO;

import DAO.ClienteDAO;
import DAO.EntregaDAO;
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

        try {

            if (entrega.getIdEntrega() != null && entrega.getIdEntrega() > 0) {

                Entrega entregaNoBanco = entregaDAO.buscarPorId(entrega.getIdEntrega());
                if (entregaNoBanco != null) {
                    if ("REALIZADA".equals(entregaNoBanco.getStatus()) || "CANCELADA".equals(entregaNoBanco.getStatus())) {
                        throw new Exception("Erro: Não é permitido alterar uma entrega finalizada (" + entregaNoBanco.getStatus() + ").");
                    }
                }

                entregaDAO.editarEntrega(entrega);

            } else {
                entregaDAO.cadastrarEntrega(entrega);
            }

        } catch (RuntimeException e) {
            String mensagemErro = e.getMessage().toLowerCase();

            if (mensagemErro.contains("duplicate key") || mensagemErro.contains("unique constraint")) {

                if (mensagemErro.contains("codigo_pedido")) {
                    throw new Exception("Erro: Já existe uma entrega cadastrada com este Código de Pedido (" + entrega.getCodigoPedido() + ").");
                }
            }

            throw new Exception("Erro interno ao salvar entrega: " + e.getMessage());
        }
    }

    public void deletar(Long id) throws Exception {
        Entrega entregaNoBanco = entregaDAO.buscarPorId(id);
        if (entregaNoBanco != null && "REALIZADA".equals(entregaNoBanco.getStatus())) {
            throw new Exception("Erro: Não é permitido excluir uma entrega REALIZADA.");
        }
        entregaDAO.deletarEntrega(id);
    }
}