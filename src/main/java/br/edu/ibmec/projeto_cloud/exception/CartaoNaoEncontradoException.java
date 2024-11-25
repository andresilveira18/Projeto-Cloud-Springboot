package br.edu.ibmec.projeto_cloud.exception;

public class CartaoNaoEncontradoException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public CartaoNaoEncontradoException(String mensagem) {
        super(mensagem);
        this.errorResponse = new ApiErrorResponse(
                "NOT_FOUND",
                "Cartão Não Encontrado",
                mensagem
        );
    }

    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
