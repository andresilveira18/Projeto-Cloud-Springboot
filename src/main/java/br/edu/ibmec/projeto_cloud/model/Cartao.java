package br.edu.ibmec.projeto_cloud.model;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Cartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotNull
    private long numeroCartao;

    @Column
    @NotNull
    private LocalDate dataValidade;

    @Column
    @NotNull
    private int cvv;

    @Column
    @NotNull
    private double limite; 

    @Column
    @NotNull
    private double saldo;

    @Column
    @NotNull
    private Boolean estaAtivado;

    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "cartao_id")
    @JsonManagedReference 
    private List<Transacao> transacoes;

    public void adicionarTransacao(Transacao transacao) {
        this.transacoes.add(transacao);
    }
}
