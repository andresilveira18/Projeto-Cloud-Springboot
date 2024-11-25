package br.edu.ibmec.projeto_cloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LimiteInsuficienteException.class)
    public ResponseEntity<ApiErrorResponse> handleLimiteInsuficienteException(LimiteInsuficienteException ex) {
        ApiErrorResponse response = new ApiErrorResponse("400", "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CartaoInativoException.class)
    public ResponseEntity<ApiErrorResponse> handleCartaoInativoException(CartaoInativoException ex) {
        ApiErrorResponse response = new ApiErrorResponse("400", "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(FrequenciaAltaTransacoesException.class)
    public ResponseEntity<ApiErrorResponse> handleFrequenciaAltaTransacoesException(FrequenciaAltaTransacoesException ex) {
        ApiErrorResponse response = new ApiErrorResponse("400", "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(TransacaoDuplicadaException.class)
    public ResponseEntity<ApiErrorResponse> handleTransacaoDuplicadaException(TransacaoDuplicadaException ex) {
        ApiErrorResponse response = new ApiErrorResponse("400", "Bad Request", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
        @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(
                        "BAD_REQUEST",
                        "Erro na Formatação do JSON",
                        "O corpo da requisição não está no formato esperado."
                ));
    }
}
