package br.com.fiap.bo;

import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.to.UsuarioTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioBO {

    private List<UsuarioTO> usuarios = new ArrayList<>();

    // Valida todos os campos obrigatórios
    public boolean validarCampos(UsuarioTO usuario) {
        return usuario != null &&
                usuario.getNomeCompleto() != null && !usuario.getNomeCompleto().isBlank() &&
                usuario.getEmail() != null && usuario.getEmail().contains("@") &&
                usuario.getSenha() != null && usuario.getSenha().length() >= 6 &&
                usuario.getDataNascimento() != null &&
                usuario.getDataNascimento().isBefore(LocalDate.now());
    }

    // Cadastrar usuário (simulação em memória)
    public boolean cadastrarUsuario(UsuarioTO usuario) {
        if (validarCampos(usuario)) {
            usuarios.add(usuario);
            System.out.println("Usuário cadastrado com sucesso: " + usuario.getNomeCompleto());
            return true;
        } else {
            System.out.println("Falha ao cadastrar usuário. Dados inválidos.");
            return false;
        }
    }

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
    }

    // Listar usuários simulados (pode ser substituído por DAO.listarUsuarios)
    public List<UsuarioTO> listarUsuarios() {
        return usuarios;
    }
}