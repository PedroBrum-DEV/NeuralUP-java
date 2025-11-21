package br.com.fiap.bo;

import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.to.UsuarioTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioBO {

    // Valida todos os campos obrigatórios
    public boolean validarCampos(UsuarioTO usuario) {
        return usuario != null &&
                usuario.getNome() != null && !usuario.getNome().isBlank() &&
                usuario.getEmail() != null && usuario.getEmail().contains("@") &&
                usuario.getSenha() != null && usuario.getSenha().length() >= 6 &&
                usuario.getDataNascimento() != null &&
                usuario.getDataNascimento().isBefore(LocalDate.now());
    };

    // Login com autenticação via DAO
    public boolean realizarLogin(String email, String senha) {
        if (email == null || senha == null || email.isBlank() || senha.isBlank()) {
            return false;
        }

        try {
            UsuarioDAO dao = new UsuarioDAO();
            UsuarioTO usuario = dao.buscarPorEmail(email);

            if (usuario != null && usuario.getSenha().equals(senha)) {
                System.out.println("Login realizado com sucesso para o usuário: " + email);
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Falha no login para o usuário: " + email);
        return false;
    };
}