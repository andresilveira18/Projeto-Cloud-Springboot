package br.edu.ibmec.projeto_cloud.exception;

public class LimiteInsuficienteException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public LimiteInsuficienteException(String mensagem) {
        super(mensagem);
        this.errorResponse = new ApiErrorResponse(
                "PAYMENT_REQUIRED",
                "Limite Insuficiente",
                mensagem
        );
    }

    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
