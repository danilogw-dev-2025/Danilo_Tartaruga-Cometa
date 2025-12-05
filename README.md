

## 🛠️ Tecnologias Utilizadas

* **Backend:** Java (JDK 8+), Servlets, JDBC.
* **Frontend:** JSP, HTML5, CSS3, JavaScript (Validações).
* **Banco de Dados:** PostgreSQL.
* **Servidor de Aplicação:** Apache Tomcat 9+.
* **Arquitetura:** MVC com camadas de DAO (Data Access Object) e BO (Business Object).
* **Gerenciamento de Dependências:** Gradle.

---

## 📋 Funcionalidades

* **Clientes:** Cadastro unificado de Pessoa Física (CPF) e Jurídica (CNPJ) com validações.
* **Produtos:** Gestão de estoque com cálculo automático de valor total.
* **Entregas:**
    * Vínculo de Remetente e Destinatário (com validação de integridade).
    * Bloqueio de datas inválidas (Entrega anterior ao Envio).
    * Cálculo automático de valor final (Produto + Frete).
    * Controle de Status (Pendente, Realizada, Cancelada) com bloqueio de edição para entregas finalizadas.

---

## 🚀 Guia de Instalação e Setup

Siga os passos abaixo para rodar a aplicação em seu ambiente local.

### 1. Configuração do Banco de Dados (PostgreSQL)

1.  Crie um banco de dados vazio no PostgreSQL (ex: `tartaruga_db`).
2.  Localize o script de criação das tabelas em:
    ```
    src/main/resources/schema.sql
    ```
3.  Execute todo o conteúdo deste script no seu banco de dados (via pgAdmin ou DBeaver).
    * *Isso criará as tabelas `TB_CLIENTE`, `TB_PRODUTO` e `TB_ENTREGA` com as chaves estrangeiras e constraints corretas.*

### 2. Configuração de Credenciais

É necessário configurar o acesso ao banco de dados para que a aplicação consiga se conectar.

1.  Abra o arquivo:
    ```
    src/main/resources/db.properties
    ```
2.  Altere as variáveis conforme o seu ambiente local:

```properties
# Exemplo de configuração
db.url=jdbc:postgresql://localhost:5432/tartaruga_db
db.user=seu_usuario_postgres
db.password=sua_senha_postgres
📦 Como Fazer o Deploy (Apache Tomcat)Opção A: Rodando via IDE (IntelliJ / Eclipse) - Recomendado para DevImporte o projeto como Gradle Project.Configure um Run/Debug Configuration apontando para o Tomcat Server (Local).Na aba "Deployment", adicione o artefato do projeto (daniiloGW_trilha:war exploded ou similar).Clique em Run.Opção B: Deploy Manual (Arquivo .WAR)Gere o arquivo .war executando o comando Gradle no terminal:Bash./gradlew build
O arquivo será gerado em build/libs/nome-do-projeto.war.Copie este arquivo .war.Cole dentro da pasta webapps do seu diretório de instalação do Apache Tomcat.Inicie o Tomcat (bin/startup.sh ou bin/startup.bat).🌍 Acesso à AplicaçãoApós iniciar o servidor, a aplicação estará disponível na seguinte URL:http://localhost:8080/trilha/(Nota: Se você renomeou o artefato no deploy, substitua /trilha pelo nome do contexto configurado).🛡️ Regras de Negócio Implementadas (Destaques)A aplicação conta com uma camada BO (Business Object) que blinda o banco de dados:Integridade Logística: O sistema impede que o Remetente e o Destinatário sejam a mesma pessoa na entrega.Segurança de Dados: Não é possível excluir Clientes ou Produtos que já possuem histórico de entregas (Cascade protegido).Auditoria: Entregas com status REALIZADA ou CANCELADA tornam-se imutáveis (não podem ser editadas ou excluídas).⚡ Dicas de Produtividade: Como Configurar os AliasesPara agilizar o desenvolvimento (Build > Deploy > Restart), sugerimos o uso de Aliases (atalhos de terminal).Siga os passos abaixo para configurar na sua máquina Linux/Mac:1. Identifique seu Shell e Edite o ArquivoAbra o terminal e digite o comando abaixo para editar o arquivo de configuração (escolha o comando conforme seu sistema):Bash# Para Bash (Maioria dos Linux)
nano ~/.bashrc

# Para Zsh (Mac e Linux modernos)
nano ~/.zshrc
2. Adicione os AtalhosVá até o final do arquivo aberto e cole o código abaixo.⚠️ ATENÇÃO: Ajuste os caminhos (/home/seu-usuario/... e /opt/tomcat...) para onde o projeto e o Tomcat estão instalados na sua máquina.Bash# --- ALIAS TARTARUGA COMETA ---

# 1. Navegar para o projeto
alias cdtrilha='cd ~/Documentos/Projetos/daniiloGW_trilha'

# 2. Rebuild com Gradle (Gera o WAR)
alias rebuildG='cd ~/Documentos/Projetos/daniiloGW_trilha && ~/gradle-7.6/bin/gradle clean build'

# 3. Deploy Manual (Copia WAR para Tomcat)
# Ajuste o caminho do Tomcat (/opt/tomcat9) conforme sua instalação
alias deployTrilha='sudo cp ~/Documentos/Projetos/daniiloGW_trilha/build/libs/daniiloGW_trilha-1.0-SNAPSHOT.war /opt/tomcat9/webapps/trilha.war'

# 4. Controle do Tomcat
alias stopTomcat='cd /opt/tomcat9/bin && ./shutdown.sh'
alias startTomcat='cd /opt/tomcat9/bin && ./startup.sh'
alias restartTomcat='cd /opt/tomcat9/bin && ./shutdown.sh && ./startup.sh'

# 5. Full Deploy (Build + Deploy + Restart) - O mais usado!
alias fullDeploy='cd ~/Documentos/Projetos/daniiloGW_trilha && ~/gradle-7.6/bin/gradle clean build && sudo cp build/libs/daniiloGW_trilha-1.0-SNAPSHOT.war /opt/tomcat9/webapps/trilha.war && cd /opt/tomcat9/bin && ./shutdown.sh && ./startup.sh'

3. Salve e AtivePressione Ctrl + O e Enter para salvar.Pressione Ctrl + X para sair do editor.Atualize o terminal com o comando:Bashsource ~/.bashrc
# ou
source ~/.zshrc

📖 Guia de Uso dos ComandosComandoO que faz?Quando usar?
cdtrilhaEntra na pasta do projeto.Sempre que abrir o terminal.
rebuildGLimpa e compila o projeto.Quando alterar códigos Java/JSP e quiser apenas gerar o WAR.deployTrilhaCopia o WAR para o Tomcat.Após o build, para atualizar o arquivo no servidor.
restartTomcat Reinicia o servidor.Para aplicar as alterações do deploy.
fullDeployFaz tudo de uma vez: Build -> Deploy -> Restart.O mais usado! Alterou código? Roda esse comando e testa no navegador

.Desenvolvido por Danilo Mendes
