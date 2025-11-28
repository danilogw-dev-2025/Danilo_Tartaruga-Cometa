package Servlet;

import DAO.EntregaDAO;
import Model.Entrega;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        //Olá
        PrintWriter out = response.getWriter();
        out.println("<html><body>" );
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    @Override
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        //pega os dados e retorna como saida de texto
        PrintWriter pw = response.getWriter();

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
        entregaDAO.cadastrarEntrega(entrega);

        //salvar no banco
//        response.sendRedirect("listar-entregas");
        response.sendRedirect(request.getContextPath() + "/listar-entregas");


//        pw.println("<!DOCTYPE html>");
//        pw.println("<html>");
//        pw.println("<head>");
//        pw.println("  <meta charset='UTF-8'>");
//        pw.println("  <title>Resumo da Entrega</title>");
//        pw.println("  <style>");
//        pw.println("    body { font-family: Arial, sans-serif; }");
//        pw.println("    h1 { color: #333; }");
//        pw.println("    table { border-collapse: collapse; margin-top: 20px; }");
//        pw.println("    th, td { border: 1px solid #ccc; padding: 8px 12px; }");
//        pw.println("    th { background-color: #f2f2f2; text-align: left; }");
//        pw.println("  </style>");
//        pw.println("</head>");
//        pw.println("<body>");
//
//        pw.println("<h1>Entrega cadastrada com sucesso!</h1>");
//
//        pw.println("<table>");
//        pw.println("  <tr><th>Campo</th><th>Valor</th></tr>");
//        pw.println("  <tr><td>Transportadora</td><td>" + transportadora + "</td></tr>");
//        pw.println("  <tr><td>Código do Pedido</td><td>" + codigo_pedido + "</td></tr>");
//        pw.println("  <tr><td>ID do Cliente</td><td>" + id_cliente + "</td></tr>");
//        pw.println("  <tr><td>ID do Produto</td><td>" + id_produto + "</td></tr>");
//        pw.println("  <tr><td>Data de Envio</td><td>" + data_envio + "</td></tr>");
//        pw.println("  <tr><td>Data de Entrega</td><td>" + data_entrega + "</td></tr>");
//        pw.println("  <tr><td>Valor do Frete</td><td>" + valor_frete + "</td></tr>");
//
//        pw.println("</table>");
//
//        pw.println("<br><a href='form-entrega.jsp'>Voltar ao formulário</a>");
//
//        pw.println("</body>");
//        pw.println("</html>");

    }

    //retira o servlet de serviço
    @Override
    public void destroy () {

    }

}
