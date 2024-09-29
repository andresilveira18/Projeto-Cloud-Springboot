package br.edu.ibmec.projeto_cloud.service;

import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    public void enviarNotificacao(String mensagem, String destinatario) {
        // Simula o envio da notificação (pode ser e-mail ou SMS)
        System.out.println("Notificação enviada para " + destinatario + ": " + mensagem);
    }
}
