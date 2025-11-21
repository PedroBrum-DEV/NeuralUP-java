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

    private UsuarioDAO usuarioDAO;

    public UsuarioResource() throws SQLException, ClassNotFoundException {
        this.usuarioDAO = new UsuarioDAO();
    }

    @GET
    public Response listarUsuarios() throws SQLException {
        List<UsuarioTO> usuarios = usuarioDAO.listarUsuarios();
        if (usuarios.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Nenhum usuário encontrado"))
                    .build();
        }
        return Response.ok(usuarios).build();
    }

    @GET
    @Path("/{email}")
    public Response buscarPorEmail(@PathParam("email") String email) throws SQLException {
        UsuarioTO usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Usuário não encontrado"))
                    .build();
        }
        return Response.ok(usuario).build();
    }

    @POST
    @Path("/cadastrar")
    public Response cadastrarUsuario(UsuarioTO usuario) throws SQLException {
        UsuarioBO bo = new UsuarioBO();

        if (!bo.validarCampos(usuario)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Campos inválidos. Verifique nome, e-mail, senha e data de nascimento."))
                    .build();
        }

        usuarioDAO.cadastrarUsuario(usuario);

        return Response.status(Response.Status.CREATED)
                .entity(usuario)
                .build();
    }

    @PUT
    @Path("/{email}")
    public Response atualizarUsuario(@PathParam("email") String email, UsuarioTO usuario) throws SQLException {
        usuario.setEmail(email);
        boolean atualizado = usuarioDAO.atualizarUsuario(usuario);
        if (atualizado) {
            return Response.ok(Map.of("message", "Usuário atualizado!")).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Usuário não encontrado"))
                    .build();
        }
    }

    @DELETE
    @Path("/{email}")
    public Response deletarUsuario(@PathParam("email") String email) throws SQLException {
        boolean deletado = usuarioDAO.deletarUsuario(email);
        if (deletado) {
            return Response.ok(Map.of("message", "Usuário removido!")).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Usuário não encontrado"))
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response realizarLogin(UsuarioTO usuario) throws SQLException {
        UsuarioBO bo = new UsuarioBO();
        boolean autenticado = bo.realizarLogin(usuario.getEmail(), usuario.getSenha());

        if (!autenticado) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "E-mail ou senha inválidos"))
                    .build();
        }

        UsuarioTO usuarioBanco = usuarioDAO.buscarPorEmail(usuario.getEmail());

        LoginResponse resp = new LoginResponse(
                "fake-token-123",
                usuarioBanco.getNome()
        );

        return Response.ok(resp).build();
    }
}
