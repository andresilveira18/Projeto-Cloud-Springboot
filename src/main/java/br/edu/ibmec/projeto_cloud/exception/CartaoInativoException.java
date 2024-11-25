package br.edu.ibmec.projeto_cloud.exception;

public class CartaoInativoException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public CartaoInativoException(String mensagem) {
        super(mensagem);
        this.errorResponse = new ApiErrorResponse(
                "FORBIDDEN",
                "Cartão Inativo",
                mensagem
        );
    }

    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
