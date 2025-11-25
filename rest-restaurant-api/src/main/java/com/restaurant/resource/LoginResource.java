package com.restaurant.resource;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.restaurant.service.LoginService;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

    private LoginService loginService = new LoginService();

    @POST
    public Response login(HashMap<String, String> req) {

        String usuario = req.get("usuario");
        String contrasena = req.get("contrasena");

        HashMap<String, Object> resultado = loginService.login(usuario, contrasena);

        if (resultado == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":\"error\",\"message\":\"Usuario o contraseña incorrectos\"}")
                    .build();
        }

        return Response.ok(resultado).build();
    }
}