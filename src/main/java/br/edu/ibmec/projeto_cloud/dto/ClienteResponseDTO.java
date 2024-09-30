package br.edu.ibmec.projeto_cloud.dto;

import java.time.LocalDate;
import java.util.List;

public class ClienteResponseDTO {
    private int id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String email;
    private String telefone;
    private String endereco;
    private List<CartaoResponseDTO> cartoes;

    // Construtor
    public ClienteResponseDTO(int id, String nome, String cpf, LocalDate dataNascimento,
                              String email, String telefone, String endereco, List<CartaoResponseDTO> cartoes) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cartoes = cartoes;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public List<CartaoResponseDTO> getCartoes() {
        return cartoes;
    }

}




