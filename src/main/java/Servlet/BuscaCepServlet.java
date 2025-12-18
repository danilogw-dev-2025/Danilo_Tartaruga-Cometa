package Servlet;

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

/**
 * Servlet de Integração com API REST (ViaCEP).
 * 1. Consumo de WebService: Utiliza 'HttpURLConnection' para realizar chamadas
 * GET síncronas ao servidor do ViaCEP, atuando como um intermediário (Proxy).
 * 2. Higienização de Parâmetros: Aplica regex para garantir que apenas números
 * sejam enviados à API, prevenindo erros de formato no request.
 * 3. Gestão de Timeouts: Define limites de conexão e leitura (5 segundos),
 * garantindo que o seu sistema não fique travado caso o serviço externo esteja instável.
 * 4. Resposta em JSON: Define 'application/json' como o tipo de conteúdo,
 * facilitando o parse imediato pelo JavaScript do 'form-cliente.jsp'.
 * 5. Tratamento de Erros HTTP: Mapeia códigos de resposta (como 404 ou 500)
 * e devolve um objeto JSON de erro padronizado para o usuário.
 */

@WebServlet("/BuscaCepServlet")
public class BuscaCepServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cep = request.getParameter("cep");

        if (cep != null) {
            cep = cep.replaceAll("[^0-9]", "");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().print("{\"erro\": true, \"mensagem\": \"CEP nao fornecido.\"}");
            return;
        }

        String urlString = "https://viacep.com.br/ws/" + cep + "/json/";

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // Retorna o JSON LIDO da API ViaCEP
                out.print(content.toString());

            } else {
                response.setStatus(responseCode);
                out.print("{\"erro\": true, \"mensagem\": \"Erro HTTP: " + responseCode + "\"}");
            }

            connection.disconnect();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"erro\": true, \"mensagem\": \"Erro interno no servidor.\" }");
            e.printStackTrace();
        }
    }
}
