package Servlet;

import BO.EntregaBO;
import DAO.ClienteDAO;
import DAO.EntregaDAO;
import DAO.ProdutoDAO;
import Model.Cliente;
import Model.Entrega;
import Model.Produto;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador (Servlet) para a entidade Entrega.
 * 1. Preparação de Contexto (GET): Nas ações 'novo' e 'editar', o servlet realiza
 * múltiplas consultas (Clientes e Produtos) para popular os campos de seleção do formulário.
 * 2. Parsing de Moeda: Implementa lógica robusta para remover símbolos (R$),
 * pontos de milhar e converter vírgulas em pontos antes da persistência do frete.
 * 3. Gerenciamento de Exceções de Negócio: Captura erros lançados pelo EntregaBO
 * (como falta de estoque ou datas inválidas) e os reencaminha para a View com feedback claro.
 * 4. Orquestração de Relacionamentos: Captura múltiplos IDs estrangeiros (FKs)
 * da requisição e reconstrói o objeto de domínio Entrega para processamento.
 * 5. Navegação Condicional: Redireciona o fluxo baseado no sucesso da operação,
 * utilizando parâmetros de URL (?status=sucesso) para disparar alertas visuais no JSP.
 */
@WebServlet("/entrega-servlet")
public class EntregaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("idEntrega");

        EntregaDAO entregaDAO = new EntregaDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        ProdutoDAO produtoDAO = new ProdutoDAO();
        EntregaBO entregaBO = new EntregaBO();

        try {
            if ("delete".equals(action) && idParam != null) {
                try {
                    entregaBO.deletar(Long.parseLong(idParam));
                } catch (Exception e) {
                    request.setAttribute("erroMsg", e.getMessage());
                }
                List<Entrega> lista = entregaDAO.listarEntregas();
                request.setAttribute("listaEntregas", lista);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/lista-entregas.jsp");
                dispatcher.forward(request, response);
                return;
            }

            if ("novo".equals(action) || "editar".equals(action)) {
                List<Cliente> listaClientes = clienteDAO.listarClientes();
                List<Produto> listaProdutos = produtoDAO.listarProdutos();

                request.setAttribute("listaClientes", listaClientes);
                request.setAttribute("listaProdutos", listaProdutos);

                if ("editar".equals(action) && idParam != null) {
                    Entrega entrega = entregaDAO.buscarPorId(Long.parseLong(idParam));
                    request.setAttribute("entrega", entrega);
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("/form-entrega.jsp");
                dispatcher.forward(request, response);
                return;
            }

            List<Entrega> lista = entregaDAO.listarEntregas();
            request.setAttribute("listaEntregas", lista);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/lista-entregas.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/entrega-servlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("idEntrega");
        String transportadora = request.getParameter("transportadora");
        String codigoPedido = request.getParameter("codigo_pedido");
        String dataEnvio = request.getParameter("data_envio");
        String dataEntrega = request.getParameter("data_entrega");
        String status = request.getParameter("status");
        String qtdPedidaStr = request.getParameter("qtd_pedida");

        int qtdPedida = 1; // Valor padrão
        if (qtdPedidaStr != null && !qtdPedidaStr.isEmpty()) {
            qtdPedida = Integer.parseInt(qtdPedidaStr);
        }

        // Captura os DOIS clientes e o produto
        Long idRemetente = Long.parseLong(request.getParameter("id_remetente"));
        Long idDestinatario = Long.parseLong(request.getParameter("id_destinatario"));
        Long idProduto = Long.parseLong(request.getParameter("id_produto"));

        String valorFreteComMascara = request.getParameter("valor_frete");
        BigDecimal valorFrete = BigDecimal.ZERO;

        if (valorFreteComMascara != null && !valorFreteComMascara.isEmpty()) {
            try {

                String valorLimpo = valorFreteComMascara.replace("R$", "").trim();
                valorLimpo = valorLimpo.replace(".", "");
                valorLimpo = valorLimpo.replace(",", ".");
                valorFrete = new BigDecimal(valorLimpo);

            } catch (NumberFormatException e) {
                throw new ServletException("O valor do frete está em um formato inválido. Use apenas números.");
            }
        }

        Entrega entrega = new Entrega(
                idRemetente,
                idDestinatario,
                idProduto,
                codigoPedido,
                dataEnvio,
                dataEntrega,
                transportadora,
                valorFrete,
                status,
                qtdPedida
        );

        if (idStr != null && !idStr.isEmpty()) {
            entrega.setIdEntrega(Long.parseLong(idStr));
        }

        try {
            EntregaBO entregaBO = new EntregaBO();
            entregaBO.salvar(entrega);

            response.sendRedirect(request.getContextPath() + "/entrega-servlet?status=sucesso");

        } catch (Exception e) {
            ClienteDAO clienteDAO = new ClienteDAO();
            ProdutoDAO produtoDAO = new ProdutoDAO();

            request.setAttribute("listaClientes", clienteDAO.listarClientes());
            request.setAttribute("listaProdutos", produtoDAO.listarProdutos());
            response.sendRedirect(request.getContextPath() + "/form-entrega.jsp?status=erro");
            request.setAttribute("erroMsg", e.getMessage());
            request.setAttribute("entrega", entrega);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/form-entrega.jsp");
            dispatcher.forward(request, response);
        }
    }
}