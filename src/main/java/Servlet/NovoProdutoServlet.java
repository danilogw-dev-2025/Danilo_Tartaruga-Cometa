package Servlet;
//NovoProdutoServlet → @WebServlet("/novo-produto")

import DAO.ProdutoDAO;
import Model.Produto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/novo-produto")
public class NovoProdutoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, javax.servlet.ServletException {

        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> listarProduto = produtoDAO.listarProdutos();

        request.setAttribute("listarProduto", listarProduto);

        request.getRequestDispatcher("form-produto.jsp")
                .forward(request, response);
        }
    }

