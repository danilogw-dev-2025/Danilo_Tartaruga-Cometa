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

        // --- LÓGICA DE TRATAMENTO DA MÁSCARA DO FRETE ---
        String valorFreteComMascara = request.getParameter("valor_frete");
        BigDecimal valorFrete = BigDecimal.ZERO;

        if (valorFreteComMascara != null && !valorFreteComMascara.isEmpty()) {
            try {
                // Remove o prefixo e espaços, ex: "R$ 1.234,56" -> "1.234,56"
                String valorLimpo = valorFreteComMascara.replace("R$", "").trim();

                // Remove os separadores de milhar, ex: "1.234,56" -> "1234,56"
                valorLimpo = valorLimpo.replace(".", "");

                // Substitui a vírgula decimal por ponto, ex: "1234,56" -> "1234.56"
                valorLimpo = valorLimpo.replace(",", ".");

                valorFrete = new BigDecimal(valorLimpo);

            } catch (NumberFormatException e) {
                // Trata o erro se o valor não puder ser convertido (útil para feedback ao usuário)
                // Se houver erro, a lógica de erro abaixo tratará.
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

            response.sendRedirect(request.getContextPath() + "/entrega-servlet");

        } catch (Exception e) {
            ClienteDAO clienteDAO = new ClienteDAO();
            ProdutoDAO produtoDAO = new ProdutoDAO();

            request.setAttribute("listaClientes", clienteDAO.listarClientes());
            request.setAttribute("listaProdutos", produtoDAO.listarProdutos());

            request.setAttribute("erroMsg", e.getMessage());
            request.setAttribute("entrega", entrega);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/form-entrega.jsp");
            dispatcher.forward(request, response);
        }
    }
}