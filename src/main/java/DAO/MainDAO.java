//package DAO;
//
//import Model.Cliente;
//import Model.Endereco;
//import Model.Entrega;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//public class MainDAO {
//    public static void main(String[] args) {
//
//        // ---------- TESTE CLIENTE ----------
//        ClienteDAO clienteDAO = new ClienteDAO();
//
//        // construtor: (codigoCliente, nome, cpf, dataNascimento, email, senha)
//        Cliente novoCliente = new Cliente(
//                "CLI001",
//                "Danilo Mendes",
//                "71001450100",
//                "1998-10-10",
//                "danilo@example.com",
//                "senha123"
//        );
//
//        clienteDAO.cadastrarCliente(novoCliente);
//        System.out.println("Cliente cadastrado!");
//
//        List<Cliente> clientes = clienteDAO.listarClientes();
//        System.out.println("\n=== CLIENTES ===");
//        for (Cliente c : clientes) {
//            System.out.println(c);
//        }
//
//        // pegar o último cliente da lista (só pra ter um idCliente pra usar nos outros testes)
//        if (clientes.isEmpty()) {
//            System.out.println("Nenhum cliente encontrado. Encerrando teste.");
//            return;
//        }
//
//        Cliente clienteRecente = clientes.get(clientes.size() - 1);
//        Long idCliente = clienteRecente.getIdCliente();
//        System.out.println("\nID do cliente usado nos próximos testes: " + idCliente);
//
//        // ---------- TESTE ENDERECO ----------
//        EnderecoDAO enderecoDAO = new EnderecoDAO();
//
//        // construtor: (idCliente, estado, cidade, bairro, rua, cep, numeroCasa)
//        Endereco novoEndereco = new Endereco(
//                idCliente,
//                "PE",
//                "Recife",
//                "Boa Viagem",
//                "Av. Boa Viagem",
//                "51011-000",
//                123
//        );
//
//        enderecoDAO.cadastrarEndereco(novoEndereco);
//        System.out.println("Endereço cadastrado!");
//
//        List<Endereco> enderecos = enderecoDAO.listarEnderecos();
//        System.out.println("\n=== ENDEREÇOS ===");
//        for (Endereco e : enderecos) {
//            System.out.println(e);
//        }
//
//        // ---------- TESTE ENTREGA ----------
//        EntregaDAO entregaDAO = new EntregaDAO();
//
//        // aqui estou assumindo que existe um produto com ID_PRODUTO = 1
//        Long idProduto = 1L;
//
//        // construtor: (idCliente, idProduto, codigoPedido, dataEnvio, dataEntrega, transportadora, valorFrete)
//        Entrega novaEntrega = new Entrega(
//                idCliente,
//                idProduto,
//                "PED001",
//                "2025-11-27",            // formato yyyy-MM-dd
//                "2025-11-30",
//                "Correios",
//                new BigDecimal("29.90")
//        );
//
//        entregaDAO.cadastrarEntrega(novaEntrega);
//        System.out.println("Entrega cadastrada!");
//
//        List<Entrega> entregas = entregaDAO.listarEntregas();
//        System.out.println("\n=== ENTREGAS ===");
//        for (Entrega en : entregas) {
//            System.out.println(en);
//        }
//
//        System.out.println("\nTESTES BÁSICOS FINALIZADOS.");
//    }
//}
