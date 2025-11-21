package br.com.fiap.dao;

import br.com.fiap.to.UsuarioTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Connection conn = ConnectionFactory.getConnection();

    public UsuarioDAO() throws SQLException, ClassNotFoundException {}

    // CREATE
    public void cadastrarUsuario(UsuarioTO usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, data_nascimento) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            stmt.executeUpdate();
        }
    }

    // READ - listar todos
    public List<UsuarioTO> listarUsuarios() throws SQLException {
        List<UsuarioTO> lista = new ArrayList<>();
        String sql = "SELECT nome, email, senha, data_nascimento FROM usuario";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new UsuarioTO(
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getDate("data_nascimento").toLocalDate()
                ));
            }
        }
        return lista;
    }

    // READ - buscar por e-mail
    public UsuarioTO buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT nome, email, senha, data_nascimento FROM usuario WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UsuarioTO(
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getDate("data_nascimento").toLocalDate()
                    );
                }
            }
        }
        return null;
    }

    // UPDATE
    public boolean atualizarUsuario(UsuarioTO usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, senha = ?, data_nascimento = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSenha());
            stmt.setDate(3, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(4, usuario.getEmail());
            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deletarUsuario(String email) throws SQLException {
        String sql = "DELETE FROM usuario WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        }
    }
}