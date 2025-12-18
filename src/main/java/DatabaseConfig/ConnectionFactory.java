package DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Fábrica de Conexões JDBC para o ecossistema PostgreSQL.
 * 1. Centralização de Configurações: Reúne as credenciais e a URL do banco em
 * um único local, permitindo mudanças globais sem alterar os DAOs.
 * 2. Registro do Driver: Força o registro do 'org.postgresql.Driver', essencial
 * para servidores de aplicação que não suportam carregamento automático JDBC 4.0+.
 * 3. Abstração de Exceções: Converte 'SQLExceptions' (erros de banco) em
 * 'RuntimeExceptions', simplificando o código dos DAOs e garantindo que falhas de
 * infraestrutura interrompam o fluxo se o banco estiver fora do ar.
 * 4. Padrão de Projeto Factory: Encapsula a complexidade da criação do objeto
 * 'Connection', entregando uma conexão pronta para uso via método estático.
 */

public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/trilhaGW"; // Alterar o nome do banco de dados
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do PostgreSQL não encontrado: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}
