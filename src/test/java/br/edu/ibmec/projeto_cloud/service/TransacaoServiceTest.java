package br.edu.ibmec.projeto_cloud.service;

import br.edu.ibmec.projeto_cloud.exception.FrequenciaAltaTransacoesException;
import br.edu.ibmec.projeto_cloud.exception.LimiteInsuficienteException;
import br.edu.ibmec.projeto_cloud.exception.TransacaoDuplicadaException;
import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.model.Transacao;
import br.edu.ibmec.projeto_cloud.repository.CartaoRepository;
import br.edu.ibmec.projeto_cloud.repository.TransacaoRepository;
import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import br.edu.ibmec.projeto_cloud.exception.CartaoInativoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

class TransacaoServiceTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validarTransacao_cartaoInativo() {
        // Arrange
        Cartao cartao = new Cartao();
        cartao.setEstaAtivado(false); // Cartão inativo

        Transacao transacao = new Transacao();
        transacao.setValor(100.0);

        // Act & Assert
        Exception exception = assertThrows(CartaoInativoException.class, () -> {
            transacaoService.validarTransacao(cartao, transacao);
        });

        // Verifica se a mensagem da exceção está correta
        assertEquals("Cartão não está ativo", exception.getMessage());
    }

    @Test
    void validarTransacao_limiteInsuficiente() {
        // Arrange
        Cartao cartao = new Cartao();
        cartao.setEstaAtivado(true); // Cartão ativo
        cartao.setSaldo(50.0); // Saldo inferior ao valor da transação

        Transacao transacao = new Transacao();
        transacao.setValor(100.0); // Valor maior que o saldo do cartão

        // Act & Assert
        Exception exception = assertThrows(LimiteInsuficienteException.class, () -> {
            transacaoService.validarTransacao(cartao, transacao);
        });

        // Verifica se a mensagem da exceção está correta
        assertEquals("Limite insuficiente", exception.getMessage());
    }
    @Test
    void validarTransacao_frequenciaAltaTransacoes() {
        // Arrange
        Cartao cartao = new Cartao();
        cartao.setEstaAtivado(true);
        cartao.setSaldo(500.0);

        // Mockar transações recentes no intervalo de 2 minutos
        LocalDateTime agora = LocalDateTime.now();
        Transacao transacao1 = new Transacao();
        transacao1.setDataTransacao(agora.minusSeconds(30));
        transacao1.setValor(50.0);

        Transacao transacao2 = new Transacao();
        transacao2.setDataTransacao(agora.minusSeconds(60));
        transacao2.setValor(50.0);

        Transacao transacao3 = new Transacao();
        transacao3.setDataTransacao(agora.minusSeconds(90));
        transacao3.setValor(50.0);

        // Mockando o repositório para retornar as transações recentes
        when(transacaoRepository.findByCartaoAndDataTransacaoAfter(eq(cartao), any(LocalDateTime.class)))
                .thenReturn(List.of(transacao1, transacao2, transacao3));

        // Nova transação que causaria alta frequência
        Transacao novaTransacao = new Transacao();
        novaTransacao.setValor(50.0);
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
        Cartao cartao = new Cartao();
        cartao.setEstaAtivado(true);
        cartao.setSaldo(500.0);

        // Mockar uma transação recente idêntica (mesmo valor e comerciante)
        LocalDateTime agora = LocalDateTime.now();
        Transacao transacaoExistente = new Transacao();
        transacaoExistente.setDataTransacao(agora.minusSeconds(60));
        transacaoExistente.setValor(100.0);
        transacaoExistente.setComerciante("Loja A");

        // Mockando o repositório para retornar a transação existente
        when(transacaoRepository.findByCartaoAndDataTransacaoAfter(eq(cartao), any(LocalDateTime.class)))
                .thenReturn(List.of(transacaoExistente));

        // Nova transação que seria duplicada
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

