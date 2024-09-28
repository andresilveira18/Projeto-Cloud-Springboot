package br.edu.ibmec.projeto_cloud.model;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Data
@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    @JsonBackReference 
    private Cartao cartao;

    @Column
    @NotNull
    private LocalDateTime dataTransacao;

    @Column
    @NotNull
    private double valor;

    @Column
    @NotBlank
    private String comerciante;
}
