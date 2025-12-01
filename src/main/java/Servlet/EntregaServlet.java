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


    }

    //retira o servlet de serviço
    @Override
    public void destroy () {

    }

}
