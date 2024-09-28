package br.edu.ibmec.projeto_cloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ibmec.projeto_cloud.model.Transacao;
import br.edu.ibmec.projeto_cloud.model.Cartao;

import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Integer> {

    // Método para buscar transações baseadas no cartão e em um período de tempo
    List<Transacao> findByCartaoAndDataTransacaoAfter(Cartao cartao, LocalDateTime dataTransacao);
}
