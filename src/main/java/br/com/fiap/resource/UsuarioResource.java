package br.com.fiap.resource;

import br.com.fiap.bo.UsuarioBO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.to.UsuarioTO;
import br.com.fiap.to.LoginResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;



@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @GET
    public Response listarUsuarios() throws SQLException {
        List<UsuarioTO> usuarios = usuarioDAO.listarUsuarios();
        return Response.ok(usuarios).build();
    }

    @GET
    @Path("/{email}")
    public Response buscarPorEmail(@PathParam("email") String email) throws SQLException {
        UsuarioTO usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null) {
            return Response.status(404).entity(Map.of("error", "Usuário não encontrado")).build();
        }
        return Response.ok(usuario).build();
    }

    @POST
    @Path("/cadastrar")
    public Response cadastrarUsuario(UsuarioTO usuario) throws SQLException {
        UsuarioBO bo = new UsuarioBO();

        if (!bo.validarCampos(usuario)) {
            return Response.status(400)
                    .entity(Map.of("error", "Dados inválidos"))
                    .build();
        }

        usuarioDAO.cadastrarUsuario(usuario);

        LoginResponse resp = new LoginResponse(
                "fake-token-123",
                usuario.getNomeCompleto()
        );

        return Response.status(201)
                .entity(resp)
                .build();
    }


    @PUT
    @Path("/{email}")
    public Response atualizarUsuario(@PathParam("email") String email, UsuarioTO usuario) throws SQLException {
        usuario.setEmail(email);
        boolean atualizado = usuarioDAO.atualizarUsuario(usuario);

        if (!atualizado) {
            return Response.status(404).entity(Map.of("error", "Usuário não encontrado")).build();
        }

        return Response.ok(Map.of("message", "Usuário atualizado!")).build();
    }

    @DELETE
    @Path("/{email}")
    public Response deletarUsuario(@PathParam("email") String email) throws SQLException {
        boolean deletado = usuarioDAO.deletarUsuario(email);
        if (!deletado) {
            return Response.status(404).entity(Map.of("error", "Usuário não encontrado")).build();
        }
        return Response.ok(Map.of("message", "Usuário removido!")).build();
    }

    public static class LoginRequest {
        public String email;
        public String senha;
    }

    @POST
    @Path("/login")
    public Response realizarLogin(LoginRequest req) throws SQLException {
        UsuarioBO bo = new UsuarioBO();
        boolean autenticado = bo.realizarLogin(req.email, req.senha);

        if (!autenticado) {
            return Response.status(401)
                    .entity(Map.of("error", "E-mail ou senha inválidos"))
                    .build();
        }

        UsuarioTO usuarioBanco = usuarioDAO.buscarPorEmail(req.email);

        LoginResponse resp = new LoginResponse(
                "fake-token-123",
                usuarioBanco.getNomeCompleto()
        );

        return Response.ok(resp).build();
    }
}
