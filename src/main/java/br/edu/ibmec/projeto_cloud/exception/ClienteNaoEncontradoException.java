package br.edu.ibmec.projeto_cloud.exception;

public class ClienteNaoEncontradoException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
        this.errorResponse = new ApiErrorResponse(
                "NOT_FOUND",
                "Cliente Não Encontrado",
                mensagem
        );
    }

    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
