package Servlet;

//ProdutoServlet → @WebServlet("/produto") (salvar)

import DAO.ProdutoDAO;
import Model.Produto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/produto-servlet")
public class ProdutoServlet extends HttpServlet {
    private String message;

    @Override
    public void init() {
        message = "endpoint-produto";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println("<h1>Olá, Tomcat + Gradle + Java 8!</h1>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter pw = response.getWriter();

        String codigoProduto = request.getParameter("codigoProduto");
        String nomeProduto = request.getParameter("nomeProduto");
        String descricao = request.getParameter("descricao");
        String preco = request.getParameter("preco");
        String quantidade = request.getParameter("quantidade");

        BigDecimal preco_ = new BigDecimal(preco);
        Long quantidade_ = Long.parseLong(quantidade);

        Produto produto = new Produto(
                codigoProduto,
                nomeProduto,
                descricao,
                preco_,
                quantidade_
        );

        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.cadastrarProduto(produto);

        response.sendRedirect(request.getContextPath() + "/listar-produtos");
    }

    @Override
    public void destroy () {


    }
}

