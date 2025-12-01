package Servlet;


//ListarClientesServlet → @WebServlet("/lista-clientes.jsp")
//
//Busca no ClienteDAO e manda para lista-clientes.jsp.jsp

import DAO.ClienteDAO;
import Model.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/listar-clientes")
public class ListarClientesServelt extends HttpServlet {
    private  String message;

    @Override
    public void init() {
        message = "endpoint-listar-clientes";
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
       throws IOException, ServletException {

        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> listarCliente = clienteDAO.listarClientes();

        request.setAttribute("listarCliente", listarCliente);

        request.getRequestDispatcher("lista-clientes.jsp")
                .forward(request, response);
    }

    @Override
    public void destroy () {

    }
}
