package br.edu.ibmec.projeto_cloud.exception;

public class FrequenciaAltaTransacoesException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public FrequenciaAltaTransacoesException(String mensagem) {
        super(mensagem);
        this.errorResponse = new ApiErrorResponse(
                "TOO_MANY_REQUESTS",
                "Alta Frequência de Transações",
                mensagem
        );
    }

    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
