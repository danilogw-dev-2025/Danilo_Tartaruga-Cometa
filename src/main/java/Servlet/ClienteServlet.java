package Servlet;

import DAO.ClienteDAO;
import Model.Cliente;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/cliente-servlet")
public class ClienteServlet extends HttpServlet {
    private String message;

    @Override
    public void init(){
        message = "endpoint-cliente";
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

        PrintWriter  pw = response.getWriter();

        String codigoCliente = request.getParameter("codigoCliente");
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String dataNascimento = request.getParameter("dataNascimento");
        String email = request.getParameter("email");


        Cliente cliente = new Cliente(

                codigoCliente,
                nome,
                cpf,
                dataNascimento,
                email
        );

        ClienteDAO  clienteDAO = new ClienteDAO();
        clienteDAO.cadastrarCliente(cliente);

        response.sendRedirect(request.getContextPath() + "/listar-clientes");
    }

    @Override
    public void destroy () {

    }
}
