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

    public Entrega(String codigoPedido, String dataEnvio, String dataEntrega, String transportadora, BigDecimal valorFrete) {
        this.codigoPedido = codigoPedido;
        this.dataEnvio = dataEnvio;
        this.dataEntrega = dataEntrega;
        this.transportadora = transportadora;
        this.valorFrete = valorFrete;
    }

    public String getCodigoPedido() {
        return  codigoPedido;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
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

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public String toString() {
        return "Codigo do Cliente: " + this.codigoPedido + ", Data de envio: " + this.dataEnvio + ", Data de Entrega: " + this.dataEntrega + ", Transportadora: " + this.transportadora + ", Valor do Frete: " + this.valorFrete;
    }

}
