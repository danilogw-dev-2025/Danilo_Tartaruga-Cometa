package DatabaseConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gerenciador de Configurações Externas do Banco de Dados.
 *
 * 1. Desacoplamento de Ambiente: Permite alterar o banco de dados (Dev, Teste, Produção)
 * sem recompilar o código Java, modificando apenas o arquivo 'db.properties'.
 * 2. Segurança: Facilita a exclusão de senhas do controle de versão (Git),
 * mantendo dados sensíveis apenas no servidor de hospedagem.
 * 3. Carregamento Estático Eficiente: Garante a leitura única dos parâmetros via
 * 'InputStream', disponibilizando-os de forma global e rápida através de métodos estáticos.
 * 4. Tratamento de Falha Crítica: Interrompe a execução do sistema caso o arquivo de
 * configuração não seja encontrado, prevenindo erros em cascata nos DAOs.
 */

public class DatabaseConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
                System.exit(1);
            }

            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDbUrl() {

        return properties.getProperty("db.url");
    }

    public static String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }
}