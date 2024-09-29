package br.edu.ibmec.projeto_cloud.exception;

public class CartaoInativoException extends RuntimeException {
    public CartaoInativoException(String mensagem) {
        super(mensagem);
    }
}
