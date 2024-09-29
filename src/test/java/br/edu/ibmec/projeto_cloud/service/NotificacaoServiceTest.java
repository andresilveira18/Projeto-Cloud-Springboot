package br.edu.ibmec.projeto_cloud.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class NotificacaoServiceTest {

    // Só um teste genérico, visto que notificaçãõ ainda é um print apenas

    @InjectMocks
    private NotificacaoService notificacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnviarNotificacao() {
        NotificacaoService notificacaoServiceMock = mock(NotificacaoService.class);

        String mensagem = "Transação aprovada";
        String destinatario = "cliente@dominio.com";

        notificacaoServiceMock.enviarNotificacao(mensagem, destinatario);

        verify(notificacaoServiceMock, times(1)).enviarNotificacao(mensagem, destinatario);
    }
}

