package Model;

public class Endereco {

    private Long idEndereco;
    private Long idCliente; //futuro Cliente cliente
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String cep;
    private int numeroCasa;

    public Endereco() {

    }

    public Endereco(Long idCliente, String estado, String cidade, String bairro, String rua, String cep, int numeroCasa) {
        this.idCliente = idCliente;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.cep = cep;
        this.numeroCasa = numeroCasa;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Long idEndereco) {
        this.idEndereco = idEndereco;
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

    public String getCEP() {
        return cep;
    }

    public void  setCEP (String cep) {
        this.cep = cep;
    }

    public  int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    @Override
    public String toString() {
        return "ID ENDERECO: "+ this.idEndereco + ", ID CLIENTE " + this.idCliente + ", estado: " + this.estado + ", Cidade: " + this.cidade + ", Bairro: " + this.bairro + ", Rua: " + this.rua +
                ", Cep: " + this.cep + ", Numero da casa: " + this.numeroCasa;
    }


}
