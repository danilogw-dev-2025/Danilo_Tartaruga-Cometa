package Servlet;

import BO.ProdutoBO;
import DAO.ProdutoDAO;
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
 * Controlador (Servlet) para a entidade Produto.

 * 1. Controle de Estado (GET): Consulta dependências de entregas para cada produto
 * da lista, habilitando ou desabilitando ações de exclusão conforme a regra de negócio.
 * 2. Conversão Financeira (POST): Processa strings provenientes de máscaras jQuery (R$ 0.000,00),
 * limpando caracteres não numéricos para a correta instanciação de objetos BigDecimal.
 * 3. Gerenciamento de Formulário: Suporta tanto a criação de novos itens quanto a
 * edição de existentes, recuperando dados pelo ID e encaminhando ao 'form-produto.jsp'.
 * 4. Tratamento de Exceções: Em caso de falha na persistência, mantém o estado do
 * objeto 'produto' na requisição para que o usuário não perca os dados digitados ao retornar ao formulário.
 * 5. Redirecionamento Padrão: Implementa o fluxo 'PRG' (Post-Redirect-Get) para
 * evitar duplicidade de cadastros ao atualizar o navegador.
 */

@WebServlet("/produto-servlet")
public class ProdutoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("idProduto");

        ProdutoDAO produtoDAO = new ProdutoDAO();
        ProdutoBO produtoBO = new ProdutoBO();

        try {

            if ("delete".equals(action) && idParam != null) {
                Long idProduto = Long.parseLong(idParam);
                produtoBO.deletar(idProduto);
                response.sendRedirect(request.getContextPath() + "/produto-servlet");
                return;
            }

            if ("editar".equals(action) && idParam != null) {
                Long idProduto = Long.parseLong(idParam);
                Produto produto = produtoDAO.buscarPorId(idProduto);
                request.setAttribute("produto", produto);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/form-produto.jsp");
                dispatcher.forward(request, response);
                return;
            }

            List<Produto> lista = produtoDAO.listarProdutos();

            for (Produto p: lista) {
                p.setEmUso(produtoDAO.isProdutoVinculado(p.getIdProduto()));
            }

            request.setAttribute("listarProduto", lista);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/lista-produtos.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/produto-servlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("idProduto");
        String nomeProduto = request.getParameter("nomeProduto");
        String descricao = request.getParameter("descricao");
        String precoStr = request.getParameter("preco");
        String quantidadeStr = request.getParameter("quantidade");


        BigDecimal preco = BigDecimal.ZERO;

        if (precoStr != null && !precoStr.isEmpty()) {
            precoStr = precoStr
                    .replace("R$", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .trim();

            preco = new BigDecimal(precoStr);
        }


        Long quantidade = (quantidadeStr != null && !quantidadeStr.isEmpty()) ? Long.parseLong(quantidadeStr) : 0L;

        Produto produto = new Produto(
                nomeProduto,
                descricao,
                preco,
                quantidade
        );

        if (idStr != null && !idStr.isEmpty()) {
            produto.setIdProduto(Long.parseLong(idStr));
        }

        try {
            ProdutoBO produtoBO = new ProdutoBO();
            produtoBO.salvar(produto);

            response.sendRedirect(request.getContextPath() + "/produto-servlet?status=sucesso");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/produto-servlet?status=erro");
            request.setAttribute("erroMsg", e.getMessage());
            request.setAttribute("produto", produto);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/form-produto.jsp");
            dispatcher.forward(request, response);
        }
    }
}