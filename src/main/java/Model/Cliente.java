package Model;

public class Cliente {

    private Long idCliente;
    private String codigoCliente;
    private String nome;
    private String cpf;
    private String dataNascimento;

    private String email;
    private String senha;

    public Cliente() {

    }

    public Cliente (String codigoCliente, String nome, String cpf, String dataNascimento, String email, String senha) {
        this.codigoCliente = codigoCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.senha = senha;

    }

    public Cliente (Long idCliente, String codigoCliente, String nome, String cpf, String dataNascimento, String email, String senha) {
        this.idCliente = idCliente;
        this.codigoCliente = codigoCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.senha = senha;
        this.idCliente = idCliente;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf( String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
       return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;

    }

    public String toString() {
        return "ID_CLIENTE: " + this.idCliente + ", Codigo do Cliente: " + this.codigoCliente + ", Nome: " + this.nome + ", CPF: " + this.cpf + "," +
                " Data de Nascimento: " + this.dataNascimento + ", Email: " + this.email;
    }
}
