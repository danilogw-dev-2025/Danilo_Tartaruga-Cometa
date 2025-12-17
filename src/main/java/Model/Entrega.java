package Model;

import java.math.BigDecimal;

public class Entrega {

    private Long idEntrega;
    private Long idRemetente;
    private Long idDestinatario;
    private Long idProduto;
    private String codigoPedido;
    private String dataEnvio;
    private String dataEntrega;
    private String transportadora;
    private BigDecimal valorFrete;
    private String status;
    private int qtdPedida;

    // Campos apenas para exibição
    private String nomeRemetente;
    private String nomeDestinatario;
    private String nomeProduto;
    private BigDecimal valorTotalProduto;
    private Long quantidadeProduto;

    public Entrega() {}

    public Entrega(Long idEntrega, Long idRemetente, Long idDestinatario, Long idProduto, String codigoPedido,
                   String dataEnvio, String dataEntrega, String transportadora, BigDecimal valorFrete, String status, int qtdPedida) {
        this.idEntrega = idEntrega;
        this.idRemetente = idRemetente;
        this.idDestinatario = idDestinatario;
        this.idProduto = idProduto;
        this.codigoPedido = codigoPedido;
        this.dataEnvio = dataEnvio;
        this.dataEntrega = dataEntrega;
        this.transportadora = transportadora;
        this.valorFrete = valorFrete;
        this.status = status;
        this.qtdPedida = qtdPedida;
    }

    public Entrega(Long idRemetente, Long idDestinatario, Long idProduto, String codigoPedido,
                   String dataEnvio, String dataEntrega, String transportadora, BigDecimal valorFrete, String status, int qtdPedida) {
        this.idRemetente = idRemetente;
        this.idDestinatario = idDestinatario;
        this.idProduto = idProduto;
        this.codigoPedido = codigoPedido;
        this.dataEnvio = dataEnvio;
        this.dataEntrega = dataEntrega;
        this.transportadora = transportadora;
        this.valorFrete = valorFrete;
        this.status = status;
        this.qtdPedida = qtdPedida;
    }

    public BigDecimal getValorFinal() {
        BigDecimal totalProd = (this.valorTotalProduto != null) ? this.valorTotalProduto : BigDecimal.ZERO;
        BigDecimal frete = (this.valorFrete != null) ? this.valorFrete : BigDecimal.ZERO;
        return totalProd.add(frete);
    }

    public Long getIdEntrega() { return idEntrega; }
    public void setIdEntrega(Long idEntrega) { this.idEntrega = idEntrega; }

    public Long getIdRemetente() { return idRemetente; }
    public void setIdRemetente(Long idRemetente) { this.idRemetente = idRemetente; }

    public Long getIdDestinatario() { return idDestinatario; }
    public void setIdDestinatario(Long idDestinatario) { this.idDestinatario = idDestinatario; }

    public Long getIdProduto() { return idProduto; }
    public void setIdProduto(Long idProduto) { this.idProduto = idProduto; }

    public String getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(String codigoPedido) { this.codigoPedido = codigoPedido; }

    public String getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(String dataEnvio) { this.dataEnvio = dataEnvio; }

    public String getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(String dataEntrega) { this.dataEntrega = dataEntrega; }

    public String getTransportadora() { return transportadora; }
    public void setTransportadora(String transportadora) { this.transportadora = transportadora; }

    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNomeRemetente() { return nomeRemetente; }
    public void setNomeRemetente(String nomeRemetente) { this.nomeRemetente = nomeRemetente; }

    public String getNomeDestinatario() { return nomeDestinatario; }
    public void setNomeDestinatario(String nomeDestinatario) { this.nomeDestinatario = nomeDestinatario; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public BigDecimal getValorTotalProduto() { return valorTotalProduto; }
    public void setValorTotalProduto(BigDecimal valorTotalProduto) { this.valorTotalProduto = valorTotalProduto; }

    public Long getQuantidadeProduto() { return quantidadeProduto; }
    public void setQuantidadeProduto(Long quantidadeProduto) { this.quantidadeProduto = quantidadeProduto; }

    public int getQtdPedida() {
        return qtdPedida;
    }

    public void setQtdPedida(int qtdPedida) {
        this.qtdPedida = qtdPedida;
    }

    @Override
    public String toString() {
        return "Entrega{" +
                "idEntrega=" + idEntrega +
                ", idRemetente=" + idRemetente +
                ", idDestinatario=" + idDestinatario +
                ", idProduto=" + idProduto +
                ", codigoPedido='" + codigoPedido + '\'' +
                ", dataEnvio='" + dataEnvio + '\'' +
                ", dataEntrega='" + dataEntrega + '\'' +
                ", transportadora='" + transportadora + '\'' +
                ", valorFrete=" + valorFrete +
                ", status='" + status + '\'' +
                ", qtdPedida=" + qtdPedida +
                ", nomeRemetente='" + nomeRemetente + '\'' +
                ", nomeDestinatario='" + nomeDestinatario + '\'' +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", valorTotalProduto=" + valorTotalProduto +
                ", quantidadeProduto=" + quantidadeProduto +
                '}';
    }
}