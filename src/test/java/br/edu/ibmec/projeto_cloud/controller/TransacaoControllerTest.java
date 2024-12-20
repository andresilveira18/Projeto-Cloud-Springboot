package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.dto.TransacaoResponseDTO;
import br.edu.ibmec.projeto_cloud.exception.CartaoInativoException;
import br.edu.ibmec.projeto_cloud.exception.LimiteInsuficienteException;
import br.edu.ibmec.projeto_cloud.exception.TransacaoDuplicadaException;
import br.edu.ibmec.projeto_cloud.exception.FrequenciaAltaTransacoesException;
import br.edu.ibmec.projeto_cloud.model.Transacao;
import br.edu.ibmec.projeto_cloud.service.TransacaoService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(TransacaoController.class)
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransacaoService transacaoService;

    @InjectMocks
    private TransacaoController transacaoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void autorizarTransacao_retornoSucesso() throws Exception {
        // Arrange
        int cartaoId = 1;
        Transacao transacao = new Transacao();
        transacao.setValor(100.0);
        transacao.setComerciante("Loja A");

        // Simula o serviço retornando uma resposta de sucesso
        when(transacaoService.autorizarTransacao(eq(cartaoId), any(Transacao.class)))
                .thenReturn(new TransacaoResponseDTO("Sucesso", "Transação autorizada"));

        // Act & Assert
        mockMvc.perform(post("/transacoes/autorizar/{cartaoId}", cartaoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": 100.0, \"comerciante\": \"Loja A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Sucesso"))
                .andExpect(jsonPath("$.mensagem").value("Transação autorizada"));
    }

    @Test
void autorizarTransacao_limiteInsuficiente() throws Exception {
    // Arrange
    int cartaoId = 1;

    // Simula o serviço lançando a exceção de limite insuficiente
    when(transacaoService.autorizarTransacao(eq(cartaoId), any(Transacao.class)))
            .thenThrow(new LimiteInsuficienteException("Limite insuficiente"));

    // Act & Assert
    mockMvc.perform(post("/transacoes/autorizar/{cartaoId}", cartaoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"valor\": 100.0, \"comerciante\": \"Loja A\"}"))
            .andExpect(status().isPaymentRequired()) // 402
            .andExpect(jsonPath("$.message").value("Limite insuficiente"));
}

@Test
void autorizarTransacao_cartaoInativo() throws Exception {
    // Arrange
    int cartaoId = 1;

    when(transacaoService.autorizarTransacao(eq(cartaoId), any(Transacao.class)))
            .thenThrow(new CartaoInativoException("Cartão não está ativo"));

    // Act & Assert
    mockMvc.perform(post("/transacoes/autorizar/{cartaoId}", cartaoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"valor\": 100.0, \"comerciante\": \"Loja A\"}"))
            .andExpect(status().isForbidden()) // 403
            .andExpect(jsonPath("$.message").value("Cartão não está ativo"));
}

@Test
void autorizarTransacao_transacaoDuplicada() throws Exception {
    // Arrange
    int cartaoId = 1;

    when(transacaoService.autorizarTransacao(eq(cartaoId), any(Transacao.class)))
            .thenThrow(new TransacaoDuplicadaException("Transação duplicada"));

    // Act & Assert
    mockMvc.perform(post("/transacoes/autorizar/{cartaoId}", cartaoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"valor\": 100.0, \"comerciante\": \"Loja A\"}"))
            .andExpect(status().isConflict()) // 409
            .andExpect(jsonPath("$.message").value("Transação duplicada"));
}

@Test
void autorizarTransacao_frequenciaAltaTransacoes() throws Exception {
    // Arrange
    int cartaoId = 1;

    when(transacaoService.autorizarTransacao(eq(cartaoId), any(Transacao.class)))
            .thenThrow(new FrequenciaAltaTransacoesException("Alta frequência de transações em pequeno intervalo"));

    // Act & Assert
    mockMvc.perform(post("/transacoes/autorizar/{cartaoId}", cartaoId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"valor\": 100.0, \"comerciante\": \"Loja A\"}"))
            .andExpect(status().isTooManyRequests()) // 429
            .andExpect(jsonPath("$.message").value("Alta frequência de transações em pequeno intervalo"));
}


}
