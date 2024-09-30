package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.dto.ClienteResponseDTO;
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
    public ResponseEntity<ClienteResponseDTO> associarCartao(@PathVariable int clienteId, @PathVariable int cartaoId) {
        try {
            ClienteResponseDTO clienteAtualizado = clienteService.associarCartao(clienteId, cartaoId);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para buscar um cliente por ID
    @GetMapping("/{clienteId}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable int clienteId) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(clienteId);

        return clienteOpt
                .map(cliente -> {
                    // Map all necessary fields to CartaoResponseDTO
                    List<CartaoResponseDTO> cartoesDto = cliente.getCartoes().stream()
                            .map(cartao -> new CartaoResponseDTO(
                                    cartao.getId(),
                                    cartao.getNumeroCartao(),
                                    cartao.getLimite(),
                                    cartao.getSaldo(),
                                    cartao.getEstaAtivado()
                            ))
                            .collect(Collectors.toList());

                    // Create ClienteResponseDTO with all fields
                    ClienteResponseDTO dto = new ClienteResponseDTO(
                            cliente.getId(),
                            cliente.getNome(),
                            cliente.getCpf(),
                            cliente.getDataNascimento(),
                            cliente.getEmail(),
                            cliente.getTelefone(),
                            cliente.getEndereco(),
                            cartoesDto // Set the list of fully mapped CartaoResponseDTO
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

}
