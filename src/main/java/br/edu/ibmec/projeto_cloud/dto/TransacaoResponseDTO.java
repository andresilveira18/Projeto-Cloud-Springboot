package br.edu.ibmec.projeto_cloud.dto;

public class TransacaoResponseDTO {
    private String status;
    private String mensagem;

    // Construtor
    public TransacaoResponseDTO(String status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }

    // Getters e Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
