package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.dto.TransacaoResponseDTO;
import br.edu.ibmec.projeto_cloud.exception.ApiErrorResponse;
import br.edu.ibmec.projeto_cloud.exception.CartaoInativoException;
import br.edu.ibmec.projeto_cloud.exception.CartaoNaoEncontradoException;
import br.edu.ibmec.projeto_cloud.exception.ClienteNaoEncontradoException;
import br.edu.ibmec.projeto_cloud.exception.FrequenciaAltaTransacoesException;
import br.edu.ibmec.projeto_cloud.exception.LimiteInsuficienteException;
import br.edu.ibmec.projeto_cloud.exception.TransacaoDuplicadaException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import br.edu.ibmec.projeto_cloud.model.Transacao;
import br.edu.ibmec.projeto_cloud.service.TransacaoService;

import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    // Endpoint para autorizar uma nova transação
    @PostMapping("/autorizar/{cartaoId}")
    public ResponseEntity<?> autorizarTransacao(@PathVariable int cartaoId, @RequestBody Transacao transacao) {
        try {
            // Autoriza a transação no serviço
            TransacaoResponseDTO resultado = transacaoService.autorizarTransacao(cartaoId, transacao);
    
            // Retorna sucesso
            return ResponseEntity.ok(resultado);
        } catch (CartaoNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorResponse());
        } catch (CartaoInativoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getErrorResponse());
        } catch (LimiteInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(e.getErrorResponse());
        } catch (FrequenciaAltaTransacoesException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getErrorResponse());
        } catch (TransacaoDuplicadaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorResponse());
        } catch (Exception e) {
            // Captura qualquer erro inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(
                            "INTERNAL_SERVER_ERROR",
                            "Erro Interno",
                            "Ocorreu um erro inesperado no servidor."
                    ));
        }
    }
    

    // Endpoint para buscar todas as transações aprovadas de um cliente
    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Transacao>> extratoCartao(
            @PathVariable("id") int id,
            @RequestParam String numeroCartao,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano) {
        try {
            // Define o mês e ano atuais se não forem fornecidos
            LocalDateTime now = LocalDateTime.now();
            int mesFiltrado = Optional.ofNullable(mes).orElse(now.getMonthValue());
            int anoFiltrado = Optional.ofNullable(ano).orElse(now.getYear());
    
            // Busca as transações através do serviço
            List<Transacao> transacoes = transacaoService.buscarTransacoesPorCartao(id, numeroCartao, mesFiltrado, anoFiltrado);
    
            return ResponseEntity.ok(transacoes);
        } catch (ClienteNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }    
}

