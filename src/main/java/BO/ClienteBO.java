package BO;

import DAO.ClienteDAO;
import Model.Cliente;

public class ClienteBO {

    private ClienteDAO clienteDAO;

    public ClienteBO() {
        this.clienteDAO = new ClienteDAO();
    }

    public void salvar(Cliente cliente) throws Exception {

        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new Exception("O nome do cliente é obrigatório!");
        }

        String docLimpo = cliente.getDocumento().replaceAll("[^0-9]", "");
        cliente.setDocumento(docLimpo);

        if (docLimpo.length() != 11 && docLimpo.length() != 14) {
            throw new Exception("Documento inválido! Deve ter 11 (CPF) ou 14 (CNPJ) números.");
        }

        if (docLimpo.length() == 11) {
            if (cliente.getNumeroCasa() <= 0) {
                throw new Exception("Erro: Para Pessoa Física, o número da casa é obrigatório!");
            }
        }

        try {

            if (cliente.getIdCliente() != null && cliente.getIdCliente() > 0) {
                clienteDAO.editarCliente(cliente);
            } else {
                clienteDAO.cadastrarCliente(cliente);
            }

        } catch (RuntimeException e) {

            String mensagemErro = e.getMessage().toLowerCase();

            if (mensagemErro.contains("duplicate key") || mensagemErro.contains("unique constraint")) {

                if (mensagemErro.contains("codigo_cliente")) {
                    throw new Exception("Erro: Já existe um cliente cadastrado com este Código (" + cliente.getCodigoCliente() + ").");
                }
                else if (mensagemErro.contains("documento")) {
                    throw new Exception("Erro: Já existe um cliente cadastrado com este CPF/CNPJ.");
                }
            }

            throw new Exception("Erro interno ao salvar cliente: " + e.getMessage());
        }
    }

    public void deletar(Long id) {
        clienteDAO.deletarCliente(id);
    }
}