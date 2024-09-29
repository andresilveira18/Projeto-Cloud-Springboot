package br.edu.ibmec.projeto_cloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import br.edu.ibmec.projeto_cloud.dto.CartaoResponseDTO;
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
    public ResponseEntity<CartaoResponseDTO> criarCartao(@RequestBody Cartao cartao) {
        Cartao novoCartao = cartaoService.criarCartao(cartao);
        CartaoResponseDTO response = new CartaoResponseDTO(
                novoCartao.getId(),
                novoCartao.getNumeroCartao(),
                novoCartao.getLimite(),
                novoCartao.getSaldo(),
                novoCartao.getEstaAtivado()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint para buscar um cartão por ID
    @GetMapping("/{cartaoId}")
    public ResponseEntity<CartaoResponseDTO> buscarCartaoPorId(@PathVariable int cartaoId) {
        Cartao cartao = cartaoService.buscarPorId(cartaoId);
        CartaoResponseDTO response = new CartaoResponseDTO(
                cartao.getId(),
                cartao.getNumeroCartao(),
                cartao.getLimite(),
                cartao.getSaldo(),
                cartao.getEstaAtivado()
        );
        return ResponseEntity.ok(response);
    }
}
