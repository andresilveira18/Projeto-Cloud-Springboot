package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.dto.ClienteResponseDTO;
import br.edu.ibmec.projeto_cloud.exception.ClienteNaoEncontradoException;
import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarCliente_retornoSucesso() throws Exception {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("12345678900");

        when(clienteService.criarCliente(any(Cliente.class))).thenReturn(cliente);

        // Act & Assert
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"João Silva\", \"cpf\": \"12345678900\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void criarCliente_erroServidor() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Erro interno")).when(clienteService).criarCliente(any(Cliente.class));
    
        // Act & Assert
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"João Silva\", \"cpf\": \"12345678900\"}"))
                .andExpect(status().isInternalServerError()) // Verifica o status HTTP 500
                .andExpect(jsonPath("$.status").value("INTERNAL_SERVER_ERROR")) // Verifica o status no JSON
                .andExpect(jsonPath("$.error").value("Erro Interno")) // Verifica o tipo de erro
                .andExpect(jsonPath("$.message").value("Ocorreu um erro ao criar o cliente.")); // Verifica a mensagem de erro
    }
    

    @Test
    void associarCartao_retornoSucesso() throws Exception {
        int clienteId = 1;
        int cartaoId = 2;

        // Simula o serviço retornando uma resposta de sucesso
        ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO(
                clienteId, "João Silva", "12345678900", null, null, null, null, new ArrayList<>());

        when(clienteService.associarCartao(clienteId, cartaoId)).thenReturn(clienteResponseDTO);

        // Act & Assert
        mockMvc.perform(post("/clientes/{clienteId}/cartoes/{cartaoId}", clienteId, cartaoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void associarCartao_clienteNaoEncontrado() throws Exception {
        int clienteId = 1;
        int cartaoId = 2;

        // Simula o serviço lançando a exceção de cliente não encontrado
        doThrow(new ClienteNaoEncontradoException("Cliente não encontrado"))
                .when(clienteService).associarCartao(clienteId, cartaoId);

        // Act & Assert
        mockMvc.perform(post("/clientes/{clienteId}/cartoes/{cartaoId}", clienteId, cartaoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Espera 404
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.error").value("Cliente Não Encontrado"))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado"));
    }

}
