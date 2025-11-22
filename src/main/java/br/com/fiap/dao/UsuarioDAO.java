package br.com.fiap.dao;

import br.com.fiap.to.UsuarioTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public UsuarioDAO() {}

    // CREATE
    public void cadastrarUsuario(UsuarioTO usuario) throws SQLException {
        String sql = "INSERT INTO usuario (NOME_COMPLETO, EMAIL, SENHA, DATA_NASCIMENTO) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));

            stmt.executeUpdate();
        }
    }

    // READ - listar todos
    public List<UsuarioTO> listarUsuarios() throws SQLException {
        List<UsuarioTO> lista = new ArrayList<>();
        String sql = "SELECT NOME_COMPLETO, EMAIL, SENHA, DATA_NASCIMENTO FROM USUARIO";

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new UsuarioTO(
                        rs.getString("NOME_COMPLETO"),
                        rs.getString("EMAIL"),
                        rs.getString("SENHA"),
                        rs.getDate("DATA_NASCIMENTO").toLocalDate()
                ));
            }
        }

        return lista;
    }

    // READ - buscar por e-mail
    public UsuarioTO buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT NOME_COMPLETO, EMAIL, SENHA, DATA_NASCIMENTO FROM USUARIO WHERE EMAIL = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UsuarioTO(
                            rs.getString("NOME_COMPLETO"),
                            rs.getString("EMAIL"),
                            rs.getString("SENHA"),
                            rs.getDate("DATA_NASCIMENTO").toLocalDate()
                    );
                }
            }
        }

        return null;
    }

    // UPDATE
    public boolean atualizarUsuario(UsuarioTO usuario) throws SQLException {
        String sql = "UPDATE USUARIO SET NOME_COMPLETO = ?, SENHA = ?, DATA_NASCIMENTO = ? WHERE EMAIL = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getSenha());
            stmt.setDate(3, Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(4, usuario.getEmail());

            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deletarUsuario(String email) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE EMAIL = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            return stmt.executeUpdate() > 0;
        }
    }
}