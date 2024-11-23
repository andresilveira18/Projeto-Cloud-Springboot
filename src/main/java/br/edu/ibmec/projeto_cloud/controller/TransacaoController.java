package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.dto.TransacaoResponseDTO;
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
    public ResponseEntity<TransacaoResponseDTO> autorizarTransacao(@PathVariable int cartaoId, @RequestBody Transacao transacao) {
        TransacaoResponseDTO resultado = transacaoService.autorizarTransacao(cartaoId, transacao);
        if ("Sucesso".equals(resultado.getStatus())) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
    }

    // Endpoint para buscar todas as transações aprovadas de um cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Transacao>> getTransacoesPorCliente(
            @PathVariable int clienteId,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano) {

        try {
            // Se mês e ano não forem fornecidos, usar mês e ano atuais
            LocalDateTime now = LocalDateTime.now();
            int mesFiltrado = Optional.ofNullable(mes).orElse(now.getMonthValue());
            int anoFiltrado = Optional.ofNullable(ano).orElse(now.getYear());

            List<Transacao> transacoes = transacaoService.buscarTransacoesPorCliente(clienteId, mesFiltrado, anoFiltrado);
            return ResponseEntity.ok(transacoes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
