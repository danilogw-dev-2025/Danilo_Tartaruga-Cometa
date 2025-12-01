package Servlet;

import DAO.ClienteDAO;
import DAO.ProdutoDAO;
import Model.Cliente;
import Model.Produto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/nova-entrega") // <<< IMPORTANTE
public class NovaEntregaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, javax.servlet.ServletException {

        ClienteDAO clienteDAO = new ClienteDAO();
        ProdutoDAO produtoDAO = new ProdutoDAO();

        List<Cliente> listaClientes = clienteDAO.listarClientes();
        List<Produto> listaProdutos = produtoDAO.listarProdutos();

        request.setAttribute("listaClientes", listaClientes);
        request.setAttribute("listaProdutos", listaProdutos);

        request.getRequestDispatcher("form-entrega.jsp")
                .forward(request, response);
    }
}
