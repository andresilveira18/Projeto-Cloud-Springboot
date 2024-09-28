package br.edu.ibmec.projeto_cloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.service.CartaoService;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    // Endpoint para criar um novo cartão
    @PostMapping
    public ResponseEntity<Cartao> criarCartao(@RequestBody Cartao cartao) {
        Cartao novoCartao = cartaoService.criarCartao(cartao);
        return new ResponseEntity<>(novoCartao, HttpStatus.CREATED);
    }

    // Endpoint para buscar um cartão por ID
    @GetMapping("/{cartaoId}")
    public ResponseEntity<Cartao> buscarCartaoPorId(@PathVariable int cartaoId) {
        Cartao cartao = cartaoService.buscarPorId(cartaoId);
        return ResponseEntity.ok(cartao);
    }
}
