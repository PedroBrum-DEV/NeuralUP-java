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
                    .entity("Nenhum usuário encontrado.")
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
                    .entity("Usuário com e-mail " + email + " não encontrado.")
                    .build();
        }
        return Response.ok(usuario).build();
    }

    @POST
    public Response cadastrarUsuario(UsuarioTO usuario) throws SQLException {
        UsuarioBO bo = new UsuarioBO();
        if (!bo.validarCampos(usuario)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campos inválidos. Verifique nome, e-mail, senha e data de nascimento.")
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
            return Response.ok("Usuário atualizado com sucesso!").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário com e-mail " + email + " não encontrado.")
                    .build();
        }
    }

    @DELETE
    @Path("/{email}")
    public Response deletarUsuario(@PathParam("email") String email) throws SQLException {
        boolean deletado = usuarioDAO.deletarUsuario(email);
        if (deletado) {
            return Response.ok("Usuário removido com sucesso!").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário com e-mail " + email + " não encontrado.")
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
                    .entity("{\"error\":\"E-mail ou senha inválidos.\"}")
                    .build();
        }

        LoginResponse resp = new LoginResponse("fake-token-123", usuario.getNomeCompleto());

        return Response.ok(resp).build();
    }
}
