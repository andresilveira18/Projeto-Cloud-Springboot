package br.edu.ibmec.projeto_cloud.dto;

public class CartaoResponseDTO {
    private int id;
    private long numeroCartao;
    private double limite;
    private double saldo;
    private Boolean estaAtivado;

    // Construtor
    public CartaoResponseDTO(int id, long numeroCartao, double limite, double saldo, Boolean estaAtivado) {
        this.id = id;
        this.numeroCartao = numeroCartao;
        this.limite = limite;
        this.saldo = saldo;
        this.estaAtivado = estaAtivado;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public long getNumeroCartao() { return numeroCartao; }
    public void setNumeroCartao(long numeroCartao) { this.numeroCartao = numeroCartao; }

    public double getLimite() { return limite; }
    public void setLimite(double limite) { this.limite = limite; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public Boolean getEstaAtivado() { return estaAtivado; }
    public void setEstaAtivado(Boolean estaAtivado) { this.estaAtivado = estaAtivado; }
}
