package br.edu.ibmec.projeto_cloud.exception;

public class TransacaoDuplicadaException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public TransacaoDuplicadaException(String mensagem) {
        super(mensagem);
        this.errorResponse = new ApiErrorResponse(
                "CONFLICT",
                "Transação Duplicada",
                mensagem
        );
    }

    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
