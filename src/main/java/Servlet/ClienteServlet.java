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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            deletarCliente(request, response);
            return;
        }


        // 1. Se não veio id, redireciona
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/listar-clientes");
            return;
        }

        try {
            Long idCliente = Long.parseLong(idParam);

            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            // 2. Se não encontrou o cliente, volta
            if (cliente == null) {
                response.sendRedirect(request.getContextPath() + "/listar-clientes");
                return;
            }

            // 3. Guarda o objeto para o JSP
            request.setAttribute("cliente", cliente);

            // 4. Encaminha para o JSP de edição
            request.getRequestDispatcher("/editar-clientes.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // id inválido → volta
            response.sendRedirect(request.getContextPath() + "/listar-clientes");
        }

    }

    private void deletarCliente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Long idCliente = Long.parseLong(request.getParameter("idCliente"));

        ClienteDAO dao = new ClienteDAO();
        dao.deletarCliente(idCliente);

        response.sendRedirect(request.getContextPath() + "/listar-clientes");
    }



    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String idStr = request.getParameter("idCliente");
        //PrintWriter  pw = response.getWriter();

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

        if (idStr != null && !idStr.isEmpty()) {
            Long idCliente = Long.parseLong(idStr);
            cliente.setIdCliente(idCliente);
            clienteDAO.editarCliente(cliente);
        }
        else {
            clienteDAO.cadastrarCliente(cliente);
        }

        response.sendRedirect(request.getContextPath() + "/listar-clientes");
    }

    @Override
    public void destroy () {

    }
}
