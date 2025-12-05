package Model;

import java.math.BigDecimal;

public class Produto {

    private Long idProduto;
    private String codigoProduto;
    private String nomeProduto;
    private String descricao;
    private BigDecimal preco;
    private Long quantidade;

    public  Produto() {

    }

    public Produto(String codigoProduto, String nomeProduto, String descricao, BigDecimal preco, Long quantidade) {
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Produto(Long idProduto, String codigoProduto, String nomeProduto, String descricao, BigDecimal preco, Long quantidade) {
        this.idProduto = idProduto;
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNomeProduto(){
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public String toString() {
        return "ID PRODUTO: " + this.idProduto + ", Codigo do Produto: " + this.codigoProduto + ", Produto: " + this.nomeProduto + ", Descricao do Produto: "
                + descricao + ", Preco: " + this.preco + ", Quantidade: " + this.quantidade;
    }
}
