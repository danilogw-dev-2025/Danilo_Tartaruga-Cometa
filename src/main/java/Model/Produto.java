package Model;

import java.math.BigDecimal;

public class Produto {

    private Long idProduto;
    private String codigoProduto;
    private String nomeProduto;
    private String descricao;
    private BigDecimal preco;
    private Long quantidade;
    private boolean emUso;

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

    public Produto(String nomeProduto, String descricao, BigDecimal preco, Long quantidade) {
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

    public boolean isEmUso() {
        return emUso;
    }

    public void setEmUso(boolean emUso) {
        this.emUso = emUso;
    }

    public String gerarSiglaDoProd () {
        if (this.nomeProduto == null || this.nomeProduto.trim().isEmpty()) {
            return "PROD"; //default
        }

        String [] partes = this.nomeProduto.trim().toUpperCase().split("\\s+");
        StringBuffer sigla = new StringBuffer();

        for (int i = 0; i < Math.min(partes.length, 3); i++ ) {
            if (!partes[i].isEmpty()) {
                sigla.append(partes[i].charAt(0));
            }
        }

        while(sigla.length() < 3) {
            sigla.append("X");
        }
        return sigla.toString();
    }

    public String toString() {
        return "ID PRODUTO: " + this.idProduto + ", Codigo do Produto: " + this.codigoProduto + ", Produto: " + this.nomeProduto + ", Descricao do Produto: "
                + descricao + ", Preco: " + this.preco + ", Quantidade: " + this.quantidade;
    }
}
