package Servlet;

//ProdutoServlet → @WebServlet("/produto") (salvar)

import DAO.ClienteDAO;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String ipParam = request.getParameter("idProduto");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            deletarProduto(request, response);
            return;
        }

        if (ipParam ==  null || ipParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/listar-produtos");
            return;
        }

        try {
            Long idProduto = Long.parseLong(ipParam);

            ProdutoDAO produtoDAO = new ProdutoDAO();
            Produto produto = produtoDAO.buscarPorId(idProduto);

            if (produto == null) {
                response.sendRedirect(request.getContextPath() + "/listar-produtos");
                return;
            }

            request.setAttribute("produto", produto);
            request.getRequestDispatcher("/editar-produtos.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath());
        }
    }

    private void deletarProduto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Long idProduto = Long.parseLong(request.getParameter("idProduto"));

        ProdutoDAO dao = new ProdutoDAO();
        dao.deletarProduto(idProduto);

        response.sendRedirect(request.getContextPath() + "/listar-produtos");
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String idStr = request.getParameter("idProduto");
//        PrintWriter pw = response.getWriter();

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

        if (idStr != null && !idStr.isEmpty()) {
            Long idProduto = Long.parseLong(idStr);
            produto.setIdProduto(idProduto);
            produtoDAO.editarProdutos(produto);
        }
        else {
            produtoDAO.cadastrarProduto(produto);
        }

        response.sendRedirect(request.getContextPath() + "/listar-produtos");
    }

    @Override
    public void destroy () {


    }
}

