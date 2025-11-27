package Model;

import java.math.BigDecimal;

public class Entrega {

    private Long idEntrega;
    private Long idCliente;
    private Long idProduto;
    private String codigoPedido;
    private String dataEnvio;
    private String dataEntrega;
    private String transportadora;
    private BigDecimal valorFrete;

    public Entrega() {
    }

    // construtor para cadastro (sem idEntrega)
    public Entrega(Long idCliente, Long idProduto, String codigoPedido,
                   String dataEnvio, String dataEntrega,
                   String transportadora, BigDecimal valorFrete) {
        this.idCliente = idCliente;
        this.idProduto = idProduto;
        this.codigoPedido = codigoPedido;
        this.dataEnvio = dataEnvio;
        this.dataEntrega = dataEntrega;
        this.transportadora = transportadora;
        this.valorFrete = valorFrete;
    }

    // construtor para SELECT (com idEntrega)
    public Entrega(Long idEntrega, Long idCliente, Long idProduto, String codigoPedido,
                   String dataEnvio, String dataEntrega,
                   String transportadora, BigDecimal valorFrete) {
        this.idEntrega = idEntrega;
        this.idCliente = idCliente;
        this.idProduto = idProduto;
        this.codigoPedido = codigoPedido;
        this.dataEnvio = dataEnvio;
        this.dataEntrega = dataEntrega;
        this.transportadora = transportadora;
        this.valorFrete = valorFrete;
    }

    public Long getIdEntrega() {
        return idEntrega;
    }

    public void setIdEntrega(Long idEntrega) {
        this.idEntrega = idEntrega;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    @Override
    public String toString() {
        return "ID ENTREGA: " + this.idEntrega +
                ", ID CLIENTE: " + this.idCliente +
                ", ID PRODUTO: " + this.idProduto +
                ", Código do Pedido: " + this.codigoPedido +
                ", Data de envio: " + this.dataEnvio +
                ", Data de Entrega: " + this.dataEntrega +
                ", Transportadora: " + this.transportadora +
                ", Valor do Frete: " + this.valorFrete;
    }
}
