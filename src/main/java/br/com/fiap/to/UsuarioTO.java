package br.com.fiap.to;

import java.time.LocalDate;

public class UsuarioTO {
    private String nome;
    private String email;
    private String senha;
    private LocalDate dataNascimento;

    public UsuarioTO() {}

    public UsuarioTO(String nome, String email, String senha, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return "Usuário: " + nome +
                " | E-mail: " + email +
                " | Data de nascimento: " + dataNascimento;
    }
}