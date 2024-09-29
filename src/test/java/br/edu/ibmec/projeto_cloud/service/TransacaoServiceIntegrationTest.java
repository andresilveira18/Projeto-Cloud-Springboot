package br.edu.ibmec.projeto_cloud.service;

import br.edu.ibmec.projeto_cloud.exception.*;
import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.model.Transacao;
import br.edu.ibmec.projeto_cloud.repository.CartaoRepository;
import br.edu.ibmec.projeto_cloud.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransacaoServiceIntegrationTest {

    @Autowired
    private TransacaoService transacaoService;

    @MockBean
    private CartaoRepository cartaoRepository;

    @MockBean
    private TransacaoRepository transacaoRepository;

    private Cartao cartao;

    @BeforeEach
    void setUp() {
        // Configurações iniciais
        cartao = new Cartao();
        cartao.setId(1);
        cartao.setSaldo(500.0);
        cartao.setEstaAtivado(true);

        when(cartaoRepository.findById(cartao.getId())).thenReturn(Optional.of(cartao));
    }

    @Test
    void validarTransacao_cartaoInativo() {
        // Arrange
        cartao.setEstaAtivado(false);
        Transacao transacao = new Transacao();
        transacao.setValor(100.0);

        // Act & Assert
        Exception exception = assertThrows(CartaoInativoException.class, () -> {
            transacaoService.validarTransacao(cartao, transacao);
        });

        assertEquals("Cartão não está ativo", exception.getMessage());
    }

    @Test
    void validarTransacao_limiteInsuficiente() {
        // Arrange
        cartao.setSaldo(50.0);
        Transacao transacao = new Transacao();
        transacao.setValor(100.0);

        // Act & Assert
        Exception exception = assertThrows(LimiteInsuficienteException.class, () -> {
            transacaoService.validarTransacao(cartao, transacao);
        });

        assertEquals("Limite insuficiente", exception.getMessage());
    }

    @Test
    void validarTransacao_frequenciaAltaTransacoes() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        Transacao transacao1 = new Transacao();
        transacao1.setDataTransacao(agora.minusSeconds(30));
        Transacao transacao2 = new Transacao();
        transacao2.setDataTransacao(agora.minusSeconds(60));
        Transacao transacao3 = new Transacao();
        transacao3.setDataTransacao(agora.minusSeconds(90));

        when(transacaoRepository.findByCartaoAndDataTransacaoAfter(eq(cartao), any(LocalDateTime.class)))
                .thenReturn(List.of(transacao1, transacao2, transacao3));

        Transacao novaTransacao = new Transacao();
        novaTransacao.setDataTransacao(agora);

        // Act & Assert
        Exception exception = assertThrows(FrequenciaAltaTransacoesException.class, () -> {
            transacaoService.validarTransacao(cartao, novaTransacao);
        });

        assertEquals("Alta frequência de transações em pequeno intervalo", exception.getMessage());
    }

    @Test
    void validarTransacao_transacaoDuplicada() {
        // Arrange
        LocalDateTime agora = LocalDateTime.now();
        Transacao transacaoExistente = new Transacao();
        transacaoExistente.setDataTransacao(agora.minusSeconds(60));
        transacaoExistente.setValor(100.0);
        transacaoExistente.setComerciante("Loja A");

        when(transacaoRepository.findByCartaoAndDataTransacaoAfter(eq(cartao), any(LocalDateTime.class)))
                .thenReturn(List.of(transacaoExistente));

        Transacao novaTransacao = new Transacao();
        novaTransacao.setValor(100.0);
        novaTransacao.setComerciante("Loja A");
        novaTransacao.setDataTransacao(agora);

        // Act & Assert
        Exception exception = assertThrows(TransacaoDuplicadaException.class, () -> {
            transacaoService.validarTransacao(cartao, novaTransacao);
        });

        assertEquals("Transação duplicada", exception.getMessage());
    }
}
