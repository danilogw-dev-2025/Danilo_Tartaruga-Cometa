package Model;

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
