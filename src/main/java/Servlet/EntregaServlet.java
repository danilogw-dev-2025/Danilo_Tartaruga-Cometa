package Servlet;

import DAO.ClienteDAO;
import DAO.EntregaDAO;
import DAO.ProdutoDAO;
import Model.Cliente;
import Model.Entrega;
import Model.Produto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/entrega-servlet")
public class EntregaServlet extends HttpServlet {
    private String message;

    //inicialização como configurações de recursos ou estabelecimento de conexões com o banco
    @Override
    public void init () {
        message = "endpoint-entrega";
    }

    //solicita dados do servidor
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("idEntrega");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            deletarEntrega(request, response);
            return;
        }

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/listar-entregas");
            return;
        }

        try {
            Long idEntrega = Long.parseLong(idParam);

            EntregaDAO entregaDAO = new EntregaDAO();
            ClienteDAO clienteDAO = new ClienteDAO();
            ProdutoDAO produtoDAO = new ProdutoDAO();

            Entrega entrega = entregaDAO.buscarPorId(idEntrega);

            if (entrega == null) {
                response.sendRedirect(request.getContextPath() + "/listar-entregas");
                return;
            }

            List<Cliente> listaClientes = clienteDAO.listarClientes();
            List<Produto> listaProdutos = produtoDAO.listarProdutos();

            request.setAttribute("entrega", entrega);
            request.setAttribute("listaClientes", listaClientes);
            request.setAttribute("listaProdutos", listaProdutos);

            request.getRequestDispatcher("/editar-entregas.jsp").forward(request, response);

        } catch (NumberFormatException | ServletException e) {
            response.sendRedirect(request.getContextPath() + "/listar-entregas");
        }
    }
    private void deletarEntrega(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Long idEntrega = Long.parseLong(request.getParameter("idEntrega"));

        EntregaDAO dao = new EntregaDAO();
        dao.deletarEntrega(idEntrega);

        response.sendRedirect(request.getContextPath() + "/listar-entregas");
    }


    @Override
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String idStr = request.getParameter("idEntrega");

        //pega os parametros do form-entrega.jsp
        String transportadora = request.getParameter("transportadora");
        String codigo_pedido = request.getParameter("codigo_pedido");
        String id_cliente = request.getParameter("id_cliente");
        String id_produto = request.getParameter("id_produto");
        String data_envio = request.getParameter("data_envio");
        String data_entrega = request.getParameter("data_entrega");
        String valor_frete = request.getParameter("valor_frete");

        Long idCliente = Long.parseLong(id_cliente);
        Long idProduto = Long.parseLong(id_produto);

        BigDecimal valorFrete = new BigDecimal(valor_frete);

        // 2) Monta objeto Entrega (usa o construtor que você tem no Model.Entrega)
        Entrega entrega = new Entrega(
                idCliente,
                idProduto,
                codigo_pedido,
                data_envio,
                data_entrega,
                transportadora,
                valorFrete
        );
        EntregaDAO entregaDAO = new EntregaDAO();

        if (idStr != null && !idStr.isEmpty()) {
            Long idEntrega = Long.parseLong(idStr);
            entrega.setIdEntrega(idEntrega);
            entregaDAO.editarEntrega(entrega);
        }
        else {
            entregaDAO.cadastrarEntrega(entrega);
        }

        //salvar no banco
//        response.sendRedirect("listar-entregas");
        response.sendRedirect(request.getContextPath() + "/listar-entregas");


    }

    //retira o servlet de serviço
    @Override
    public void destroy () {

    }

}
