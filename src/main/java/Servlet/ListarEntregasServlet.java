package Servlet;

import DAO.EntregaDAO;
import Model.Entrega;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/listar-entregas")
public class ListarEntregasServlet extends HttpServlet {
    private String message;

    //inicialização como configurações de recursos ou estabelecimento de conexões com o banco
    @Override
    public void init () {
        message = "endpoint-listar-entregas";
    }

    //solicita dados do servidor
//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        response.setContentType("text/html; charset=UTF-8");
//
//        PrintWriter pw = response.getWriter();
//
//        pw.println("<!DOCTYPE html>");
//        pw.println("<html>");
//        pw.println("<head>");
//        pw.println("  <meta charset='UTF-8'>");
//        pw.println("  <title>Lista de Entregas</title>");
//        pw.println("  <style>");
//        pw.println("    body { font-family: Arial, sans-serif; }");
//        pw.println("    h1 { color: #333; }");
//        pw.println("    table { border-collapse: collapse; margin-top: 20px; width: 100%; max-width: 900px; }");
//        pw.println("    th, td { border: 1px solid #ccc; padding: 8px 12px; }");
//        pw.println("    th { background-color: #f2f2f2; text-align: left; }");
//        pw.println("    tr:nth-child(even) { background-color: #fafafa; }");
//        pw.println("    a { text-decoration: none; color: #0066cc; }");
//        pw.println("  </style>");
//        pw.println("</head>");
//        pw.println("<body>");
//
//        pw.println("<h1>Lista de Entregas</h1>");
//        pw.println("<a href='form-entrega.jsp'>Cadastrar nova entrega</a><br><br>");
//
//        pw.println("<table>");
//        pw.println("  <tr>");
//        pw.println("    <th>Transportadora</th>");
//        pw.println("    <th>Código Pedido</th>");
//        pw.println("    <th>ID Cliente</th>");
//        pw.println("    <th>ID Produto</th>");
//        pw.println("    <th>Data Envio</th>");
//        pw.println("    <th>Data Entrega</th>");
//        pw.println("    <th>Valor Frete</th>");
//        pw.println("  </tr>");
//
//        // por enquanto só uma linha de exemplo (ou deixa vazio)
//        pw.println("  <tr>");
//        pw.println("    <td>(vazio)</td>");
//        pw.println("    <td>(vazio)</td>");
//        pw.println("    <td>(vazio)</td>");
//        pw.println("    <td>(vazio)</td>");
//        pw.println("    <td>(vazio)</td>");
//        pw.println("    <td>(vazio)</td>");
//        pw.println("    <td>(vazio)</td>");
//        pw.println("  </tr>");
//
//        pw.println("</table>");
//
//        pw.println("</body>");
//        pw.println("</html>");
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // 1. Buscar lista no DAO
        EntregaDAO entregaDAO = new EntregaDAO();
        List<Entrega> listaEntregas = entregaDAO.listarEntregas();

        // 2. Colocar na requisição
        request.setAttribute("listaEntregas", listaEntregas);

        // 3. Encaminhar para o JSP
        request.getRequestDispatcher("lista-entregas.jsp")
                .forward(request, response);
    }


    //retira o servlet de serviço
    @Override
    public void destroy () {

    }
}
