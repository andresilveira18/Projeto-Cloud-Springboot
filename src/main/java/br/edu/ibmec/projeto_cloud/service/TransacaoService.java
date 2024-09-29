package br.edu.ibmec.projeto_cloud.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.ibmec.projeto_cloud.dto.TransacaoResponseDTO;
import br.edu.ibmec.projeto_cloud.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.projeto_cloud.model.Cartao;
import br.edu.ibmec.projeto_cloud.model.Cliente;
import br.edu.ibmec.projeto_cloud.model.Transacao;
import br.edu.ibmec.projeto_cloud.service.NotificacaoService;
import br.edu.ibmec.projeto_cloud.repository.CartaoRepository;
import br.edu.ibmec.projeto_cloud.repository.ClienteRepository;
import br.edu.ibmec.projeto_cloud.repository.TransacaoRepository;

@Service
public class TransacaoService {

    private final CartaoRepository cartaoRepository;
    private final TransacaoRepository transacaoRepository;
    private final ClienteRepository clienteRepository;
    private final NotificacaoService notificacaoService;

    public TransacaoService(CartaoRepository cartaoRepository, TransacaoRepository transacaoRepository, ClienteRepository clienteRepository, NotificacaoService notificacaoService) {
        this.cartaoRepository = cartaoRepository;
        this.transacaoRepository = transacaoRepository;
        this.clienteRepository = clienteRepository;
        this.notificacaoService = notificacaoService;
    }

    // Aplica regras de negócio para validar se a transação pode ser autorizada
    public void validarTransacao(Cartao cartao, Transacao transacao) {
        Cliente cliente = cartao.getCliente();
        String destinatario = (cliente != null && cliente.getEmail() != null) ? cliente.getEmail() : "suporte@dominio.com";

        // Verificar se o cartão está ativo
        if (!cartao.getEstaAtivado()) {
            notificacaoService.enviarNotificacao("Transação recusada: cartão inativo", destinatario);
            throw new CartaoInativoException("Cartão não está ativo");
        }

        // Verificar se o valor da transação excede o saldo disponível
        if (transacao.getValor() > cartao.getSaldo()) {
            notificacaoService.enviarNotificacao("Transação recusada: limite insuficiente", destinatario);
            throw new LimiteInsuficienteException("Limite insuficiente");
        }

        // Verificar alta frequência de transações (mais de 3 em 2 minutos)
        LocalDateTime doisMinutosAtras = transacao.getDataTransacao().minusMinutes(2);
        List<Transacao> transacoesRecentes = transacaoRepository.findByCartaoAndDataTransacaoAfter(cartao, doisMinutosAtras);

        if (transacoesRecentes.size() >= 3) {
            notificacaoService.enviarNotificacao("Transação recusada: alta frequência de transações", destinatario);
            throw new FrequenciaAltaTransacoesException("Alta frequência de transações em pequeno intervalo");
        }

        // Verificar transações duplicadas (mesmo valor e comerciante em 2 minutos)
        boolean transacaoDuplicada = transacoesRecentes.stream()
                .anyMatch(t -> Double.compare(t.getValor(), transacao.getValor()) == 0 && t.getComerciante().equals(transacao.getComerciante()));

        if (transacaoDuplicada) {
            notificacaoService.enviarNotificacao("Transação recusada: transação duplicada", destinatario);
            throw new TransacaoDuplicadaException("Transação duplicada");
        }

        // Eu colocaria uma exceção de validar se há um cliente associado ao cartao
    }

    // Método para autorizar uma transação, caso esteja tudo bem com a validação
    @Transactional
    public TransacaoResponseDTO autorizarTransacao(int cartaoId, Transacao transacao) {
        // Buscar o objeto Cartao pelo ID
        Cartao cartao = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado"));

        // Validar a transação (lança exceções em caso de falha)
        validarTransacao(cartao, transacao);

        // Associar cartao
        transacao.setCartao(cartao);

        // Atualizar saldo do cartão
        cartao.setSaldo(cartao.getSaldo() - transacao.getValor());

        // Persistir as alterações no banco de dados
        cartao.adicionarTransacao(transacao);
        cartaoRepository.save(cartao);
        transacaoRepository.save(transacao);

        // Buscar Cliente para Notificação
        Cliente cliente = cartao.getCliente(); // Supondo que o Cartao tem um método para pegar o cliente
        String destinatario;

        if (cliente == null || cliente.getEmail() == null) {
            // Caso não tenha cliente associado, use um e-mail genérico
            destinatario = "suporte@dominio.com";
        } else {
            destinatario = cliente.getEmail();
        }

        // Enviar notificação
        notificacaoService.enviarNotificacao(
                "Transação autorizada com sucesso para o valor de R$" + transacao.getValor(),
                destinatario
        );

        // Retornar uma resposta de sucesso
        return new TransacaoResponseDTO("Sucesso", "Transação autorizada");
    }

    // Método para buscar todas as transações aprovadas de um cliente
    public List<Transacao> buscarTransacoesPorCliente(int clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));

        // Busca todas as transações associadas aos cartões do cliente
        return cliente.getCartoes().stream()
            .flatMap(cartao -> cartao.getTransacoes().stream())
            .collect(Collectors.toList());
    }
}
