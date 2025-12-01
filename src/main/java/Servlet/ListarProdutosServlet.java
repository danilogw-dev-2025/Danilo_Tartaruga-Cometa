package Servlet;

import DAO.ProdutoDAO;
import Model.Produto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//ListarProdutosServlet → @WebServlet("/lista-produtos")
@WebServlet("/listar-produtos")
public class ListarProdutosServlet extends HttpServlet {
    private String message;

    @Override
    public void init() {
        message = "endpoint-listar-produtos";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> listarProduto = produtoDAO.listarProdutos();

        request.setAttribute("listarProduto", listarProduto);

        request.getRequestDispatcher("lista-produtos.jsp")
                .forward(request, response);
    }

    @Override
    public void destroy () {

    }
}
