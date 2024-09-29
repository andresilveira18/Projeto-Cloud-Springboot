package br.edu.ibmec.projeto_cloud.exception;

public class TransacaoDuplicadaException extends RuntimeException {
    public TransacaoDuplicadaException(String mensagem) {
        super(mensagem);
    }
}
