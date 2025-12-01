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
