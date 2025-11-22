package br.com.fiap.to;

public class LoginResponse {
    private String token;
    private String nomeCompleto;

    public LoginResponse() {}

    public LoginResponse(String token, String nomeCompleto) {
        this.token = token;
        this.nomeCompleto = nomeCompleto;
    }

    public String getToken() {
        return token;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }
}
