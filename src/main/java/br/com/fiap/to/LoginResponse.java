package br.com.fiap.to;

public class LoginResponse {
    private String token;
    private String nome;

    public LoginResponse() {}

    public LoginResponse(String token, String nome) {
        this.token = token;
        this.nome = nome;
    }

    public String getToken() {
        return token;
    }

    public String getNome() {
        return nome;
    }
}
