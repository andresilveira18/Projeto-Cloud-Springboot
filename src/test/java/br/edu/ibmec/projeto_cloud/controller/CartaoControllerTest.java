package br.edu.ibmec.projeto_cloud.controller;

import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.service.CartaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.MockitoAnnotations;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(CartaoController.class)
public class CartaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartaoService cartaoService;

    @InjectMocks
    private CartaoController cartaoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarCartao_retornoSucesso() throws Exception {
        // Arrange
        Cartao cartao = new Cartao();
        cartao.setNumeroCartao(1234567890123456L);
        cartao.setSaldo(1000.0);
        cartao.setEstaAtivado(true);

        when(cartaoService.criarCartao(any(Cartao.class))).thenReturn(cartao);

        // Act & Assert
        mockMvc.perform(post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroCartao\": 1234567890123456, \"saldo\": 1000.0, \"estaAtivado\": true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value(1234567890123456L))
                .andExpect(jsonPath("$.saldo").value(1000.0))
                .andExpect(jsonPath("$.estaAtivado").value(true));
    }
    @Test
    void buscarCartaoPorId_retornoSucesso() throws Exception {
        // Arrange
        int cartaoId = 1;
        Cartao cartao = new Cartao();
        cartao.setId(cartaoId);
        cartao.setNumeroCartao(1234567890123456L);
        cartao.setSaldo(1000.0);
        cartao.setEstaAtivado(true);

        when(cartaoService.buscarPorId(cartaoId)).thenReturn(cartao);

        // Act & Assert
        mockMvc.perform(get("/cartoes/{cartaoId}", cartaoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCartao").value(1234567890123456L))
                .andExpect(jsonPath("$.saldo").value(1000.0))
                .andExpect(jsonPath("$.estaAtivado").value(true));
    }

}
