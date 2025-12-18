package Model;

/**
 * Classe de Modelo (Model) para a entidade Cliente.
 * 1. Representação de Estado: Encapsula todos os atributos do cliente, desde o
 * ID interno do banco até o estado de 'bloqueio' para exclusão lógica na UI.
 * 2. Inteligência de Domínio: Implementa a regra de geração de siglas (ex: João Silva -> JSA),
 * garantindo padronização na criação dos códigos de identificação.
 * 3. Polimorfismo de Documento: O metodo 'getTipoPessoa' deriva automaticamente
 * se o cliente é PF ou PJ baseando-se no comprimento da string do documento.
 * 4. Sobrecarga de Construtores: Oferece flexibilidade para criar instâncias em
 * diferentes momentos (ex: antes de salvar, sem ID; ou ao listar, com todos os dados).
 * 5. Desacoplamento: Serve como o 'contrato' de dados que une o Servlet, o BO e o DAO.
 */

public class Cliente {

    private Long idCliente;
    private String codigoCliente;
    private String nome;
    private String documento;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private int numeroCasa;
    private String cep;
    private boolean bloqueadoParaExclusao;


    public Cliente() {

    }

    public Cliente(Long idCliente, String codigoCliente, String nome, String documento, String estado, String cidade, String bairro, String rua, int numeroCasa, String cep) {
        this.idCliente = idCliente;
        this.codigoCliente = codigoCliente;
        this.nome = nome;
        this.documento = documento;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numeroCasa = numeroCasa;
        this.cep = cep;
    }

    public Cliente(String codigoCliente, String nome, String documento, String estado, String cidade, String bairro, String rua, int numeroCasa, String cep) {
        this.codigoCliente = codigoCliente;
        this.nome = nome;
        this.documento = documento;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numeroCasa = numeroCasa;
        this.cep = cep;
    }

    public Cliente(String nome, String documento, String estado, String cidade, String bairro, String rua, int numeroCasa, String cep) {
        this.nome = nome;
        this.documento = documento;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numeroCasa = numeroCasa;
        this.cep = cep;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public boolean isBloqueadoParaExclusao() {
        return bloqueadoParaExclusao;
    }

    public void setBloqueadoParaExclusao(boolean bloqueadoParaExclusao) {
        this.bloqueadoParaExclusao = bloqueadoParaExclusao;
    }

    public String getTipoPessoa() {
        if (this.documento == null) {
            return "Indefinido";
        }

        if (this.documento.length() > 14) {
            return "CNPJ";
        } else {
            return "CPF";
        }
    }

    public String gerarSiglaDoNome() {
        if (this.nome == null || this.nome.trim().isEmpty()) {
            return "CLI"; //default sigla
        }

        // Remove espaços extras e divide o nome por palavras
        String[] partes = this.nome.trim().toUpperCase().split("\\s+");
        StringBuffer sigla = new StringBuffer();

        for (int i = 0; i < Math.min(partes.length, 3); i++) {
            if (!partes[i].isEmpty()) {
                sigla.append(partes[i].charAt(0));
            }
        }

        // Se o nome for apenas "Ana" (sigla A), preenche com X até ter 3 letras (AXX)
        while(sigla.length() < 3) {
            sigla.append("X");
        }
        return sigla.toString();
    }


    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", codigoCliente='" + codigoCliente + '\'' +
                ", nome='" + nome + '\'' +
                ", documento='" + documento + '\'' +
                ", estado='" + estado + '\'' +
                ", cidade='" + cidade + '\'' +
                ", bairro='" + bairro + '\'' +
                ", rua='" + rua + '\'' +
                ", numeroCasa=" + numeroCasa +
                ", cep='" + cep + '\'' +
                '}';
    }
}
