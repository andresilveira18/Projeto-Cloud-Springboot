package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.dto.ClienteResponseDTO;
import br.edu.ibmec.projeto_cloud.exception.ApiErrorResponse;
import br.edu.ibmec.projeto_cloud.exception.ClienteNaoEncontradoException;
import br.edu.ibmec.projeto_cloud.dto.CartaoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.service.ClienteService;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Endpoint para criar um novo cliente
    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.criarCliente(cliente);
        return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
    }

    // Endpoint para associar um cartão existente a um cliente usando o ID do cartão
    @PostMapping("/{clienteId}/cartoes/{cartaoId}")
    public ResponseEntity<?> associarCartao(@PathVariable int clienteId, @PathVariable int cartaoId) {
        try {
            ClienteResponseDTO clienteResponse = clienteService.associarCartao(clienteId, cartaoId);
            return ResponseEntity.ok(clienteResponse);
        } catch (ClienteNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(
                            "INTERNAL_SERVER_ERROR",
                            "Erro Interno",
                            "Ocorreu um erro inesperado ao associar o cartão ao cliente."
                    ));
        }
    }

    // Endpoint para buscar um cliente por ID
    @GetMapping("/{clienteId}")
    public ResponseEntity<?> buscarClientePorId(@PathVariable int clienteId) {
        try {
            ClienteResponseDTO cliente = clienteService.buscarPorId(clienteId)
                    .map(c -> {
                        // Mapeia os cartões associados ao cliente
                        List<CartaoResponseDTO> cartoesDTO = c.getCartoes().stream()
                                .map(cartao -> new CartaoResponseDTO(
                                        cartao.getId(),
                                        cartao.getNumeroCartao(),
                                        cartao.getLimite(),
                                        cartao.getSaldo(),
                                        cartao.getEstaAtivado()
                                ))
                                .toList();
    
                        // Retorna o ClienteResponseDTO com os cartões
                        return new ClienteResponseDTO(
                                c.getId(),
                                c.getNome(),
                                c.getCpf(),
                                c.getDataNascimento(),
                                c.getEmail(),
                                c.getTelefone(),
                                c.getEndereco(),
                                cartoesDTO
                        );
                    })
                    .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
    
            return ResponseEntity.ok(cliente);
        } catch (ClienteNaoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorResponse());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiErrorResponse(
                            "INTERNAL_SERVER_ERROR",
                            "Erro Interno",
                            "Ocorreu um erro inesperado ao buscar o cliente."
                    ));
        }
    }

    @GetMapping("/cpf/{cpf}")
public ResponseEntity<?> buscarClientePorCpf(@PathVariable String cpf) {
    try {
        ClienteResponseDTO cliente = clienteService.buscarPorCpf(cpf)
                .map(c -> new ClienteResponseDTO(
                        c.getId(),
                        c.getNome(),
                        c.getCpf(),
                        c.getDataNascimento(),
                        c.getEmail(),
                        c.getTelefone(),
                        c.getEndereco(),
                        null // Preencher cartões, se necessário
                ))
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado com CPF: " + cpf));
        
        return ResponseEntity.ok(cliente);
    } catch (ClienteNaoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorResponse());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse(
                        "INTERNAL_SERVER_ERROR",
                        "Erro Interno",
                        "Ocorreu um erro ao buscar o cliente pelo CPF."
                ));
    }
}
    
}
