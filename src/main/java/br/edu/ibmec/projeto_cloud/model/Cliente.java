package br.edu.ibmec.projeto_cloud.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank
    private String nome;

    @Column
    @NotBlank
    private String cpf;

    @Column
    @NotNull
    private LocalDate dataNascimento;

    @Column
    @Email
    private String email;

    @Column
    @NotBlank
    private String telefone;

    @Column
    @NotBlank
    private String endereco;

    @OneToMany
    @JoinColumn(referencedColumnName = "id", name = "cliente_id")
    private List<Cartao> cartoes = new ArrayList<>();

    public void associarCartao(Cartao cartao) {
        this.cartoes.add(cartao);
    }
}
