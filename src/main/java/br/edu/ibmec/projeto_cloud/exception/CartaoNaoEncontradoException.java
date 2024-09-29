package br.edu.ibmec.projeto_cloud.exception;

public class CartaoNaoEncontradoException extends RuntimeException {
    public CartaoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
