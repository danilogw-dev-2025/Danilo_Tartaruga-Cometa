package BO;

import DAO.ClienteDAO;
import Model.Cliente;

/**
 * Camada de Negócio (Business Object) para Clientes.
 * 1. Validação Estrutural: Garante que nomes não sejam vazios e documentos tenham o tamanho correto.
 * 2. Higienização de Dados: Remove caracteres não numéricos de CPF/CNPJ antes da persistência.
 * 3. Abstração de Erros de Banco: Captura RuntimeExceptions do DAO (ex: restrições UNIQUE)
 * e as relança como Exception com mensagens legíveis para o Front-end.
 * 4. Orquestração de Fluxo: Decide entre cadastrar (novo) ou editar (ID existente) de forma transparente.
 */

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