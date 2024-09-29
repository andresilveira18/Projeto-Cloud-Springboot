package br.edu.ibmec.projeto_cloud.exception;

public class LimiteInsuficienteException extends RuntimeException {
    public LimiteInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
