package Servlet;

import BO.ClienteBO;
import DAO.ClienteDAO;
import Model.Cliente;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@WebServlet("/cliente-servlet")
public class ClienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        ClienteDAO clienteDAO = new ClienteDAO();
        ClienteBO clienteBO = new ClienteBO();

        try {

            if ("delete".equals(action) && idParam != null) {
                Long idCliente = Long.parseLong(idParam);
                clienteBO.deletar(idCliente);
                response.sendRedirect(request.getContextPath() + "/cliente-servlet");
                return;
            }

            if ("editar".equals(action) && idParam != null) {
                Long idCliente = Long.parseLong(idParam);
                Cliente cliente = clienteDAO.buscarPorId(idCliente);

                request.setAttribute("cliente", cliente);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/form-cliente.jsp");
                dispatcher.forward(request, response);
                return;
            }

            List<Cliente> lista = clienteDAO.listarClientes();
            request.setAttribute("listarCliente", lista);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/lista-clientes.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cliente-servlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("idCliente");
        String codigoCliente = request.getParameter("codigoCliente");
        String nome = request.getParameter("nome");
        String documento = request.getParameter("documento");
        String estado = request.getParameter("estado");
        String cidade = request.getParameter("cidade");
        String bairro = request.getParameter("bairro");
        String rua = request.getParameter("rua");
        String numeroCasaStr = request.getParameter("numeroCasa");
        String cep = request.getParameter("cep");

        // TRATAMENTO DE ERRO: Se vier vazio, considera 0
        int numeroCasa = 0;
        if (numeroCasaStr != null && !numeroCasaStr.isEmpty()) {
            try {
                numeroCasa = Integer.parseInt(numeroCasaStr);
            } catch (NumberFormatException e) {
                numeroCasa = 0;
            }
        }


        Cliente cliente = new Cliente(
                codigoCliente,
                nome,
                documento,
                estado,
                cidade,
                bairro,
                rua,
                numeroCasa,
                cep
        );

        if (idStr != null && !idStr.isEmpty()) {
            cliente.setIdCliente(Long.parseLong(idStr));
        }

        try {
            ClienteBO clienteBO = new ClienteBO();
            clienteBO.salvar(cliente);

            response.sendRedirect(request.getContextPath() + "/cliente-servlet");

        } catch (Exception e) {

            request.setAttribute("erroMsg", e.getMessage());
            request.setAttribute("cliente", cliente);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/form-cliente.jsp");
            dispatcher.forward(request, response);
        }
    }
}