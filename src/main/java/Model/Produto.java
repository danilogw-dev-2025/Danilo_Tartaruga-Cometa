package Model;

import java.math.BigDecimal;

public class Produto {

    private Long idProduto;
    private int codigoProduto;
    private String nomeProduto;
    private String descricao;
    private BigDecimal preco;
    private int quantidade;

    public  Produto() {

    }

    public Produto(int codigoProduto, String nomeProduto, String descricao, BigDecimal preco, int quantidade) {
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

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(int codigoProduto) {
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String toString() {
        return "ID PRODUTO: " + this.idProduto + ", Codigo do Produto: " + this.codigoProduto + ", Produto: " + this.nomeProduto + ", Descricao do Produto: "
                + descricao + ", Preco: " + this.preco + ", Quantidade: " + this.quantidade;
    }
}
